package com.poianitibaldizhou.trackme.apigateway.filter.route;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.poianitibaldizhou.trackme.apigateway.security.TokenAuthenticationFilter;
import com.poianitibaldizhou.trackme.apigateway.service.ThirdPartyAuthenticationService;
import com.poianitibaldizhou.trackme.apigateway.service.UserAuthenticationService;
import com.poianitibaldizhou.trackme.apigateway.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

/**
 * This filters add the necessary headers to a request. Indeed, it add the third party customer id, if the client
 * that called the api is logged as third party customer, or adds the user ssn if the client that called the api
 * is logged as a user.
 */
public class TranslationFilter extends ZuulFilter {

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @Autowired
    private ThirdPartyAuthenticationService thirdPartyAuthenticationService;

    @Autowired
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Override
    public String filterType() {
        return FilterConstants.ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();

        final String token = tokenAuthenticationFilter.getToken(ctx.getRequest());

        if (thirdPartyAuthenticationService.findThirdPartyByToken(token).isPresent()) {
            ctx.addZuulRequestHeader(Constants.THIRD_PARTY_ID_HEADER_KEY,
                    thirdPartyAuthenticationService.findThirdPartyByToken(token).orElseThrow(IllegalStateException::new).getId().toString());
            ctx.addZuulRequestHeader(Constants.USER_SSN_HEADER_KEY, Constants.EMPTY_HEADER);
        } else if (userAuthenticationService.findUserByToken(token).isPresent()) {
            ctx.addZuulRequestHeader(Constants.USER_SSN_HEADER_KEY,
                    userAuthenticationService.findUserByToken(token).orElseThrow(IllegalStateException::new).getSsn());
            ctx.addZuulRequestHeader(Constants.THIRD_PARTY_ID_HEADER_KEY, Constants.EMPTY_HEADER);
        } else {
            throw new IllegalStateException();
        }

        return null;
    }
}
