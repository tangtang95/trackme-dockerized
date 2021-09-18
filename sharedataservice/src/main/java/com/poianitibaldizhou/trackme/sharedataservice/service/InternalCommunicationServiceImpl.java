package com.poianitibaldizhou.trackme.sharedataservice.service;

import com.poianitibaldizhou.trackme.sharedataservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.sharedataservice.entity.GroupRequest;
import com.poianitibaldizhou.trackme.sharedataservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.sharedataservice.entity.User;
import com.poianitibaldizhou.trackme.sharedataservice.exception.UserNotFoundException;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.GroupRequestProtocolMessage;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.IndividualRequestProtocolMessage;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.UserProtocolMessage;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.enumerator.IndividualRequestStatusProtocolMessage;
import com.poianitibaldizhou.trackme.sharedataservice.message.publisher.NumberOfUserInvolvedDataPublisher;
import com.poianitibaldizhou.trackme.sharedataservice.repository.FilterStatementRepository;
import com.poianitibaldizhou.trackme.sharedataservice.repository.GroupRequestRepository;
import com.poianitibaldizhou.trackme.sharedataservice.repository.IndividualRequestRepository;
import com.poianitibaldizhou.trackme.sharedataservice.repository.UserRepository;
import com.poianitibaldizhou.trackme.sharedataservice.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Profile("usage-message-broker")
public class InternalCommunicationServiceImpl implements InternalCommunicationService {

    private final UserRepository userRepository;
    private final FilterStatementRepository filterStatementRepository;
    private final GroupRequestRepository groupRequestRepository;
    private final IndividualRequestRepository individualRequestRepository;
    private final NumberOfUserInvolvedDataPublisher numberOfUserInvolvedDataPublisher;

    public InternalCommunicationServiceImpl(UserRepository userRepository,
                                            FilterStatementRepository filterStatementRepository,
                                            GroupRequestRepository groupRequestRepository,
                                            IndividualRequestRepository individualRequestRepository,
                                            NumberOfUserInvolvedDataPublisher numberOfUserInvolvedDataPublisher) {
        this.userRepository = userRepository;
        this.filterStatementRepository = filterStatementRepository;
        this.groupRequestRepository = groupRequestRepository;
        this.individualRequestRepository = individualRequestRepository;
        this.numberOfUserInvolvedDataPublisher = numberOfUserInvolvedDataPublisher;
    }

    @Override
    public void handleGroupRequestAccepted(GroupRequestProtocolMessage groupRequestProtocolMessage) {
        GroupRequest groupRequest = convertToGroupRequest(groupRequestProtocolMessage);
        List<FilterStatement> filterStatementList = convertToFilterStatementList(groupRequestProtocolMessage);
        GroupRequest groupRequestSaved = groupRequestRepository.save(groupRequest);
        filterStatementList.forEach(filterStatement ->{
            filterStatement.setGroupRequest(groupRequestSaved);
            filterStatementRepository.save(filterStatement);
        });
    }

    @Override
    public void handleIndividualRequestAccepted(IndividualRequestProtocolMessage individualRequestProtocolMessage) {
        User user = userRepository.findById(individualRequestProtocolMessage.getUserSsn())
                .orElseThrow(() -> new UserNotFoundException(individualRequestProtocolMessage.getUserSsn()));
        IndividualRequest individualRequest = new IndividualRequest();
        individualRequest.setId(individualRequestProtocolMessage.getId());
        individualRequest.setThirdPartyId(individualRequestProtocolMessage.getThirdPartyId());
        individualRequest.setUser(user);
        individualRequest.setStartDate(individualRequestProtocolMessage.getStartDate());
        individualRequest.setEndDate(individualRequestProtocolMessage.getEndDate());
        individualRequest.setCreationTimestamp(individualRequestProtocolMessage.getCreationTimestamp());
        individualRequestRepository.save(individualRequest);
    }

    @Override
    public void handleUserCreated(UserProtocolMessage userProtocolMessage) {
        if(userRepository.existsById(userProtocolMessage.getSsn())) {
            log.error("FATAL ERROR: " + userProtocolMessage + "already existing");
            return;
        }
        User user = new User();
        user.setSsn(userProtocolMessage.getSsn());
        user.setFirstName(userProtocolMessage.getFirstName());
        user.setLastName(userProtocolMessage.getLastName());
        user.setBirthDate(userProtocolMessage.getBirthDate());
        user.setBirthCity(userProtocolMessage.getBirthCity());
        user.setBirthNation(userProtocolMessage.getBirthNation());
        userRepository.save(user);
    }

    @Override
    public void handleGroupRequestCreated(GroupRequestProtocolMessage groupRequestProtocolMessage) {
        GroupRequest groupRequest = convertToGroupRequest(groupRequestProtocolMessage);
        List<FilterStatement> filterStatementList = convertToFilterStatementList(groupRequestProtocolMessage);
        AggregatorOperator distinctCountOperator = AggregatorOperator.DISTINCT_COUNT;
        RequestType userSsnRequestType = RequestType.USER_SSN;
        Double numberOfUserInvolved = userRepository.getAggregatedData(distinctCountOperator,
                userSsnRequestType, filterStatementList);

        numberOfUserInvolvedDataPublisher.publishNumberOfUserInvolvedData(groupRequest.getId(),
                numberOfUserInvolved.intValue());
    }

    /**
     * Convert the groupRequestProtocolMessage into a GroupRequest
     *
     * @param groupRequestProtocol the group request protocol message
     * @return the group request contained in the group request protocol message
     */
    private GroupRequest convertToGroupRequest(GroupRequestProtocolMessage groupRequestProtocol){
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setId(groupRequestProtocol.getId());
        groupRequest.setCreationTimestamp(groupRequestProtocol.getCreationTimestamp());
        groupRequest.setAggregatorOperator(AggregatorOperatorUtils
                .getAggregatorOperator(groupRequestProtocol.getAggregatorOperator()));
        groupRequest.setRequestType(RequestTypeUtils.getRequestType(groupRequestProtocol.getRequestType()));
        groupRequest.setThirdPartyId(groupRequestProtocol.getThirdPartyId());
        return groupRequest;
    }

    /**
     * Convert the groupRequestProtocolMessage into a list of filter statement
     *
     * @param groupRequestProtocol the group request protocol message
     * @return the list of filter statement contained in the group request protocol message
     */
    private List<FilterStatement> convertToFilterStatementList(GroupRequestProtocolMessage groupRequestProtocol){
        return groupRequestProtocol.getFilterStatements().stream()
                .map(filterStatementProtocol -> {
                    FilterStatement filterStatement = new FilterStatement();
                    filterStatement.setColumn(FieldTypeUtils.getFieldType(filterStatementProtocol.getColumn()));
                    filterStatement.setComparisonSymbol(ComparisonSymbolUtils
                            .getComparisonSymbol(filterStatementProtocol.getComparisonSymbol()));
                    filterStatement.setValue(filterStatementProtocol.getValue());
                    return filterStatement;
                }).collect(Collectors.toList());
    }
}
