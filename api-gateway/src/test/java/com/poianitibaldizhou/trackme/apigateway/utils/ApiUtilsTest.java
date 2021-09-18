package com.poianitibaldizhou.trackme.apigateway.utils;

import com.poianitibaldizhou.trackme.apigateway.entity.Api;
import com.poianitibaldizhou.trackme.apigateway.repository.ApiRepository;
import com.poianitibaldizhou.trackme.apigateway.util.ApiUtils;
import com.poianitibaldizhou.trackme.apigateway.util.Privilege;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test the utils for accessing apis
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApiUtilsTest {

    @Autowired
    private ApiRepository apiRepository;

    @Autowired
    private ApiUtils apiUtils;

    /**
     * Test the retrieve of an api when it is not loaded with data.sql
     */
    @Test
    public void testNotPresentApi() {
        assertNull(apiUtils.getApiByUriWithNoPathVar("nonPresentApi", HttpMethod.GET));
    }

    /**
     * Test the retrieve of an api when it is present in data.sql
     */
    @Test
    public void testPresentApi() {
        System.out.println("REPO: "+ apiRepository.findAllByHttpMethod(HttpMethod.GET));

        Api api = apiUtils.getApiByUriWithNoPathVar("/grouprequestservice/grouprequests/id/1", HttpMethod.GET);
        assertEquals("/grouprequestservice/grouprequests/id", api.getStartingUri() );
        assertEquals(HttpMethod.GET, api.getHttpMethod());
        assertEquals(Privilege.THIRD_PARTY, api.getPrivilege());
    }
}
