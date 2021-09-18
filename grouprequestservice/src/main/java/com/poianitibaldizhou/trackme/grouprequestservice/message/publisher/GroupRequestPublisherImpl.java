package com.poianitibaldizhou.trackme.grouprequestservice.message.publisher;

import com.google.common.collect.ImmutableMap;
import com.poianitibaldizhou.trackme.grouprequestservice.message.protocol.FilterStatementProtocolMessage;
import com.poianitibaldizhou.trackme.grouprequestservice.message.protocol.GroupRequestProtocolMessage;
import com.poianitibaldizhou.trackme.grouprequestservice.message.protocol.enumerator.GroupRequestStatusProtocolMessage;
import com.poianitibaldizhou.trackme.grouprequestservice.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class GroupRequestPublisherImpl implements GroupRequestPublisher {

    private RabbitTemplate rabbitTemplate;

    private TopicExchange groupRequestExchange;

    public GroupRequestPublisherImpl(RabbitTemplate rabbitTemplate, TopicExchange groupRequestExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.groupRequestExchange = groupRequestExchange;
    }

    @Override
    public void publishGroupRequest(GroupRequestWrapper groupRequestWrapper) {
        GroupRequestProtocolMessage groupRequestProtocolMessage = convertToGroupRequestProtocolMessage(groupRequestWrapper);
        rabbitTemplate.convertAndSend(groupRequestExchange.getName(), getRoutingKey(groupRequestProtocolMessage.getStatus()),
                groupRequestProtocolMessage);
        log.info("PUBLISHED GroupRequestProtocolMessage: " + groupRequestProtocolMessage);
    }

    /**
     * Convert the groupRequestWrapper into a groupRequestProtocolMessage
     *
     * @param groupRequestWrapper the group request wrapper to be converted
     * @return the group request protocol message converted from the given group request wrapper
     */
    private GroupRequestProtocolMessage convertToGroupRequestProtocolMessage(GroupRequestWrapper groupRequestWrapper){
        GroupRequestProtocolMessage groupRequestProtocolMessage = new GroupRequestProtocolMessage();
        groupRequestProtocolMessage.setId(groupRequestWrapper.getGroupRequest().getId());
        groupRequestProtocolMessage.setAggregatorOperator(AggregatorOperatorUtils
                .getAggregatorOperatorOfProtocol(groupRequestWrapper.getGroupRequest().getAggregatorOperator()));
        groupRequestProtocolMessage.setRequestType(RequestTypeUtils
                .getRequestTypeOfProtocol(groupRequestWrapper.getGroupRequest().getRequestType()));
        groupRequestProtocolMessage.setThirdPartyId(groupRequestWrapper.getGroupRequest().getThirdPartyId());
        groupRequestProtocolMessage.setCreationTimestamp(groupRequestWrapper.getGroupRequest().getCreationTimestamp());
        groupRequestProtocolMessage.setStatus(RequestStatusUtils
                .getGroupRequestStatusOfProtocol(groupRequestWrapper.getGroupRequest().getStatus()));

        groupRequestProtocolMessage.setFilterStatements(groupRequestWrapper.getFilterStatementList().stream().map(filterStatement -> {
            FilterStatementProtocolMessage filterStatementProtocolMessage = new FilterStatementProtocolMessage();
            filterStatementProtocolMessage.setColumn(FieldTypeUtils.getFieldTypeOfProtocol(filterStatement.getColumn()));
            filterStatementProtocolMessage.setComparisonSymbol(ComparisonSymbolUtils
                    .getComparisonSymbolOfProtocol(filterStatement.getComparisonSymbol()));
            filterStatementProtocolMessage.setValue(filterStatement.getValue());
            return filterStatementProtocolMessage;
        }).collect(Collectors.toList()));

        return groupRequestProtocolMessage;
    }

    /**
     * Return the routing key based on the status of the group request protocol message
     *
     * @param statusProtocolMessage the status of the group request of protocol message
     * @return the routing key based on the status of the group request protocol
     */
    private String getRoutingKey(GroupRequestStatusProtocolMessage statusProtocolMessage){
        Map<GroupRequestStatusProtocolMessage, String> routingKeyMap = ImmutableMap.<GroupRequestStatusProtocolMessage, String>builder()
                .put(GroupRequestStatusProtocolMessage.ACCEPTED, "grouprequest.event.accepted")
                .put(GroupRequestStatusProtocolMessage.UNDER_ANALYSIS, "grouprequest.event.created")
                .put(GroupRequestStatusProtocolMessage.REFUSED, "grouprequest.event.refused")
                .build();
        return routingKeyMap.get(statusProtocolMessage);
    }
}
