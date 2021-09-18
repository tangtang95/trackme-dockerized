package com.poianitibaldizhou.trackme.apigateway.service;

import com.poianitibaldizhou.trackme.apigateway.entity.User;
import com.poianitibaldizhou.trackme.apigateway.exception.AlreadyPresentSsnException;
import com.poianitibaldizhou.trackme.apigateway.exception.AlreadyPresentUsernameException;
import com.poianitibaldizhou.trackme.apigateway.exception.SsnNotFoundException;
import com.poianitibaldizhou.trackme.apigateway.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Unit test for the implementation of the user account manager service
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class UserAccountManagerServiceImplUnitTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserAccountManagerServiceImpl service;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {
        // Set up the repository
        user1 = new User();
        user1.setSsn("user1");
        user1.setBirthCity("Milano");
        user1.setBirthDate(new Date(0));
        user1.setBirthNation("Italy");
        user1.setFirstName("user1name");
        user1.setLastName("user1lastname");
        user1.setPassword("password1");
        user1.setUsername("username1");

        user2 = new User();
        user2.setSsn("user2");
        user2.setBirthCity("Verona");
        user2.setBirthDate(new Date(0));
        user2.setBirthNation("Italy");
        user2.setFirstName("user2name");
        user2.setLastName("user2lastname");
        user2.setPassword("password2");
        user2.setUsername("username2");

        user3 = new User();
        user3.setSsn("user3");
        user3.setBirthCity("London");
        user3.setBirthDate(new Date(0));
        user3.setBirthNation("UK");
        user3.setFirstName("user3name");
        user3.setLastName("user3lastname");
        user3.setPassword("password3");
        user3.setUsername("username3");

        Mockito.when(userRepository.findById("user1")).thenReturn(java.util.Optional.of(user1));
        Mockito.when(userRepository.findById("user2")).thenReturn(java.util.Optional.of(user2));
        Mockito.when(userRepository.findById("user3")).thenReturn(java.util.Optional.of(user3));

        Mockito.when(userRepository.findByUsername("username1")).thenReturn(java.util.Optional.of(user1));
        Mockito.when(userRepository.findByUsername("username2")).thenReturn(java.util.Optional.of(user2));
        Mockito.when(userRepository.findByUsername("username3")).thenReturn(java.util.Optional.of(user3));

        // Create the service
        service = new UserAccountManagerServiceImpl(userRepository, passwordEncoder);
    }

    @After
    public void tearDown() {
        service = null;
    }

    /**
     * Test the get of a user by his ssn when the user is registered
     */
    @Test
    public void testGetUserBySsnWhenPresent() {
        User user = service.getUserBySsn("user1");
        assertEquals(user, user1);
    }

    /**
     * Test the get of a user by its username when not present
     */
    @Test(expected = UsernameNotFoundException.class)
    public void testGetUserByUsernameWhenNotPresent() {
        service.getUserByUserName("notPresentUsername");
    }

    /**
     * Test the get of a user by his ssn when no user with that ssn is registered
     */
    @Test(expected = SsnNotFoundException.class)
    public void testGetUserBySsnWhenNotPresent() {
        service.getUserBySsn("nonPresentSsn");
    }

    /**
     * Test the registration of a user when no other user with the new ssn and username exists (i.e. the sign up
     * is successful)
     */
    @Test
    public void testRegisterUser() {
        User user = new User();
        user.setSsn("user4");
        user.setUsername("username4");
        user.setPassword("pass4");
        user.setLastName("user4lastname");
        user.setFirstName("user4name");
        user.setBirthDate(new Date(0));
        user.setBirthCity("Venezia");
        user.setBirthNation("Italia");

        Mockito.when(userRepository.saveAndFlush(user)).thenReturn(user);

        service.registerUser(user);

        verify(userRepository, times(1)).saveAndFlush(user);
    }

    /**
     * Test the registration of a user when another user with the specified ssn is already registered
     */
    @Test(expected = AlreadyPresentSsnException.class)
    public void testRegisterUserWhenSsnIsPresent() {
        User user = new User();
        user.setSsn(user3.getSsn());
        user.setUsername("newUserName");
        user.setPassword("password");
        user.setLastName("surname");
        user.setFirstName("name");
        user.setBirthDate(new Date(0));
        user.setBirthCity("Tokyo");
        user.setBirthNation("Japan");

        service.registerUser(user);
    }

    /**
     * Test the registration of a user when another user with the specified username is already registered
     */
    @Test(expected = AlreadyPresentUsernameException.class)
    public void testRegisterUserWhenUsernameIsPresent() {
        User user = new User();
        user.setSsn("notPresentUser");
        user.setUsername(user2.getUsername());
        user.setPassword("password");
        user.setLastName("surname");
        user.setFirstName("name");
        user.setBirthDate(new Date(0));
        user.setBirthCity("Tokyo");
        user.setBirthNation("Japan");

        service.registerUser(user);
    }
}
