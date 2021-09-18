package com.poianitibaldizhou.trackme.individualrequestservice.message.listener;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.ThirdParty;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.ThirdPartyNotFoundException;
import com.poianitibaldizhou.trackme.individualrequestservice.message.protocol.ThirdPartyProtocolMessage;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.ThirdPartyRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.service.InternalCommunicationService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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

import static org.junit.Assert.assertEquals;

@RunWith(Enclosed.class)
public class ThirdPartyEventListenerImplTest {

    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    @SpringBootTest
    @RunWith(SpringRunner.class)
    @ActiveProfiles("usage-message-broker")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public static class IntegrationTestWithoutMessageBroker {

        @Autowired
        private InternalCommunicationService internalCommunicationService;

        private ThirdPartyEventListener thirdPartyEventListener;

        @Autowired
        private ThirdPartyRepository thirdPartyRepository;

        @Before
        public void setUp() throws Exception {
            MockitoAnnotations.initMocks(internalCommunicationService);
            thirdPartyEventListener = new ThirdPartyEventListenerImpl(internalCommunicationService);
        }

        @After
        public void tearDown() throws Exception {
            thirdPartyEventListener = null;
            internalCommunicationService = null;
        }

        /**
         * Test onThirdPartyCreated with a correct company third party
         *
         * @throws Exception no exception expected
         */
        @Test
        public void onThirdPartyCreatedCorrectCompany() throws Exception{
            ThirdPartyProtocolMessage thirdPartyProtocolMessage = new ThirdPartyProtocolMessage();
            thirdPartyProtocolMessage.setId(1L);
            thirdPartyProtocolMessage.setCompanyName("companyName");
            thirdPartyProtocolMessage.setAddress("address");
            thirdPartyProtocolMessage.setDunsNumber("dunsNumber");

            thirdPartyEventListener.onThirdPartyCreated(thirdPartyProtocolMessage);

            ThirdParty thirdParty = thirdPartyRepository.findById(1L).orElseThrow(() -> new ThirdPartyNotFoundException(1L));
            assertEquals(thirdPartyProtocolMessage.getId(), thirdParty.getId());
            assertEquals(thirdPartyProtocolMessage.getCompanyName(), thirdParty.getIdentifierName());
        }

        /**
         * Test onThirdPartyCreated with a correct private third party
         *
         * @throws Exception no exception expected
         */
        @Test
        public void onThirdPartyCreatedCorrectPrivate() throws Exception{
            ThirdPartyProtocolMessage thirdPartyProtocolMessage = new ThirdPartyProtocolMessage();
            thirdPartyProtocolMessage.setId(1L);
            thirdPartyProtocolMessage.setSsn("ssn");
            thirdPartyProtocolMessage.setName("name");
            thirdPartyProtocolMessage.setSurname("surname");
            thirdPartyProtocolMessage.setBirthCity("birthCity");
            thirdPartyProtocolMessage.setBirthDate(new Date(0));

            thirdPartyEventListener.onThirdPartyCreated(thirdPartyProtocolMessage);

            ThirdParty thirdParty = thirdPartyRepository.findById(1L).orElseThrow(() -> new ThirdPartyNotFoundException(1L));
            assertEquals(thirdPartyProtocolMessage.getId(), thirdParty.getId());
            assertEquals(thirdPartyProtocolMessage.getName() + " " + thirdPartyProtocolMessage.getSurname(),
                    thirdParty.getIdentifierName());
        }

        /**
         * Test onThirdPartyCreated with an incomplete private third party
         *
         * @throws Exception no exception expected
         */
        @Test(expected = ThirdPartyNotFoundException.class)
        public void onThirdPartyCreatedIncompletePrivate() throws Exception{
            ThirdPartyProtocolMessage thirdPartyProtocolMessage = new ThirdPartyProtocolMessage();
            thirdPartyProtocolMessage.setId(1L);
            thirdPartyProtocolMessage.setSsn("ssn");
            thirdPartyProtocolMessage.setSurname("surname");
            thirdPartyProtocolMessage.setBirthCity("birthCity");
            thirdPartyProtocolMessage.setBirthDate(new Date(0));

            thirdPartyEventListener.onThirdPartyCreated(thirdPartyProtocolMessage);

            ThirdParty thirdParty = thirdPartyRepository.findById(1L).orElseThrow(() -> new ThirdPartyNotFoundException(1L));
        }

        /**
         * Test onThirdPartyCreated with an incomplete company third party
         *
         * @throws Exception no exception expected
         */
        @Test(expected = ThirdPartyNotFoundException.class)
        public void onThirdPartyCreatedIncompleteCompany() throws Exception{
            ThirdPartyProtocolMessage thirdPartyProtocolMessage = new ThirdPartyProtocolMessage();
            thirdPartyProtocolMessage.setId(1L);

            thirdPartyProtocolMessage.setAddress("address");
            thirdPartyProtocolMessage.setDunsNumber("dunsNumber");

            thirdPartyEventListener.onThirdPartyCreated(thirdPartyProtocolMessage);

            ThirdParty thirdParty = thirdPartyRepository.findById(1L).orElseThrow(() -> new ThirdPartyNotFoundException(1L));
        }

        /**
         * Test onThirdPartyCreated with all fields not null (impossible to have both company and private)
         *
         * @throws Exception no exception expected
         */
        @Test(expected = ThirdPartyNotFoundException.class)
        public void onThirdPartyCreatedAllFieldsNotNull() throws Exception{
            ThirdPartyProtocolMessage thirdPartyProtocolMessage = new ThirdPartyProtocolMessage();
            thirdPartyProtocolMessage.setId(1L);
            thirdPartyProtocolMessage.setCompanyName("companyName");
            thirdPartyProtocolMessage.setAddress("address");
            thirdPartyProtocolMessage.setDunsNumber("dunsNumber");
            thirdPartyProtocolMessage.setSsn("ssn");
            thirdPartyProtocolMessage.setName("name");
            thirdPartyProtocolMessage.setSurname("surname");
            thirdPartyProtocolMessage.setBirthCity("birthCity");
            thirdPartyProtocolMessage.setBirthDate(new Date(0));

            thirdPartyEventListener.onThirdPartyCreated(thirdPartyProtocolMessage);

            ThirdParty thirdParty = thirdPartyRepository.findById(1L).orElseThrow(() -> new ThirdPartyNotFoundException(1L));
        }

    }

    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    @SpringBootTest
    @RunWith(SpringRunner.class)
    @ActiveProfiles("usage-message-broker")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Sql("classpath:sql/testEventListener.sql")
    public static class IntegrationTestWithMessageBroker {

