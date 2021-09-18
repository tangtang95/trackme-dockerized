package com.poianitibaldizhou.trackme.grouprequestservice.message.publisher;

import com.poianitibaldizhou.trackme.grouprequestservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.grouprequestservice.entity.GroupRequest;
import com.poianitibaldizhou.trackme.grouprequestservice.exception.GroupRequestNotFoundException;
import com.poianitibaldizhou.trackme.grouprequestservice.message.protocol.FilterStatementProtocolMessage;
import com.poianitibaldizhou.trackme.grouprequestservice.message.protocol.GroupRequestProtocolMessage;
import com.poianitibaldizhou.trackme.grouprequestservice.repository.FilterStatementRepository;
import com.poianitibaldizhou.trackme.grouprequestservice.repository.GroupRequestRepository;
import com.poianitibaldizhou.trackme.grouprequestservice.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(value = {"usage-message-broker"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("classpath:sql/testEventListener.sql")
public class GroupRequestPublisherImplIntegrationTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue groupRequestCreatedToShareDataServiceQueue;

    @Autowired
    private Queue groupRequestAcceptedToShareDataServiceQueue;

    @Autowired
    private GroupRequestPublisher groupRequestPublisher;

    @Autowired
    private GroupRequestRepository groupRequestRepository;

    @Autowired
    private FilterStatementRepository filterStatementRepository;

    private RabbitAdmin rabbitAdmin;

    @Before
    public void setUp() throws Exception {
        rabbitAdmin = new RabbitAdmin(rabbitTemplate);
        rabbitAdmin.purgeQueue(groupRequestAcceptedToShareDataServiceQueue.getName());
        rabbitAdmin.purgeQueue(groupRequestCreatedToShareDataServiceQueue.getName());
    }

    @After
    public void tearDown() throws Exception {
        rabbitAdmin.purgeQueue(groupRequestAcceptedToShareDataServiceQueue.getName());
        rabbitAdmin.purgeQueue(groupRequestCreatedToShareDataServiceQueue.getName());
        rabbitAdmin = null;
    }

    /**
     * Test publish group request method when the group request to send is under analysis
     *
     * @throws Exception no exception expected
     */
    @Test
    public void publishGroupRequestWhenItIsUnderAnalysis() throws Exception {
        GroupRequest groupRequest = groupRequestRepository.findById(2L).orElseThrow(() -> new GroupRequestNotFoundException(2L));
        List<FilterStatement> filterStatementList = filterStatementRepository.findAllByGroupRequest_Id(2L);
        GroupRequestWrapper groupRequestWrapper = new GroupRequestWrapper(groupRequest, filterStatementList);
        groupRequestPublisher.publishGroupRequest(groupRequestWrapper);

        GroupRequestProtocolMessage protocolMessage = (GroupRequestProtocolMessage) rabbitTemplate
                .receiveAndConvert(groupRequestCreatedToShareDataServiceQueue.getName(), 2000);
        assertTrue(GroupRequestProtocolMessage.validateMessage(protocolMessage));

        assertEquals(groupRequest.getId(), protocolMessage.getId());
        assertEquals(AggregatorOperatorUtils.getAggregatorOperatorOfProtocol(groupRequest.getAggregatorOperator()),
                protocolMessage.getAggregatorOperator());
        assertEquals(groupRequest.getThirdPartyId(), protocolMessage.getThirdPartyId());
        assertEquals(groupRequest.getCreationTimestamp(), protocolMessage.getCreationTimestamp());
        assertEquals(RequestTypeUtils.getRequestTypeOfProtocol(groupRequest.getRequestType()), protocolMessage.getRequestType());
        assertEquals(RequestStatusUtils.getGroupRequestStatusOfProtocol(groupRequest.getStatus()), protocolMessage.getStatus());

        List<FilterStatementProtocolMessage> filterStatementProtocolMessageList = filterStatementList.stream().map(filterStatement -> {
            FilterStatementProtocolMessage protocolMessage1 = new FilterStatementProtocolMessage();
            protocolMessage1.setValue(filterStatement.getValue());
            protocolMessage1.setComparisonSymbol(ComparisonSymbolUtils.getComparisonSymbolOfProtocol(filterStatement.getComparisonSymbol()));
            protocolMessage1.setColumn(FieldTypeUtils.getFieldTypeOfProtocol(filterStatement.getColumn()));
            return protocolMessage1;
        }).collect(Collectors.toList());

        assertThat(filterStatementProtocolMessageList, is(protocolMessage.getFilterStatements()));

        GroupRequestProtocolMessage emptyMessage = (GroupRequestProtocolMessage) rabbitTemplate
                .receiveAndConvert(groupRequestAcceptedToShareDataServiceQueue.getName(), 2000);
        assertNull(emptyMessage);
    }

    /**
     * Test publish group request method when the group request to sent is accepted
     *
     * @throws Exception no exception expected
     */
    @Test
    public void publishGroupRequestWhenItIsAccepted() throws Exception {
        GroupRequest groupRequest = groupRequestRepository.findById(1L).orElseThrow(() -> new GroupRequestNotFoundException(1L));
        List<FilterStatement> filterStatementList = filterStatementRepository.findAllByGroupRequest_Id(1L);
        GroupRequestWrapper groupRequestWrapper = new GroupRequestWrapper(groupRequest, filterStatementList);
        groupRequestPublisher.publishGroupRequest(groupRequestWrapper);

        GroupRequestProtocolMessage protocolMessage = (GroupRequestProtocolMessage) rabbitTemplate
                .receiveAndConvert(groupRequestAcceptedToShareDataServiceQueue.getName(), 2000);
        assertTrue(GroupRequestProtocolMessage.validateMessage(protocolMessage));

        assertEquals(groupRequest.getId(), protocolMessage.getId());
        assertEquals(AggregatorOperatorUtils.getAggregatorOperatorOfProtocol(groupRequest.getAggregatorOperator()),
                protocolMessage.getAggregatorOperator());
        assertEquals(groupRequest.getThirdPartyId(), protocolMessage.getThirdPartyId());
        assertEquals(groupRequest.getCreationTimestamp(), protocolMessage.getCreationTimestamp());
        assertEquals(RequestTypeUtils.getRequestTypeOfProtocol(groupRequest.getRequestType()), protocolMessage.getRequestType());
        assertEquals(RequestStatusUtils.getGroupRequestStatusOfProtocol(groupRequest.getStatus()), protocolMessage.getStatus());

        List<FilterStatementProtocolMessage> filterStatementProtocolMessageList = filterStatementList.stream().map(filterStatement -> {
            FilterStatementProtocolMessage protocolMessage1 = new FilterStatementProtocolMessage();
            protocolMessage1.setValue(filterStatement.getValue());
            protocolMessage1.setComparisonSymbol(ComparisonSymbolUtils.getComparisonSymbolOfProtocol(filterStatement.getComparisonSymbol()));
            protocolMessage1.setColumn(FieldTypeUtils.getFieldTypeOfProtocol(filterStatement.getColumn()));
            return protocolMessage1;
        }).collect(Collectors.toList());

        assertThat(filterStatementProtocolMessageList, is(protocolMessage.getFilterStatements()));

        GroupRequestProtocolMessage emptyMessage = (GroupRequestProtocolMessage) rabbitTemplate
                .receiveAndConvert(groupRequestCreatedToShareDataServiceQueue.getName(), 2000);
        assertNull(emptyMessage);
    }

    /**
     * Test publish group request when the group request to be sent is refused
     *
     * @throws Exception no exception expected
     */
    @Test
    public void publishGroupRequestWhenItIsRefused() throws Exception {
        GroupRequest groupRequest = groupRequestRepository.findById(3L).orElseThrow(() -> new GroupRequestNotFoundException(3L));
        List<FilterStatement> filterStatementList = filterStatementRepository.findAllByGroupRequest_Id(3L);
        GroupRequestWrapper groupRequestWrapper = new GroupRequestWrapper(groupRequest, filterStatementList);
        groupRequestPublisher.publishGroupRequest(groupRequestWrapper);

        GroupRequestProtocolMessage emptyMessage1 = (GroupRequestProtocolMessage) rabbitTemplate
                .receiveAndConvert(groupRequestAcceptedToShareDataServiceQueue.getName(), 2000);
        assertNull(emptyMessage1);

        GroupRequestProtocolMessage emptyMessage2 = (GroupRequestProtocolMessage) rabbitTemplate
                .receiveAndConvert(groupRequestCreatedToShareDataServiceQueue.getName(), 2000);
        assertNull(emptyMessage2);
    }
}