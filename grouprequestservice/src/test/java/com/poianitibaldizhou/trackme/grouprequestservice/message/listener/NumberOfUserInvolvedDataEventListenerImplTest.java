package com.poianitibaldizhou.trackme.grouprequestservice.message.listener;

import com.poianitibaldizhou.trackme.grouprequestservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.grouprequestservice.entity.GroupRequest;
import com.poianitibaldizhou.trackme.grouprequestservice.exception.GroupRequestNotFoundException;
import com.poianitibaldizhou.trackme.grouprequestservice.message.protocol.NumberOfUserInvolvedProtocolMessage;
import com.poianitibaldizhou.trackme.grouprequestservice.message.publisher.GroupRequestPublisher;
import com.poianitibaldizhou.trackme.grouprequestservice.repository.FilterStatementRepository;
import com.poianitibaldizhou.trackme.grouprequestservice.repository.GroupRequestRepository;
import com.poianitibaldizhou.trackme.grouprequestservice.service.InternalCommunicationService;
import com.poianitibaldizhou.trackme.grouprequestservice.service.InternalCommunicationServiceImpl;
import com.poianitibaldizhou.trackme.grouprequestservice.util.GroupRequestWrapper;
import com.poianitibaldizhou.trackme.grouprequestservice.util.RequestStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(Enclosed.class)
public class NumberOfUserInvolvedDataEventListenerImplTest {

    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    @DataJpaTest
    @RunWith(SpringRunner.class)
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Sql("classpath:sql/testEventListener.sql")
    public static class IntegrationTestWithoutMessageBroker {

        @Autowired
        private GroupRequestRepository groupRequestRepository;

        @Autowired
        private FilterStatementRepository filterStatementRepository;

        @Mock
        private GroupRequestPublisher groupRequestPublisher;

        private NumberOfUserInvolvedDataEventListener numberOfUserInvolvedDataEventListener;

        private InternalCommunicationServiceImpl internalCommunicationService;

        @Before
        public void setUp() throws Exception {
            MockitoAnnotations.initMocks(this);
            internalCommunicationService = new InternalCommunicationServiceImpl(groupRequestRepository,
                    filterStatementRepository, groupRequestPublisher);
            internalCommunicationService.setMinNumberOfUserInvolved(5);
            numberOfUserInvolvedDataEventListener = new NumberOfUserInvolvedDataEventListenerImpl(internalCommunicationService);
        }

        @After
        public void tearDown() throws Exception {
            numberOfUserInvolvedDataEventListener = null;
            internalCommunicationService = null;
        }

        /**
         * Test onNumberOfUserInvolvedData method when the protocol message is correct and the result should be
         * refused
         *
         * @throws Exception no exception expected
         */
        @Test
        public void onNumberOfUserInvolvedDataGeneratedSuccessfulWithRefusedExpectancy() throws Exception {
            NumberOfUserInvolvedProtocolMessage protocolMessage = new NumberOfUserInvolvedProtocolMessage();
            protocolMessage.setGroupRequestId(2L);
            protocolMessage.setNumberOfUserInvolved(4);
            numberOfUserInvolvedDataEventListener.onNumberOfUserInvolvedDataGenerated(protocolMessage);

            GroupRequest groupRequest = groupRequestRepository.findById(2L).orElseThrow(() -> new GroupRequestNotFoundException(2L));
            assertEquals(RequestStatus.REFUSED, groupRequest.getStatus());
        }

        /**
         * Test onNumberOfUserInvolvedData method when the protocol message is correct and the result should be
         * accepted
         *
         * @throws Exception no exception expected
         */
        @Test
        public void onNumberOfUserInvolvedDataGeneratedSuccessfulWithAcceptedExpectancy() throws Exception {
            NumberOfUserInvolvedProtocolMessage protocolMessage = new NumberOfUserInvolvedProtocolMessage();
            protocolMessage.setGroupRequestId(2L);
            protocolMessage.setNumberOfUserInvolved(5);
            numberOfUserInvolvedDataEventListener.onNumberOfUserInvolvedDataGenerated(protocolMessage);

            GroupRequest groupRequest = groupRequestRepository.findById(2L).orElseThrow(() -> new GroupRequestNotFoundException(2L));
            assertEquals(RequestStatus.ACCEPTED, groupRequest.getStatus());
            List<FilterStatement> filterStatementList = filterStatementRepository.findAllByGroupRequest_Id(2L);
            GroupRequestWrapper groupRequestWrapper = new GroupRequestWrapper(groupRequest, filterStatementList);
            verify(groupRequestPublisher, times(1)).publishGroupRequest(groupRequestWrapper);
        }

        /**
         * Test onNumberOfUserInvolvedData method when the protocol message is incorrect (no number of user involved
         * data)
         *
         * @throws Exception no exception expected
         */
        @Test
        public void onNumberOfUserInvolvedDataGeneratedFailureDueToNoNumberOfUserInvolved() throws Exception {
            NumberOfUserInvolvedProtocolMessage protocolMessage = new NumberOfUserInvolvedProtocolMessage();
            protocolMessage.setGroupRequestId(2L);
            numberOfUserInvolvedDataEventListener.onNumberOfUserInvolvedDataGenerated(protocolMessage);

            GroupRequest groupRequest = groupRequestRepository.findById(2L).orElseThrow(() -> new GroupRequestNotFoundException(2L));
            assertEquals(RequestStatus.UNDER_ANALYSIS, groupRequest.getStatus());
        }