        @Autowired
        @SpyBean
        private ThirdPartyEventListener thirdPartyEventListener;

        @Autowired
        private TopicExchange thirdPartyExchange;

        @Autowired
        private Queue thirdPartyCreatedToIndividualRequestServiceQueue;

        @Autowired
        private RabbitTemplate rabbitTemplate;

        private RabbitAdmin rabbitAdmin;

        @Before
        public void setUp() throws Exception {
            rabbitAdmin = new RabbitAdmin(rabbitTemplate);
            rabbitAdmin.purgeQueue(thirdPartyCreatedToIndividualRequestServiceQueue.getName());
        }

        @After
        public void tearDown() throws Exception {
            rabbitAdmin = null;
        }

        /**
         * Test onThirdPartyCreated successful when exchange name and routing key correct
         *
         * @throws Exception no exception expected
         */
        @Test
        public void onThirdPartyCreatedSuccessful() throws Exception {
            ThirdPartyProtocolMessage thirdPartyProtocolMessage = new ThirdPartyProtocolMessage();
            thirdPartyProtocolMessage.setId(1L);
            thirdPartyProtocolMessage.setCompanyName("companyName");
            thirdPartyProtocolMessage.setAddress("address");
            thirdPartyProtocolMessage.setDunsNumber("dunsNumber");
            rabbitTemplate.convertAndSend(thirdPartyExchange.getName(), "third-party.event.created", thirdPartyProtocolMessage);

            Mockito.verify(thirdPartyEventListener, Mockito.timeout(2000).times(1))
                    .onThirdPartyCreated(thirdPartyProtocolMessage);
        }

        /**
         * Test onThirdPartyCreated when the name of the exchange is wrong
         *
         * @throws Exception no exception expected
         */
        @Test
        public void onThirdPartyCreatedWrongExchange() throws Exception{
            ThirdPartyProtocolMessage thirdPartyProtocolMessage = new ThirdPartyProtocolMessage();
            thirdPartyProtocolMessage.setId(1L);
            thirdPartyProtocolMessage.setCompanyName("companyName");
            thirdPartyProtocolMessage.setAddress("address");
            thirdPartyProtocolMessage.setDunsNumber("dunsNumber");
            rabbitTemplate.convertAndSend("wrongExchange", "third-party.event.created", thirdPartyProtocolMessage);

            Mockito.verify(thirdPartyEventListener, Mockito.timeout(2000).times(0))
                    .onThirdPartyCreated(thirdPartyProtocolMessage);
        }

        /**
         * Test onThirdPartyCreated when the routing key is wrong
         *
         * @throws Exception no exception expected
         */
        @Test
        public void onThirdPartyCreated() throws Exception{
            ThirdPartyProtocolMessage thirdPartyProtocolMessage = new ThirdPartyProtocolMessage();
            thirdPartyProtocolMessage.setId(1L);
            thirdPartyProtocolMessage.setCompanyName("companyName");
            thirdPartyProtocolMessage.setAddress("address");
            thirdPartyProtocolMessage.setDunsNumber("dunsNumber");
            rabbitTemplate.convertAndSend(thirdPartyExchange.getName(), "third-party.event.generated", thirdPartyProtocolMessage);

            Mockito.verify(thirdPartyEventListener, Mockito.timeout(2000).times(0))
                    .onThirdPartyCreated(thirdPartyProtocolMessage);
        }

    }

}
