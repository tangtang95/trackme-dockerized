package com.poianitibaldizhou.trackme.apigateway.controller;

import com.poianitibaldizhou.trackme.apigateway.assembler.UserAssembler;
import com.poianitibaldizhou.trackme.apigateway.entity.User;
import com.poianitibaldizhou.trackme.apigateway.exception.AlreadyPresentSsnException;
import com.poianitibaldizhou.trackme.apigateway.exception.AlreadyPresentUsernameException;
import com.poianitibaldizhou.trackme.apigateway.security.TokenAuthenticationProvider;
import com.poianitibaldizhou.trackme.apigateway.service.ThirdPartyAuthenticationService;
import com.poianitibaldizhou.trackme.apigateway.service.UserAuthenticationService;
import com.poianitibaldizhou.trackme.apigateway.service.UserAccountManagerService;
import com.poianitibaldizhou.trackme.apigateway.util.ApiUtils;
import com.poianitibaldizhou.trackme.apigateway.util.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.sql.Date;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit test of the public controller of the users
 */
@RunWith(SpringRunner.class)
@WebMvcTest(PublicUserController.class)
@Import({UserAssembler.class})
public class PublicUserControllerUnitTest {

    @Value(Constants.SERVER_ADDRESS)
    private String serverAddress;

    @Value(Constants.PORT)
    private Integer port;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserAccountManagerService service;


    // unused but needed to set up the context application by Spring

    @MockBean
    private UserAuthenticationService authenticationService;

    @MockBean
    private TokenAuthenticationProvider tokenAuthenticationProvider;

    @MockBean
    private ApiUtils apiUtils;

    @MockBean
    private ThirdPartyAuthenticationService thirdPartyAuthenticationService;

    /**
     * Test the registration of a user
     *
     * @throws Exception due to mvc post method
     */
    @Test
    public void testRegisterUser() throws Exception {
        User user = new User();
        user.setSsn("newUser");
        user.setBirthNation("Italia");
        user.setBirthCity("Brescia");
        user.setBirthDate(new Date(0));
        user.setFirstName("TangTang");
        user.setLastName("Zhou");
        user.setPassword("tangpass");
        user.setUsername("vertex95");

        given(service.registerUser(any(User.class))).willReturn(user);

        String json = "{\n" +
                "   \"ssn\":\"newUser\",\n"+
                "   \"password\":\"tangpass\",\n" +
                "   \"username\":\"vertex95\",\n" +
                "   \"firstName\":\"TangTang\",\n" +
                "   \"lastName\":\"Zhou\",\n" +
                "   \"birthDate\":\"1970-01-01\",\n" +
                "   \"birthCity\":\"Brescia\",\n" +
                "   \"birthNation\":\"Italia\"\n" +
                "}";

        mvc.perform(post(Constants.PUBLIC_USER_API + "/newUser").
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("ssn", is(user.getSsn())))
                .andExpect(jsonPath("username", is(user.getUsername())))
                .andExpect(jsonPath("firstName", is(user.getFirstName())))
                .andExpect(jsonPath("lastName", is(user.getLastName())))
                .andExpect(jsonPath("birthDate", is(user.getBirthDate().toString())))
                .andExpect(jsonPath("birthCity", is(user.getBirthCity())))
                .andExpect(jsonPath("birthNation", is(user.getBirthNation())))
                .andExpect(jsonPath("_links.self.href", is("http://localhost" +
                        Constants.SECURED_USER_API + Constants.GET_USER_INFO_API)));
    }

