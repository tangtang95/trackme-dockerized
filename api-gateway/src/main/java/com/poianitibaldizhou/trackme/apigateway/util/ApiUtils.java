package com.poianitibaldizhou.trackme.apigateway.util;

import com.poianitibaldizhou.trackme.apigateway.entity.Api;
import com.poianitibaldizhou.trackme.apigateway.repository.ApiRepository;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Util class for the management of the api
 */
@Service
public class ApiUtils {

    private final ApiRepository apiRepository;

    public ApiUtils(ApiRepository apiRepository) {
        this.apiRepository = apiRepository;
    }

    /**
     * Returns an api whose uri is the beginning of the provided requestApi.
     * Furthermore, the method of the api is the one specified by method.
     *
     * @param requestApi api that will be matched against the available ones
     * @param method method used to access the api
     * @return api that matches the above mentioned criteria. If nothing is found, a null
     * value is returned
     */
    public Api getApiByUriWithNoPathVar(String requestApi, HttpMethod method) {
        List<Api> apiList = apiRepository.findAllByHttpMethod(method);

        for(Api api : apiList) {
            if(requestApi.startsWith(api.getStartingUri()))
                return api;
        }

        return null;
    }
}