        /**
         * Test onNumberOfUserInvolvedData method when the protocol message is incorrect (group request not existing)
         *
         * @throws Exception no exception expected
         */
        @Test(expected = GroupRequestNotFoundException.class)
        public void onNumberOfUserInvolvedDataGeneratedFailureDueToNotExistingGroupRequest() throws Exception {
            NumberOfUserInvolvedProtocolMessage protocolMessage = new NumberOfUserInvolvedProtocolMessage();
            protocolMessage.setGroupRequestId(4L);
            protocolMessage.setNumberOfUserInvolved(10);
            numberOfUserInvolvedDataEventListener.onNumberOfUserInvolvedDataGenerated(protocolMessage);

            GroupRequest groupRequest = groupRequestRepository.findById(4L).orElseThrow(() -> new GroupRequestNotFoundException(4L));
        }

        /**
         * Test onNumberOfUserInvolvedData method when the protocol message is incorrect (group request not under analysis)
         *
         * @throws Exception no exception expected
         */
        @Test
        public void onNumberOfUserInvolvedDataGeneratedFailureDueToGroupRequestNotUnderAnalysis() throws Exception {
            NumberOfUserInvolvedProtocolMessage protocolMessage = new NumberOfUserInvolvedProtocolMessage();
            protocolMessage.setGroupRequestId(1L);
            protocolMessage.setNumberOfUserInvolved(10);
            numberOfUserInvolvedDataEventListener.onNumberOfUserInvolvedDataGenerated(protocolMessage);

            GroupRequest groupRequest = groupRequestRepository.findById(1L).orElseThrow(() -> new GroupRequestNotFoundException(1L));
            assertEquals(RequestStatus.ACCEPTED, groupRequest.getStatus());
        }
    }

    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    @SpringBootTest
    @ActiveProfiles(value = {"usage-message-broker"})
    @RunWith(SpringRunner.class)
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Sql("classpath:sql/testEventListener.sql")
    public static class IntegrationTestWithMessageBroker{

        @Autowired
        private RabbitTemplate rabbitTemplate;

        @Autowired
        private TopicExchange numberOfUserInvolvedExchange;

        @Autowired
        private Queue numberOfUserInvolvedGeneratedToGroupRequestServiceQueue;

        @Autowired
        @SpyBean
        private NumberOfUserInvolvedDataEventListener numberOfUserInvolvedDataEventListener;

        private RabbitAdmin rabbitAdmin;

        @Before
        public void setUp() throws Exception {
            rabbitAdmin = new RabbitAdmin(rabbitTemplate);
            rabbitAdmin.purgeQueue(numberOfUserInvolvedGeneratedToGroupRequestServiceQueue.getName());
        }

        @After
        public void tearDown() throws Exception {
            rabbitAdmin.purgeQueue(numberOfUserInvolvedGeneratedToGroupRequestServiceQueue.getName());
            rabbitAdmin = null;
        }

        /**
         * Test onNumberOfUserInvolvedData called after a correct message sent through message broker
         *
         * @throws Exception no exception expected
         */
        @Test
        public void onNumberOfUserInvolvedDataGeneratedSuccessful() throws Exception{
            NumberOfUserInvolvedProtocolMessage protocolMessage = new NumberOfUserInvolvedProtocolMessage();
            protocolMessage.setGroupRequestId(2L);
            protocolMessage.setNumberOfUserInvolved(4);
            rabbitTemplate.convertAndSend(numberOfUserInvolvedExchange.getName(), "number-of-user.event.generated", protocolMessage);

            verify(numberOfUserInvolvedDataEventListener, timeout(2000).times(1)).onNumberOfUserInvolvedDataGenerated(protocolMessage);
        }

        /**
         * Test onNumberOfUserInvolvedData not called after a incorrect message due to wrong exchange name
         *
         * @throws Exception no exception expected
         */
        @Test
        public void onNumberOfUserInvolvedDataGeneratedWrongExchange() throws Exception{
            NumberOfUserInvolvedProtocolMessage protocolMessage = new NumberOfUserInvolvedProtocolMessage();
            protocolMessage.setGroupRequestId(2L);
            protocolMessage.setNumberOfUserInvolved(4);
            rabbitTemplate.convertAndSend("wrongExchange", "number-of-user.event.generated", protocolMessage);

            verify(numberOfUserInvolvedDataEventListener, timeout(2000).times(0))
                    .onNumberOfUserInvolvedDataGenerated(any(NumberOfUserInvolvedProtocolMessage.class));
        }

        /**
         * Test onNumberOfUserInvolvedData not called after a incorrect message due to wrong routing key
         *
         * @throws Exception no exception expected
         */
        @Test
        public void onNumberOfUserInvolvedDataGeneratedWrongRoutingKey() throws Exception{
            NumberOfUserInvolvedProtocolMessage protocolMessage = new NumberOfUserInvolvedProtocolMessage();
            protocolMessage.setGroupRequestId(2L);
            protocolMessage.setNumberOfUserInvolved(4);
            rabbitTemplate.convertAndSend(numberOfUserInvolvedExchange.getName(), "number-of-user.event.created", protocolMessage);

            verify(numberOfUserInvolvedDataEventListener, timeout(2000).times(0))
                    .onNumberOfUserInvolvedDataGenerated(any(NumberOfUserInvolvedProtocolMessage.class));
        }

    }


}