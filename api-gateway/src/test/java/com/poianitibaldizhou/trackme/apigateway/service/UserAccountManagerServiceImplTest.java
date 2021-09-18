package com.poianitibaldizhou.trackme.apigateway.service;

import com.poianitibaldizhou.trackme.apigateway.entity.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("usage-message-broker")
public class UserAccountManagerServiceImplTest {

    @Autowired
    private UserAccountManagerService userAccountManagerService;

    @Autowired
    @SpyBean
    private InternalCommunicationService internalCommunicationService;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void registerUser() throws Exception {
        User user = new User();
        user.setUsername("username1");
        user.setPassword("password1");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setSsn("ssn");
        user.setBirthCity("birthCity");
        user.setBirthNation("birthNation");
        user.setBirthDate(new Date(0));
        userAccountManagerService.registerUser(user);

        verify(internalCommunicationService, times(1)).broadcastUserMessage(user);
    }

}