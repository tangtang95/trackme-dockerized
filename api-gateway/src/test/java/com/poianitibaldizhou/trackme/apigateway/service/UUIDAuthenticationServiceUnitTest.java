package com.poianitibaldizhou.trackme.apigateway.service;

import com.poianitibaldizhou.trackme.apigateway.entity.ThirdPartyCustomer;
import com.poianitibaldizhou.trackme.apigateway.entity.User;
import com.poianitibaldizhou.trackme.apigateway.service.ThirdPartyAccountManagerService;
import com.poianitibaldizhou.trackme.apigateway.service.UUIDAuthenticationService;
import com.poianitibaldizhou.trackme.apigateway.service.UserAccountManagerService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Unit test of the authentication service implemented by means of UUID
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class UUIDAuthenticationServiceUnitTest {

    private UUIDAuthenticationService authenticationService;

    @MockBean
    private UserAccountManagerService userAccountManagerService;

    @MockBean
    private ThirdPartyAccountManagerService thirdPartyAccountManagerService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private User user;

    private ThirdPartyCustomer thirdPartyCustomer;

    @Before
    public void setUp() {
        user = new User();
        user.setUsername("userName1");
        user.setPassword("encodedPassword1");
        user.setSsn("ssn1");

        thirdPartyCustomer = new ThirdPartyCustomer();
        thirdPartyCustomer.setId(1L);
        thirdPartyCustomer.setEmail("tp1@provider.com");
        thirdPartyCustomer.setPassword("encodedTpPassword");

        authenticationService = new UUIDAuthenticationService(
                userAccountManagerService, thirdPartyAccountManagerService, passwordEncoder);
    }

    @After
    public void tearDown() {
        user = null;
        authenticationService = null;
    }

    /**
     * Test the user login when the provided credential are corrected
     */
    @Test
    public void userLoginTest() {
        when(userAccountManagerService.getUserByUserName("userName1")).thenReturn(user);
        when(passwordEncoder.matches("password1", user.getPassword())).thenReturn(true);

        assertTrue(authenticationService.userLogin("userName1", "password1").isPresent());
    }

    /**
     * Test the user login when the user has already performed the login
     */
    @Test
    public void userLoginTestWhenAlreadyLogged() {
        when(userAccountManagerService.getUserByUserName("userName1")).thenReturn(user);
        when(passwordEncoder.matches("password1", user.getPassword())).thenReturn(true);

        authenticationService.userLogin("userName1", "password1");
        assertTrue(authenticationService.userLogin("userName1", "password1").isPresent());
    }

    /**
     * Test the login of a user when the password doesn't match
     */
    @Test
    public void userLoginTestWhenPasswordNotMatching() {
        when(userAccountManagerService.getUserByUserName("userName1")).thenReturn(user);
        when(passwordEncoder.matches("password1", user.getPassword())).thenReturn(false);
        assertTrue(!authenticationService.userLogin("userName1", "password1").isPresent());
    }

    /**
     * Test the retrieval of a user token
     */
    @Test
    public void getUserByTokenTest() {
        when(userAccountManagerService.getUserByUserName("userName1")).thenReturn(user);
        when(passwordEncoder.matches("password1", user.getPassword())).thenReturn(true);
        String token = authenticationService.userLogin("userName1", "password1").get();
        assertEquals(user, authenticationService.findUserByToken(token).get());
    }

    /**
     * Test the retrieval of a user by token when no user with that token is present
     */
    @Test
    public void getUserByTokenWhenNotPresent() {
        assertTrue(!authenticationService.findUserByToken("fakeToken").isPresent());
    }

    /**
     * Test the logout of a user when that user is not logged
     */
    @Test(expected = IllegalStateException.class)
    public void userLogoutTestWhenIllegalAction() {
        authenticationService.userLogout(user);
    }

    /**
     * Test the logout of a user when that user is logged
     */
    @Test
    public void userLogoutTest() {
        when(userAccountManagerService.getUserByUserName("userName1")).thenReturn(user);
        when(passwordEncoder.matches("password1", user.getPassword())).thenReturn(true);

        String token = authenticationService.userLogin("userName1", "password1").get();
        authenticationService.userLogout(user);

        assertTrue(!authenticationService.findUserByToken(token).isPresent());
    }

    /**
     * Test the login of a third party customer
     */
    @Test
    public void testTpLogin() {
        when(thirdPartyAccountManagerService.getThirdPartyByEmail(thirdPartyCustomer.getEmail())).thenReturn(thirdPartyCustomer);
        when(passwordEncoder.matches("password1", thirdPartyCustomer.getPassword())).thenReturn(true);

        assertTrue(authenticationService.thirdPartyLogin(thirdPartyCustomer.getEmail(), "password1").isPresent());
    }

    /**
     * Test the login of a third party customer when he is already logged
     */
    @Test
    public void testTpLoginWhenAlreadyLogged() {
        when(thirdPartyAccountManagerService.getThirdPartyByEmail(thirdPartyCustomer.getEmail())).thenReturn(thirdPartyCustomer);
        when(passwordEncoder.matches("password1", thirdPartyCustomer.getPassword())).thenReturn(true);

        authenticationService.thirdPartyLogin(thirdPartyCustomer.getEmail(), "password1");
        assertTrue(authenticationService.thirdPartyLogin(thirdPartyCustomer.getEmail(), "password1").isPresent());
    }

    /**
     * Test the login of a third party customer when passwords don't match
     */
    @Test
    public void testTpLoginWhenPasswordNotMatching() {
        when(thirdPartyAccountManagerService.getThirdPartyByEmail(thirdPartyCustomer.getEmail())).thenReturn(thirdPartyCustomer);
        when(passwordEncoder.matches("password1", thirdPartyCustomer.getPassword())).thenReturn(false);

        assertTrue(!authenticationService.thirdPartyLogin(thirdPartyCustomer.getEmail(), "password1").isPresent());
    }

    /**
     * Test the retrieval of a customer by its token
     */
    @Test
    public void testGetTpByToken() {
        when(thirdPartyAccountManagerService.getThirdPartyByEmail(thirdPartyCustomer.getEmail())).thenReturn(thirdPartyCustomer);
        when(passwordEncoder.matches("password1", thirdPartyCustomer.getPassword())).thenReturn(true);

        String token = authenticationService.thirdPartyLogin(thirdPartyCustomer.getEmail(), "password1").get();
        assertEquals(thirdPartyCustomer, authenticationService.findThirdPartyByToken(token).get());
    }

    /**
     * Test the retrieval of a customer by its token when he is not logged
     */
    @Test
    public void testGetTpByTokenWhenNotPresent() {
        assertTrue(!authenticationService.findThirdPartyByToken("fakeToken").isPresent());
    }

    /**
     * Test the logout of a third party when he is not logged
     */
    @Test(expected = IllegalStateException.class)
    public void tpLogoutTestWhenIllegalAction() {
        authenticationService.thirdPartyLogout(thirdPartyCustomer);
    }

    /**
     * Test the logout of a third party when that he is not logged
     */
    @Test
    public void tpLogoutTest() {
        when(thirdPartyAccountManagerService.getThirdPartyByEmail(thirdPartyCustomer.getEmail())).thenReturn(thirdPartyCustomer);
        when(passwordEncoder.matches("password1", thirdPartyCustomer.getPassword())).thenReturn(true);

        String token = authenticationService.thirdPartyLogin(thirdPartyCustomer.getEmail(), "password1").get();
        authenticationService.thirdPartyLogout(thirdPartyCustomer);
        assertTrue(!authenticationService.findThirdPartyByToken(token).isPresent());
    }

    /**
     * Test the retrieve of detail by token when no user with that token is present
     */
    @Test(expected = UsernameNotFoundException.class)
    public void findDetailByTokenWhenNotPresent() {
        authenticationService.findUserDetailsByToken("fakeToken");
    }

    /**
     * Test the retrieve of detail by token when user with that token is present
     */
    @Test
    public void findDetailByTokenTestWhenUserPresent() {
        when(userAccountManagerService.getUserByUserName("userName1")).thenReturn(user);
        when(passwordEncoder.matches("password1", user.getPassword())).thenReturn(true);

        String token = authenticationService.userLogin(user.getUsername(), "password1").get();
        assertTrue(authenticationService.findUserDetailsByToken(token).getUsername().equals(user.getUsername()));
    }

    /**
     * Test the retrieve of detail by token when customer with that token is present
     */
    @Test
    public void findDetailByTokenTestWhenTpPresent() {
        when(thirdPartyAccountManagerService.getThirdPartyByEmail(thirdPartyCustomer.getEmail())).thenReturn(thirdPartyCustomer);
        when(passwordEncoder.matches("password1", thirdPartyCustomer.getPassword())).thenReturn(true);

        String token = authenticationService.thirdPartyLogin(thirdPartyCustomer.getEmail(), "password1").get();
        assertEquals(thirdPartyCustomer.getEmail(), authenticationService.findUserDetailsByToken(token).getUsername());

    }
}
