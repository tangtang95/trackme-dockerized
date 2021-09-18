package com.poianitibaldizhou.trackme.sharedataservice.message.listener;

import com.poianitibaldizhou.trackme.sharedataservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.sharedataservice.entity.User;
import com.poianitibaldizhou.trackme.sharedataservice.exception.IndividualRequestNotFoundException;
import com.poianitibaldizhou.trackme.sharedataservice.exception.UserNotFoundException;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.IndividualRequestProtocolMessage;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.enumerator.IndividualRequestStatusProtocolMessage;
import com.poianitibaldizhou.trackme.sharedataservice.message.publisher.NumberOfUserInvolvedDataPublisher;
import com.poianitibaldizhou.trackme.sharedataservice.repository.FilterStatementRepository;
import com.poianitibaldizhou.trackme.sharedataservice.repository.GroupRequestRepository;
import com.poianitibaldizhou.trackme.sharedataservice.repository.IndividualRequestRepository;
import com.poianitibaldizhou.trackme.sharedataservice.repository.UserRepository;
import com.poianitibaldizhou.trackme.sharedataservice.service.InternalCommunicationService;
import com.poianitibaldizhou.trackme.sharedataservice.service.InternalCommunicationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;


@RunWith(Enclosed.class)
public class IndividualRequestEventListenerImplTest {

    /**
     * Integration test of individual request event listener without the message broker
     */
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    @DataJpaTest
    @ActiveProfiles("test")
    @RunWith(SpringRunner.class)
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Sql("classpath:sql/testEventListener.sql")
    public static class IntegrationTestWithoutMessageBroker {

        @Autowired
        private GroupRequestRepository groupRequestRepository;

        @Autowired
        private FilterStatementRepository filterStatementRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private IndividualRequestRepository individualRequestRepository;

        @Mock
        private NumberOfUserInvolvedDataPublisher numberOfUserInvolvedDataPublisher;

        private InternalCommunicationService internalCommunicationService;

        private IndividualRequestEventListener individualRequestEventListener;

        @Before
        public void setUp() throws Exception {
            internalCommunicationService = new InternalCommunicationServiceImpl(userRepository, filterStatementRepository,
                    groupRequestRepository, individualRequestRepository, numberOfUserInvolvedDataPublisher);
            individualRequestEventListener = new IndividualRequestEventListenerImpl(internalCommunicationService);
        }

        @After
        public void tearDown() throws Exception {
            individualRequestEventListener = null;
        }

        /**
         * Test onIndividualRequestAccepted method when it is successful sent
         *
         * @throws Exception no exception expected
         */
        @Test
        public void onIndividualRequestAcceptedSuccessful() throws Exception {
            IndividualRequestProtocolMessage individualRequestProtocolMessage = new IndividualRequestProtocolMessage();
            individualRequestProtocolMessage.setId(1L);
            individualRequestProtocolMessage.setThirdPartyName("thirdParty1");
            individualRequestProtocolMessage.setCreationTimestamp(new Timestamp(0));
            individualRequestProtocolMessage.setStatus(IndividualRequestStatusProtocolMessage.ACCEPTED);
            individualRequestProtocolMessage.setThirdPartyId(1L);
            individualRequestProtocolMessage.setStartDate(new Date(0));
            individualRequestProtocolMessage.setEndDate(new Date(1));
            individualRequestProtocolMessage.setUserSsn("user1");
            individualRequestProtocolMessage.setMotivationText("motivation");

            User user = userRepository.findById("user1").orElseThrow(() -> new UserNotFoundException("user1"));

            individualRequestEventListener.onIndividualRequestAccepted(individualRequestProtocolMessage);

            IndividualRequest individualRequest = individualRequestRepository.findByIdAndThirdPartyId(1L, 1L)
                    .orElseThrow(() -> new IndividualRequestNotFoundException(1L));

            assertEquals(new Timestamp(0), individualRequest.getCreationTimestamp());
            assertEquals(new Date(0), individualRequest.getStartDate());
            assertEquals(new Date(1), individualRequest.getEndDate());
            assertEquals(user, individualRequest.getUser());

        }

        /**
         * Test onIndividualRequestAccepted when the individual request is different from not accepted
         *
         * @throws Exception no exception expected
         */
        @Test
        public void onIndividualRequestAcceptedWithIndividualRequestNotAccepted() throws Exception {
            IndividualRequestProtocolMessage individualRequestProtocolMessage = new IndividualRequestProtocolMessage();
            individualRequestProtocolMessage.setId(1L);
            individualRequestProtocolMessage.setThirdPartyName("thirdParty1");
            individualRequestProtocolMessage.setCreationTimestamp(new Timestamp(0));
            individualRequestProtocolMessage.setStatus(IndividualRequestStatusProtocolMessage.REFUSED);
            individualRequestProtocolMessage.setThirdPartyId(1L);
            individualRequestProtocolMessage.setStartDate(new Date(0));
            individualRequestProtocolMessage.setEndDate(new Date(1));
            individualRequestProtocolMessage.setUserSsn("user2");
            individualRequestProtocolMessage.setMotivationText("motivation");

            individualRequestEventListener.onIndividualRequestAccepted(individualRequestProtocolMessage);

            assertFalse(individualRequestRepository.existsById(1L));
        }

