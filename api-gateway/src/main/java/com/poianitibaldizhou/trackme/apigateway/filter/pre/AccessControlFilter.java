package com.poianitibaldizhou.trackme.apigateway.filter.pre;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.poianitibaldizhou.trackme.apigateway.entity.Api;
import com.poianitibaldizhou.trackme.apigateway.security.TokenAuthenticationFilter;
import com.poianitibaldizhou.trackme.apigateway.service.ThirdPartyAuthenticationService;
import com.poianitibaldizhou.trackme.apigateway.service.UserAuthenticationService;
import com.poianitibaldizhou.trackme.apigateway.util.ApiUtils;
import com.poianitibaldizhou.trackme.apigateway.util.Constants;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.security.AccessControlException;

/**
 * This filter controls the access of all the api calls. Indeed, it checks if the client is a third party
 * or a user and acts accordingly: api that are reserved for users, won't permit access to third party customers
 * and viceversa.
 */
public class AccessControlFilter extends ZuulFilter {
    private final UserAuthenticationService userAuthenticationService;

    private final ThirdPartyAuthenticationService thirdPartyAuthenticationService;

    private final ApiUtils apiUtils;

    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    /**
     * Creates a new access control filter
     *
     * @param userAuthenticationService user authentication service, useful to access information by token
     * @param thirdPartyAuthenticationService third party authentication service, useful to access information by token
     * @param apiUtils api utiles for accessing info about avaiable apis
     * @param tokenAuthenticationFilter token authentication filter for accessing information regarding the token
     */
    public AccessControlFilter(UserAuthenticationService userAuthenticationService, ThirdPartyAuthenticationService thirdPartyAuthenticationService,
                               ApiUtils apiUtils, TokenAuthenticationFilter tokenAuthenticationFilter) {
        this.userAuthenticationService = userAuthenticationService;
        this.thirdPartyAuthenticationService = thirdPartyAuthenticationService;
        this.apiUtils = apiUtils;
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
    }

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();

        // Find the api that is currently being accessed
        Api calledApi = apiUtils.getApiByUriWithNoPathVar(request.getRequestURI(), HttpMethod.resolve(request.getMethod()));

        if(calledApi == null) {
            throw new ZuulException(Constants.API_NOT_FOUND_EXCEPTION, HttpStatus.BAD_REQUEST.value(), Constants.API_NOT_FOUND_EXCEPTION);
        }

        // Check if the client accessing the api has real access to it
        final String token = tokenAuthenticationFilter.getToken(request);

        switch (calledApi.getPrivilege()) {
            case THIRD_PARTY:
                if(!thirdPartyAuthenticationService.findThirdPartyByToken(token).isPresent()) {
                    throw new AccessControlException(Constants.ACCESS_CONTROL_EXCEPTION_USER);
                }
                break;
            case USER:
                if(!userAuthenticationService.findUserByToken(token).isPresent()) {
                    throw new AccessControlException(Constants.ACCESS_CONTROL_EXCEPTION_TP);
                }
                break;
            case ALL:
                // Everything is fine nothing to do
                break;
        }

        return null;
    }
}
