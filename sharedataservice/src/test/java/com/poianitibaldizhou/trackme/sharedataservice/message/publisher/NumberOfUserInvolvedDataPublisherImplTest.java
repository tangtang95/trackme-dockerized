package com.poianitibaldizhou.trackme.sharedataservice.message.publisher;

import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.NumberOfUserInvolvedProtocolMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
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

import static org.junit.Assert.*;

/**
 * Integration test with message broker
 */
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(value = {"usage-message-broker", "test"})
public class NumberOfUserInvolvedDataPublisherImplTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue numberOfUserInvolvedGeneratedToGroupRequestServiceQueue;

    @Autowired
    private NumberOfUserInvolvedDataPublisher numberOfUserInvolvedDataPublisher;

    private RabbitAdmin rabbitAdmin;

    @Before
    public void setUp() throws Exception {
        rabbitAdmin = new RabbitAdmin(rabbitTemplate);
        rabbitAdmin.purgeQueue(numberOfUserInvolvedGeneratedToGroupRequestServiceQueue.getName());
    }

    @After
    public void tearDown() throws Exception {
        rabbitAdmin = null;
    }

    /**
     * Test publishNumberOfUserInvolvedData method to check if it send the data in the correct queue
     *
     * @throws Exception no exception expected
     */
    @Test
    public void publishNumberOfUserInvolvedData() throws Exception {
        numberOfUserInvolvedDataPublisher.publishNumberOfUserInvolvedData(1L, 100);
        NumberOfUserInvolvedProtocolMessage protocolMessage = (NumberOfUserInvolvedProtocolMessage) rabbitTemplate.receiveAndConvert(
                numberOfUserInvolvedGeneratedToGroupRequestServiceQueue.getName(), 2000);
        assertTrue(NumberOfUserInvolvedProtocolMessage.validateMessage(protocolMessage));
        assertEquals(new Integer(100), protocolMessage.getNumberOfUserInvolved());
        assertEquals(new Long(1), protocolMessage.getGroupRequestId());
        Integer numberOfMessages = (Integer) rabbitAdmin
                .getQueueProperties(numberOfUserInvolvedGeneratedToGroupRequestServiceQueue.getName())
                .get("QUEUE_MESSAGE_COUNT");
        assertEquals(new Integer(0), numberOfMessages);
    }


}