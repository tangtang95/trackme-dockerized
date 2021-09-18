package com.poianitibaldizhou.trackme.individualrequestservice.service;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.User;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.RequestNotFoundException;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.UserNotFoundException;
import com.poianitibaldizhou.trackme.individualrequestservice.message.protocol.UserProtocolMessage;
import com.poianitibaldizhou.trackme.individualrequestservice.message.publisher.IndividualRequestPublisher;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.IndividualRequestRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.ThirdPartyRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("classpath:sql/testEventListener.sql")
public class InternalCommunicationServiceImplTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private IndividualRequestRepository individualRequestRepository;

    @Mock
    private IndividualRequestPublisher individualRequestPublisher;

    private InternalCommunicationService internalCommunicationService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        internalCommunicationService = new InternalCommunicationServiceImpl(userRepository, thirdPartyRepository, individualRequestPublisher);
    }

    @After
    public void tearDown() throws Exception {
        internalCommunicationService = null;
    }

    /**
     * Test handleUserCreated method when the message is correct
     *
     * @throws Exception no exception expected
     */
    @Test
    public void handleUserCreatedSuccessful() throws Exception {
        UserProtocolMessage userProtocolMessage = new UserProtocolMessage();
        userProtocolMessage.setSsn("user10");
        userProtocolMessage.setFirstName("firstName");
        userProtocolMessage.setLastName("lastName");
        userProtocolMessage.setBirthDate(new Date(0));
        userProtocolMessage.setBirthCity("birthCity");
        userProtocolMessage.setBirthNation("birthNation");
        internalCommunicationService.handleUserCreated(userProtocolMessage);

        User user = userRepository.findById("user10").orElseThrow(() -> new UserNotFoundException(new User("user10")));
        assertEquals("user10", user.getSsn());
    }

    /**
     * Test handleUserCreated method when the message is incorrect due to null value in some attribute of protocol
     * message
     *
     * @throws Exception no exception expected
     */
    @Test
    public void handleUserCreatedFailDueToNullValue() throws Exception {
        UserProtocolMessage userProtocolMessage = new UserProtocolMessage();
        userProtocolMessage.setSsn("user10");
        userProtocolMessage.setFirstName("firstName");
        userProtocolMessage.setLastName("lastName");
        userProtocolMessage.setBirthDate(new Date(0));
        internalCommunicationService.handleUserCreated(userProtocolMessage);

        assertFalse(userRepository.existsById("user10"));
    }

    /**
     * Test handleUserCreated when a user ssn already exists
     *
     * @throws Exception no exception expected
     */
    @Test
    public void handleUserCreatedAlreadyExistingUser() throws Exception {
        UserProtocolMessage userProtocolMessage = new UserProtocolMessage();
        userProtocolMessage.setSsn("user1");
        userProtocolMessage.setFirstName("firstName");
        userProtocolMessage.setLastName("lastName");
        userProtocolMessage.setBirthDate(new Date(0));
        userProtocolMessage.setBirthCity("birthCity");
        userProtocolMessage.setBirthNation("birthNation");
        internalCommunicationService.handleUserCreated(userProtocolMessage);

        User user = userRepository.findById("user1").orElseThrow(() -> new UserNotFoundException(new User("user1")));
        assertEquals("user1", user.getSsn());
    }

    /**
     * Test sendIndividualRequest method with generic individual request
     *
     * @throws Exception no exception expected
     */
    @Test
    public void sendIndividualRequestSuccessful() throws Exception {
        IndividualRequest individualRequest = individualRequestRepository.findById(1L)
                .orElseThrow(() -> new RequestNotFoundException(1L));
        internalCommunicationService.sendIndividualRequest(individualRequest);

        verify(individualRequestPublisher, times(1)).publishIndividualRequest(individualRequest);
    }

}