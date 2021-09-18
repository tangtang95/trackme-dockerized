package com.poianitibaldizhou.trackme.apigateway.message.publisher;

import com.poianitibaldizhou.trackme.apigateway.entity.User;
import com.poianitibaldizhou.trackme.apigateway.message.protocol.UserProtocolMessage;
import com.poianitibaldizhou.trackme.apigateway.repository.UserRepository;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(value = {"usage-message-broker"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("classpath:sql/testRepository.sql")
public class UserPublisherImplTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue userCreatedToShareDataServiceQueue;

    @Autowired
    private UserRepository userRepository;

    private RabbitAdmin rabbitAdmin;

    @Autowired
    private UserPublisher userPublisher;

    @Before
    public void setUp()  {
        rabbitAdmin = new RabbitAdmin(rabbitTemplate);
        rabbitAdmin.purgeQueue(userCreatedToShareDataServiceQueue.getName());
    }

    @After
    public void tearDown() {
        rabbitAdmin.purgeQueue(userCreatedToShareDataServiceQueue.getName());
        rabbitAdmin = null;
    }

    @Test
    public void publishUserCreated() {
        User user = userRepository.findByUsername("username1").orElseThrow(() -> new UsernameNotFoundException("username1"));
        userPublisher.publishUserCreated(user);

        UserProtocolMessage userProtocolMessage = (UserProtocolMessage) rabbitTemplate
                .receiveAndConvert(userCreatedToShareDataServiceQueue.getName(), 5000);

        assertNotNull(userProtocolMessage);
        assertEquals(user.getSsn(), userProtocolMessage.getSsn());
        assertEquals(user.getFirstName(), userProtocolMessage.getFirstName());
        assertEquals(user.getLastName(), userProtocolMessage.getLastName());
        assertEquals(user.getBirthDate(), userProtocolMessage.getBirthDate());
        assertEquals(user.getBirthCity(), userProtocolMessage.getBirthCity());
        assertEquals(user.getBirthNation(), userProtocolMessage.getBirthNation());
        assertTrue(UserProtocolMessage.validateMessage(userProtocolMessage));
    }

}