    /**
     * Test the registration of a user when another user with the specified username is already registered
     *
     * @throws Exception due to mvc mock post method
     */
    @Test
    public void testUserRegistrationWhenUserNameAlreadyPresent() throws Exception {
        User user = new User();
        user.setSsn("newUser");
        user.setBirthNation("Italia");
        user.setBirthCity("Brescia");
        user.setBirthDate(new Date(0));
        user.setFirstName("TangTang");
        user.setLastName("Zhou");
        user.setPassword("tangpass");
        user.setUsername("alreadyPresentUsername");

        String json = "{\n" +
                "   \"ssn\":\"newUser\",\n"+
                "   \"password\":\"tangpass\",\n" +
                "   \"username\":\"alreadyPresentUsername\",\n" +
                "   \"firstName\":\"TangTang\",\n" +
                "   \"lastName\":\"Zhou\",\n" +
                "   \"birthDate\":\"1970-01-01\",\n" +
                "   \"birthCity\":\"Brescia\",\n" +
                "   \"birthNation\":\"Italia\"\n" +
                "}";

        given(service.registerUser(any(User.class))).willThrow(new AlreadyPresentUsernameException(user.getUsername()));

        mvc.perform(post(Constants.PUBLIC_USER_API+"/newUser").
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").content(json))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test the registration of a user when another user with the specified ssn is already registered
     *
     * @throws Exception due to mvc mock post method
     */
    @Test
    public void testUserRegistrationWhenSsnAlreadyPresent() throws Exception {
        User user = new User();
        user.setSsn("SsnAlreadyPresent");
        user.setBirthNation("Italia");
        user.setBirthCity("Brescia");
        user.setBirthDate(new Date(0));
        user.setFirstName("TangTang");
        user.setLastName("Zhou");
        user.setPassword("tangpass");
        user.setUsername("newUserName");

        String json = "{\n" +
                "   \"ssn\":\"SsnAlreadyPresent\",\n"+
                "   \"password\":\"tangpass\",\n" +
                "   \"username\":\"newUserName\",\n" +
                "   \"firstName\":\"TangTang\",\n" +
                "   \"lastName\":\"Zhou\",\n" +
                "   \"birthDate\":\"1970-01-01\",\n" +
                "   \"birthCity\":\"Brescia\",\n" +
                "   \"birthNation\":\"Italia\"\n" +
                "}";

        given(service.registerUser(any(User.class))).willThrow(new AlreadyPresentSsnException(user.getSsn()));

        mvc.perform(post( Constants.PUBLIC_USER_API + "/SsnAlreadyPresent").
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").content(json))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test the login of a user into the system
     *
     * @throws Exception due to mock mvc post method
     */
    @Test
    public void testUserLogin() throws Exception {
        User user = new User();
        user.setSsn("SsnAlreadyPresent");
        user.setBirthNation("Italia");
        user.setBirthCity("Brescia");
        user.setBirthDate(new Date(0));
        user.setFirstName("TangTang");
        user.setLastName("Zhou");
        user.setPassword("tangpass");
        user.setUsername("newUserName");

        given(authenticationService.userLogin("newUserName", "tangpass")).willReturn(Optional.of("newToken"));

        mvc.perform(post(Constants.PUBLIC_USER_API + Constants.LOGIN_USER_API  +"?username=newUserName&password=tangpass"))
                .andDo(print())
                .andExpect(jsonPath("token", is("newToken")))
                .andExpect(jsonPath("_links.logout.href", is("https://" + serverAddress + ":" + port + "/users/logout")))
                .andExpect(jsonPath("_links.info.href", is("https://" + serverAddress + ":" + port + "/users/info")))
                .andExpect(jsonPath("_links.pendingRequests.href", is("https://" + serverAddress + ":" + port + "/individualrequestservice/requests/users")))
                .andExpect(jsonPath("_links.postHealthData.href", is("https://" + serverAddress + ":" + port + "/sharedataservice/datacollection/healthdata")))
                .andExpect(jsonPath("_links.postPositionData.href", is("https://" + serverAddress + ":" + port + "/sharedataservice/datacollection/positiondata")))
                .andExpect(jsonPath("_links.postClusterData.href", is("https://" + serverAddress + ":" + port + "/sharedataservice/datacollection/clusterdata")))
                .andExpect(jsonPath("_links.getOwnData.href", is("https://" + serverAddress + ":" + port + "/sharedataservice/dataretrieval/users")));
    }

}
