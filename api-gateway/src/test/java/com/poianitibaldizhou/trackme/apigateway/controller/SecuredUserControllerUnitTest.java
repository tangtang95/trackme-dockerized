package com.poianitibaldizhou.trackme.apigateway.controller;

import com.poianitibaldizhou.trackme.apigateway.assembler.UserAssembler;
import com.poianitibaldizhou.trackme.apigateway.entity.User;
import com.poianitibaldizhou.trackme.apigateway.exception.SsnNotFoundException;
import com.poianitibaldizhou.trackme.apigateway.filter.pre.AccessControlFilter;
import com.poianitibaldizhou.trackme.apigateway.filter.route.TranslationFilter;
import com.poianitibaldizhou.trackme.apigateway.service.UserAuthenticationService;
import com.poianitibaldizhou.trackme.apigateway.service.UserAccountManagerService;
import com.poianitibaldizhou.trackme.apigateway.util.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit test of the controller of the secured part of the controller of the user.
 * In this test class the authentication is not considered: indeed, only the pieces of functionality of the
 * controller methods are verified.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = SecuredUserController.class, secure = false)
@Import({UserAssembler.class})
public class SecuredUserControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserAccountManagerService service;

    @MockBean
    private UserAuthenticationService userAuthenticationService;

    @MockBean
    private AccessControlFilter accessControlFilter;

    @MockBean
    private TranslationFilter translationFilter;


    /**
     * Test the retrieval of information related to a user by means of his ssn
     *
     * @throws Exception due to mock mvc method get
     */
    @Test
    public void testGetUserBySsn() throws Exception {
        User user = new User();
        user.setSsn("user1");
        user.setBirthNation("Italy");
        user.setBirthCity("Verona");
        user.setBirthDate(new Date(0));
        user.setFirstName("firstname1");
        user.setLastName("lastname1");
        user.setPassword("password1");
        user.setUsername("username1");

        given(service.getUserBySsn(null)).willReturn(user);

        mvc.perform(get(Constants.SECURED_USER_API + Constants.GET_USER_INFO_API).accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("ssn", is(user.getSsn())))
                .andExpect(jsonPath("firstName", is(user.getFirstName())))
                .andExpect(jsonPath("lastName", is(user.getLastName())))
                .andExpect(jsonPath("birthDate", is(user.getBirthDate().toString())))
                .andExpect(jsonPath("birthCity", is(user.getBirthCity())))
                .andExpect(jsonPath("birthNation", is(user.getBirthNation())))
                .andExpect(jsonPath("username", is(user.getUsername())));
    }

    /**
     * Test the retrieval of information related to a user by means of his ssn when no user with that ssn
     * is registered
     *
     * @throws Exception due to mock mvc method get
     */
    @Test
    public void testGetUserBySsnWhenNotPresent() throws Exception {
        given(service.getUserBySsn(null)).willThrow(new SsnNotFoundException("ssnNotFound"));

        mvc.perform(get(Constants.SECURED_USER_API + Constants.GET_USER_INFO_API).accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

}
