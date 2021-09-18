package com.poianitibaldizhou.trackme.apigateway.message.publisher;

import com.poianitibaldizhou.trackme.apigateway.entity.CompanyDetail;
import com.poianitibaldizhou.trackme.apigateway.entity.PrivateThirdPartyDetail;
import com.poianitibaldizhou.trackme.apigateway.entity.ThirdPartyCustomer;
import com.poianitibaldizhou.trackme.apigateway.exception.ThirdPartyCustomerNotFoundException;
import com.poianitibaldizhou.trackme.apigateway.message.protocol.ThirdPartyProtocolMessage;
import com.poianitibaldizhou.trackme.apigateway.repository.CompanyDetailRepository;
import com.poianitibaldizhou.trackme.apigateway.repository.PrivateThirdPartyDetailRepository;
import com.poianitibaldizhou.trackme.apigateway.repository.ThirdPartyRepository;
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

import static org.junit.Assert.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(value = {"usage-message-broker"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("classpath:sql/testRepository.sql")
public class ThirdPartyPublisherImplTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue thirdPartyCreatedToIndividualRequestServiceQueue;

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private CompanyDetailRepository companyDetailRepository;

    @Autowired
    private PrivateThirdPartyDetailRepository privateThirdPartyDetailRepository;

    private RabbitAdmin rabbitAdmin;

    @Autowired
    private ThirdPartyPublisher thirdPartyPublisher;

    @Before
    public void setUp() throws Exception {
        rabbitAdmin = new RabbitAdmin(rabbitTemplate);
        rabbitAdmin.purgeQueue(thirdPartyCreatedToIndividualRequestServiceQueue.getName());
    }

    @After
    public void tearDown() throws Exception {
        rabbitAdmin.purgeQueue(thirdPartyCreatedToIndividualRequestServiceQueue.getName());
        rabbitAdmin = null;
    }

    @Test
    public void publishPrivateThirdPartyCreated() throws Exception {
        ThirdPartyCustomer thirdPartyCustomer = thirdPartyRepository.findByEmail("tp3@provider.com")
                .orElseThrow(() -> new ThirdPartyCustomerNotFoundException("tp3@provider.com"));
        PrivateThirdPartyDetail privateDetail = privateThirdPartyDetailRepository.findByThirdPartyCustomer(thirdPartyCustomer)
                .orElseThrow(Exception::new);

        thirdPartyPublisher.publishPrivateThirdPartyCreated(privateDetail);
        ThirdPartyProtocolMessage protocolMessage = (ThirdPartyProtocolMessage) rabbitTemplate
                .receiveAndConvert(thirdPartyCreatedToIndividualRequestServiceQueue.getName(), 2000);

        assertNotNull(protocolMessage);
        assertFalse(ThirdPartyProtocolMessage.validateCompanyDetailMessage(protocolMessage));
        assertTrue(ThirdPartyProtocolMessage.validatePrivateDetailMessage(protocolMessage));
        assertEquals(privateDetail.getThirdPartyCustomer().getId(), protocolMessage.getId());
        assertEquals(privateDetail.getSsn(), protocolMessage.getSsn());
        assertEquals(privateDetail.getName(), protocolMessage.getName());
        assertEquals(privateDetail.getSurname(), protocolMessage.getSurname());
        assertEquals(privateDetail.getBirthDate(), protocolMessage.getBirthDate());
        assertEquals(privateDetail.getBirthCity(), protocolMessage.getBirthCity());
    }

    @Test
    public void publishCompanyThirdPartyCreated() throws Exception {
        ThirdPartyCustomer thirdPartyCustomer = thirdPartyRepository.findByEmail("tp1@provider.com")
                .orElseThrow(() -> new ThirdPartyCustomerNotFoundException("tp1@provider.com"));
        CompanyDetail companyDetail = companyDetailRepository.findByThirdPartyCustomer(thirdPartyCustomer).orElseThrow(Exception::new);

        thirdPartyPublisher.publishCompanyThirdPartyCreated(companyDetail);
        ThirdPartyProtocolMessage protocolMessage = (ThirdPartyProtocolMessage) rabbitTemplate
                .receiveAndConvert(thirdPartyCreatedToIndividualRequestServiceQueue.getName(), 2000);
        assertNotNull(protocolMessage);
        assertTrue(ThirdPartyProtocolMessage.validateCompanyDetailMessage(protocolMessage));
        assertFalse(ThirdPartyProtocolMessage.validatePrivateDetailMessage(protocolMessage));
        assertEquals(companyDetail.getThirdPartyCustomer().getId(), protocolMessage.getId());
        assertEquals(companyDetail.getCompanyName(), protocolMessage.getCompanyName());
        assertEquals(companyDetail.getAddress(), protocolMessage.getAddress());
        assertEquals(companyDetail.getDunsNumber(), protocolMessage.getDunsNumber());
    }

}