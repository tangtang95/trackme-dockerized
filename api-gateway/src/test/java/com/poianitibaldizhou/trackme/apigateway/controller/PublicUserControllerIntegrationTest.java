package com.poianitibaldizhou.trackme.apigateway.controller;

import com.poianitibaldizhou.trackme.apigateway.ApiGatewayApplication;
import com.poianitibaldizhou.trackme.apigateway.TestUtils;
import com.poianitibaldizhou.trackme.apigateway.entity.User;
import com.poianitibaldizhou.trackme.apigateway.repository.UserRepository;
import com.poianitibaldizhou.trackme.apigateway.util.Constants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Integration test for the public controller that manages the user accounts
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiGatewayApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"classpath:IntegrationUserControllerTestData.sql"})
@ActiveProfiles("test")
@Transactional
public class PublicUserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    private HttpHeaders httpHeaders = new HttpHeaders();

    private RestTemplate restTemplate;

    @Before
    public void setUp() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        restTemplate = TestUtils.getRestTemplate();
    }

    @After
    public void tearDown() {
        restTemplate = null;
    }

    /**
     * Test the registration of a user
     *
     * @throws Exception user already present in the data set
     */
    @Test
    public void testRegisterUser() throws Exception {
        User user = new User();
        user.setUsername("newUserName");
        user.setPassword("xcasggv");
        user.setLastName("Tiba");
        user.setFirstName("Mattia");
        user.setBirthDate(new Date(0));
        user.setBirthCity("Palermo");
        user.setBirthNation("Italia");

        HttpEntity<User> entity = new HttpEntity<>(user, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(Constants.PUBLIC_USER_API+ "/newSsn"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        User insertedUser = userRepository.findById("newSsn").orElseThrow(Exception::new);
        assertEquals(user.getUsername(), insertedUser.getUsername());
        assertEquals(user.getLastName(), insertedUser.getLastName());
        assertEquals(user.getFirstName(), insertedUser.getFirstName());
        assertEquals(user.getBirthCity(), insertedUser.getBirthCity());
        assertEquals(user.getBirthNation(), insertedUser.getBirthNation());
        assertTrue(passwordEncoder.matches(user.getPassword(), insertedUser.getPassword()));
        assertEquals(user.getBirthDate(), insertedUser.getBirthDate());
    }

    /**
     * Test the registration of a user when a user with the specified username is already present
     */
    @Test
    public void testRegisterUserWhenUserNameAlreadyPresent() {
        User user = new User();
        user.setUsername("username1");
        user.setPassword("xcasggv");
        user.setLastName("Tiba");
        user.setFirstName("Mattia");
        user.setBirthDate(new Date(0));
        user.setBirthCity("Palermo");
        user.setBirthNation("Italia");

        HttpEntity<User> entity = new HttpEntity<>(user, httpHeaders);

        try {
            restTemplate.exchange(
                    createURLWithPort(Constants.PUBLIC_USER_API + "/newSsn"),
                    HttpMethod.POST, entity, String.class);
        } catch(RestClientException e) {
            assertEquals("400 ", e.getMessage());
        }
    }

    /**
     * Test the registration of a user when a user with the specified ssn is already present
     */
    @Test
    public void testRegisterUserWhenSsnAlreadyPresent() {
        User user = new User();
        user.setUsername("newUsername");
        user.setPassword("xcasggv");
        user.setLastName("Tiba");
        user.setFirstName("Mattia");
        user.setBirthDate(new Date(0));
        user.setBirthCity("Palermo");
        user.setBirthNation("Italia");

        HttpEntity<User> entity = new HttpEntity<>(user, httpHeaders);

        try {
            restTemplate.exchange(
                    createURLWithPort(Constants.PUBLIC_USER_API + "/user1"),
                    HttpMethod.POST, entity, String.class);
        } catch(RestClientException e) {
            assertEquals("400 ", e.getMessage());
        }
    }

    /**
     * Test the registration of a user when not all of the necessary and mandatory parameters has been specified
     */
    @Test
    public void testRegisterUserWrongParameters() {
        User user = new User();
        user.setUsername("newUsername");
        user.setPassword("xcasggv");
        user.setLastName("Tiba");
        user.setFirstName("Mattia");
        user.setBirthNation("Italia");

        HttpEntity<User> entity = new HttpEntity<>(user, httpHeaders);

        try {
            restTemplate.exchange(
                    createURLWithPort(Constants.PUBLIC_USER_API + "/user100"),
                    HttpMethod.POST, entity, String.class);
        }catch(RestClientException e) {
            assertEquals("400 ", e.getMessage());
        }

    }

    /**
     * Test the log in when the credentials are wrong
     */
    @Test
    public void testLoginBadCredentials() {
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        try {
            restTemplate.exchange(createURLWithPort(
                    Constants.PUBLIC_USER_API + Constants.LOGIN_USER_API+"?username=wrong&password=wrong"),
                    HttpMethod.POST, entity, String.class);
            fail("Exception expected");
        } catch(Exception e) {
            assertEquals("401 ", e.getMessage());
        }
    }

    /**
     * Test the log in when the password is wrong
     */
    @Test
    public void testLoginBadCredentialsWhenWrongPassword() {
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        try {
            restTemplate.exchange(createURLWithPort(
                    Constants.PUBLIC_USER_API + Constants.LOGIN_USER_API+"?username=username1&password=wrong"),
                    HttpMethod.POST, entity, String.class);
            fail("Exception expected");
        } catch(Exception e) {
            assertEquals("401 ", e.getMessage());
        }
    }

    /**
     * Utility method to form the url with the injected port for a certain uri
     * @param uri uri that will access a certain resource of the application
     * @return url for accesing the resource identified by the uri
     */
    private String createURLWithPort(String uri) {
        return "https://localhost:" + port + uri;
    }
}