        /**
         * Test onIndividualRequestAccepted when the individual request has some null attribute
         *
         * @throws Exception no exception expected
         */
        @Test
        public void onIndividualRequestAcceptedWithIncompleteMessage() throws Exception {
            IndividualRequestProtocolMessage individualRequestProtocolMessage = new IndividualRequestProtocolMessage();
            individualRequestProtocolMessage.setId(1L);
            individualRequestProtocolMessage.setStatus(IndividualRequestStatusProtocolMessage.ACCEPTED);
            individualRequestProtocolMessage.setStartDate(new Date(0));
            individualRequestProtocolMessage.setEndDate(new Date(1));
            individualRequestProtocolMessage.setUserSsn("user2");
            individualRequestProtocolMessage.setMotivationText("motivation");

            individualRequestEventListener.onIndividualRequestAccepted(individualRequestProtocolMessage);

            assertFalse(individualRequestRepository.existsById(1L));
        }
    }

    /**
     * Integration test of individual event listener with the message broker (w/o DB)
     */
    @Slf4j
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    @RunWith(SpringRunner.class)
    @SpringBootTest
    @ActiveProfiles(value = {"usage-message-broker", "test"})
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Sql("classpath:sql/testEventListener.sql")
    public static class IntegrationTestWithMessageBroker {

        @Autowired
        private RabbitTemplate rabbitTemplate;

        @Autowired
        private TopicExchange individualRequestExchange;

        @Autowired
        private Queue individualRequestAcceptedToShareDataServiceQueue;

        @Autowired
        @SpyBean
        private IndividualRequestEventListener individualRequestEventListener;

        private RabbitAdmin rabbitAdmin;

        @Before
        public void setUp() throws Exception {
            rabbitAdmin = new RabbitAdmin(rabbitTemplate);
            rabbitAdmin.purgeQueue(individualRequestAcceptedToShareDataServiceQueue.getName());
        }

        @After
        public void tearDown() throws Exception {
            rabbitAdmin = null;
        }


        /**
         * Test to check if the method of onIndividualRequestAccepted will be called after sending the message when the
         * routing key is correct
         *
         * @throws Exception no exception expected
         */
        @Test
        public void onIndividualRequestAcceptedWithCorrectRoutingKey() throws Exception {
            IndividualRequestProtocolMessage individualRequestProtocolMessage = new IndividualRequestProtocolMessage();
            individualRequestProtocolMessage.setId(1L);
            individualRequestProtocolMessage.setThirdPartyName("thirdParty1");
            individualRequestProtocolMessage.setCreationTimestamp(new Timestamp(0));
            individualRequestProtocolMessage.setStatus(IndividualRequestStatusProtocolMessage.ACCEPTED);
            individualRequestProtocolMessage.setThirdPartyId(1L);
            individualRequestProtocolMessage.setStartDate(new Date(0));
            individualRequestProtocolMessage.setEndDate(new Date(1));
            individualRequestProtocolMessage.setUserSsn("user1");
            individualRequestProtocolMessage.setMotivationText("motivation");

            rabbitTemplate.convertAndSend(individualRequestExchange.getName(), "individualrequest.event.accepted",
                    individualRequestProtocolMessage);

            verify(individualRequestEventListener, timeout(5000).times(1))
                    .onIndividualRequestAccepted(individualRequestProtocolMessage);
        }

        /**
         * Test to check if the method of onIndividualRequestAccepted will not be called after sending the message when
         * the routing key is incorrect w.r.t. to the topic exchange and binding
         *
         * @throws Exception no exception expected
         */
        @Test
        public void onIndividualRequestAcceptedWithIncorrectRoutingKey() throws Exception {
            IndividualRequestProtocolMessage individualRequestProtocolMessage = new IndividualRequestProtocolMessage();
            individualRequestProtocolMessage.setId(1L);
            individualRequestProtocolMessage.setThirdPartyName("thirdParty1");
            individualRequestProtocolMessage.setCreationTimestamp(new Timestamp(0));
            individualRequestProtocolMessage.setStatus(IndividualRequestStatusProtocolMessage.ACCEPTED);
            individualRequestProtocolMessage.setThirdPartyId(1L);
            individualRequestProtocolMessage.setStartDate(new Date(0));
            individualRequestProtocolMessage.setEndDate(new Date(1));
            individualRequestProtocolMessage.setUserSsn("user1");
            individualRequestProtocolMessage.setMotivationText("motivation");

            rabbitTemplate.convertAndSend(individualRequestExchange.getName(), "individualrequest.event.refused",
                    individualRequestProtocolMessage);

            verify(individualRequestEventListener, timeout(5000).times(0))
                    .onIndividualRequestAccepted(any(IndividualRequestProtocolMessage.class));
        }

        /**
         * Test to check if the method of onIndividualRequestAccepted will not be called after sending the message when
         * the topic exchange is wrong
         *
         * @throws Exception no exception expected
         */
        @Test
        public void onIndividualRequestAcceptedWithWrongExchange() throws Exception {
            IndividualRequestProtocolMessage individualRequestProtocolMessage = new IndividualRequestProtocolMessage();
            individualRequestProtocolMessage.setId(1L);
            individualRequestProtocolMessage.setThirdPartyName("thirdParty1");
            individualRequestProtocolMessage.setCreationTimestamp(new Timestamp(0));
            individualRequestProtocolMessage.setStatus(IndividualRequestStatusProtocolMessage.ACCEPTED);
            individualRequestProtocolMessage.setThirdPartyId(1L);
            individualRequestProtocolMessage.setStartDate(new Date(0));
            individualRequestProtocolMessage.setEndDate(new Date(1));
            individualRequestProtocolMessage.setUserSsn("user1");
            individualRequestProtocolMessage.setMotivationText("motivation");

            rabbitTemplate.convertAndSend("wrongExchange", "individualrequest.event.accepted",
                    individualRequestProtocolMessage);

            verify(individualRequestEventListener, timeout(5000).times(0))
                    .onIndividualRequestAccepted(any(IndividualRequestProtocolMessage.class));
        }
    }
}