package com.poianitibaldizhou.trackme.individualrequestservice.message.listener;

import com.poianitibaldizhou.trackme.individualrequestservice.message.protocol.UserProtocolMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@RunWith(Enclosed.class)
public class UserEventListenerImplTest {

    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    @SpringBootTest
    @RunWith(SpringRunner.class)
    @ActiveProfiles("usage-message-broker")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Sql("classpath:sql/testEventListener.sql")
    public static class IntegrationTestWithMessageBroker{

        @Autowired
        @SpyBean
        private UserEventListener userEventListener;

        @Autowired
        private RabbitTemplate rabbitTemplate;

        @Autowired
        private TopicExchange userExchange;

        @Autowired
        private Queue userCreatedToIndividualRequestServiceQueue;

        private RabbitAdmin rabbitAdmin;

        @Before
        public void setUp() throws Exception {
            rabbitAdmin = new RabbitAdmin(rabbitTemplate);
            rabbitAdmin.purgeQueue(userCreatedToIndividualRequestServiceQueue.getName());
        }

        @After
        public void tearDown() throws Exception {
            rabbitAdmin = null;
        }

        /**
         * Test onUserCreated method when all the name of exchange and routing key correct
         *
         * @throws Exception no exception expected
         */
        @Test
        public void onUserCreatedSuccessful() throws Exception {
            UserProtocolMessage userProtocolMessage = new UserProtocolMessage();
            userProtocolMessage.setSsn("user10");
            userProtocolMessage.setFirstName("firstName");
            userProtocolMessage.setLastName("lastName");
            userProtocolMessage.setBirthDate(new Date(0));
            userProtocolMessage.setBirthCity("birthCity");
            userProtocolMessage.setBirthNation("birthNation");

            rabbitTemplate.convertAndSend(userExchange.getName(), "user.event.created", userProtocolMessage);

            verify(userEventListener, timeout(5000).times(1)).onUserCreated(userProtocolMessage);
        }

        /**
         * Test onUserCreated method when the name of the exchange is incorrect
         *
         * @throws Exception no exception expected
         */
        @Test
        public void onUserCreatedWrongExchange() throws Exception {
            UserProtocolMessage userProtocolMessage = new UserProtocolMessage();
            userProtocolMessage.setSsn("user10");
            userProtocolMessage.setFirstName("firstName");
            userProtocolMessage.setLastName("lastName");
            userProtocolMessage.setBirthDate(new Date(0));
            userProtocolMessage.setBirthCity("birthCity");
            userProtocolMessage.setBirthNation("birthNation");

            rabbitTemplate.convertAndSend("wrongExchange", "user.event.created", userProtocolMessage);

            verify(userEventListener, timeout(5000).times(0)).onUserCreated(any(UserProtocolMessage.class));
        }

        /**
         * Test onUserCreated method when the routing key is incorrect
         *
         * @throws Exception no exception expected
         */
        @Test
        public void onUserCreatedWrongRoutingKey() throws Exception {
            UserProtocolMessage userProtocolMessage = new UserProtocolMessage();
            userProtocolMessage.setSsn("user10");
            userProtocolMessage.setFirstName("firstName");
            userProtocolMessage.setLastName("lastName");
            userProtocolMessage.setBirthDate(new Date(0));
            userProtocolMessage.setBirthCity("birthCity");
            userProtocolMessage.setBirthNation("birthNation");

            rabbitTemplate.convertAndSend(userExchange.getName(), "user.event.updated", userProtocolMessage);

            verify(userEventListener, timeout(5000).times(0)).onUserCreated(userProtocolMessage);
        }



    }


}