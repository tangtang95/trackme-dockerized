package com.poianitibaldizhou.trackme.individualrequestservice.message.publisher;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.RequestNotFoundException;
import com.poianitibaldizhou.trackme.individualrequestservice.message.protocol.IndividualRequestProtocolMessage;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.IndividualRequestRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestStatus;
import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestStatusUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("usage-message-broker")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("classpath:sql/testEventListener.sql")
public class IndividualRequestPublisherImplIntegrationTest {

    @Autowired
    private IndividualRequestPublisher individualRequestPublisher;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IndividualRequestRepository individualRequestRepository;

    @Autowired
    private Queue individualRequestAcceptedToShareDataServiceQueue;

    private RabbitAdmin rabbitAdmin;

    @Before
    public void setUp() throws Exception {
        rabbitAdmin = new RabbitAdmin(rabbitTemplate);
        rabbitAdmin.purgeQueue(individualRequestAcceptedToShareDataServiceQueue.getName());
    }

    @After
    public void tearDown() throws Exception {
        rabbitAdmin.purgeQueue(individualRequestAcceptedToShareDataServiceQueue.getName());
        rabbitAdmin = null;
    }

    /**
     * Test publishIndividualRequest method when the individual request is accepted
     *
     * @throws Exception no exception expected
     */
    @Test
    public void publishIndividualRequestWhenItIsAccepted() throws Exception {
        IndividualRequest individualRequest = individualRequestRepository.findById(1L)
                .orElseThrow(() -> new RequestNotFoundException(1L));
        individualRequest.setStatus(IndividualRequestStatus.ACCEPTED);
        individualRequestPublisher.publishIndividualRequest(individualRequest);

        IndividualRequestProtocolMessage protocolMessage = (IndividualRequestProtocolMessage) rabbitTemplate
                .receiveAndConvert(individualRequestAcceptedToShareDataServiceQueue.getName(), 2000);

        assertEquals(individualRequest.getId(), protocolMessage.getId());
        assertEquals(IndividualRequestStatusUtils.getIndividualRequestStatusOfProtocol(individualRequest.getStatus()),
                protocolMessage.getStatus());
        assertEquals(individualRequest.getThirdParty().getId(), protocolMessage.getThirdPartyId());
        assertEquals(individualRequest.getThirdParty().getIdentifierName(), protocolMessage.getThirdPartyName());
        assertEquals(individualRequest.getTimestamp(), protocolMessage.getCreationTimestamp());
        assertEquals(individualRequest.getStartDate(), protocolMessage.getStartDate());
        assertEquals(individualRequest.getEndDate(), protocolMessage.getEndDate());
        assertEquals(individualRequest.getMotivation(), protocolMessage.getMotivationText());
        assertEquals(individualRequest.getUser().getSsn(), protocolMessage.getUserSsn());
    }

    /**
     * Test publishIndividualRequest method when the individual request is refused
     *
     * @throws Exception no exception expected
     */
    @Test
    public void publishIndividualRequestWhenItIsRefused() throws Exception {
        IndividualRequest individualRequest = individualRequestRepository.findById(1L)
                .orElseThrow(() -> new RequestNotFoundException(1L));
        individualRequest.setStatus(IndividualRequestStatus.REFUSED);
        individualRequestPublisher.publishIndividualRequest(individualRequest);

        IndividualRequestProtocolMessage protocolMessage = (IndividualRequestProtocolMessage) rabbitTemplate
                .receiveAndConvert(individualRequestAcceptedToShareDataServiceQueue.getName(), 2000);
        assertNull(protocolMessage);
    }

    /**
     * Test publishIndividualRequest method when the individual request is pending
     *
     * @throws Exception no exception expected
     */
    @Test
    public void publishIndividualRequestWhenItIsPending() throws Exception {
        IndividualRequest individualRequest = individualRequestRepository.findById(1L)
                .orElseThrow(() -> new RequestNotFoundException(1L));
        individualRequest.setStatus(IndividualRequestStatus.PENDING);
        individualRequestPublisher.publishIndividualRequest(individualRequest);

        IndividualRequestProtocolMessage protocolMessage = (IndividualRequestProtocolMessage) rabbitTemplate
                .receiveAndConvert(individualRequestAcceptedToShareDataServiceQueue.getName(), 2000);
        assertNull(protocolMessage);
    }

}