package com.poianitibaldizhou.trackme.grouprequestservice.service;

import com.poianitibaldizhou.trackme.grouprequestservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.grouprequestservice.entity.GroupRequest;
import com.poianitibaldizhou.trackme.grouprequestservice.message.protocol.NumberOfUserInvolvedProtocolMessage;
import com.poianitibaldizhou.trackme.grouprequestservice.message.publisher.GroupRequestPublisher;
import com.poianitibaldizhou.trackme.grouprequestservice.repository.FilterStatementRepository;
import com.poianitibaldizhou.trackme.grouprequestservice.repository.GroupRequestRepository;
import com.poianitibaldizhou.trackme.grouprequestservice.util.GroupRequestWrapper;
import com.poianitibaldizhou.trackme.grouprequestservice.util.RequestStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Profile("usage-message-broker")
public class InternalCommunicationServiceImpl implements InternalCommunicationService {

    private Integer minNumberOfUserInvolved;

    private final GroupRequestRepository groupRequestRepository;
    private final FilterStatementRepository filterStatementRepository;
    private final GroupRequestPublisher groupRequestPublisher;

    public InternalCommunicationServiceImpl(GroupRequestRepository groupRequestRepository,
                                            FilterStatementRepository filterStatementRepository,
                                            GroupRequestPublisher groupRequestPublisher) {
        this.groupRequestRepository = groupRequestRepository;
        this.filterStatementRepository = filterStatementRepository;
        this.groupRequestPublisher = groupRequestPublisher;
    }

    public void setMinNumberOfUserInvolved(Integer minNumberOfUserInvolved){
        this.minNumberOfUserInvolved = minNumberOfUserInvolved;
        log.info("numberOfUserInvolved: " + minNumberOfUserInvolved);
    }

    @Override
    public void handleNumberOfUserInvolvedData(NumberOfUserInvolvedProtocolMessage protocolMessage) {
        Optional<GroupRequest> groupRequestOptional = groupRequestRepository.findById(protocolMessage.getGroupRequestId());
        if (!NumberOfUserInvolvedProtocolMessage.validateMessage(protocolMessage)){
            log.error("FATAL ERROR: Bad protocol message with null fields " + protocolMessage);
            return;
        }
        if(!groupRequestOptional.isPresent()){
            log.error("FATAL ERROR: Group request with id " + protocolMessage.getGroupRequestId() + " not found");
            return;
        }
        GroupRequest groupRequest = groupRequestOptional.get();
        if(!groupRequest.getStatus().equals(RequestStatus.UNDER_ANALYSIS)){
            log.error("FATAL ERROR: Group request is not UNDER_ANALYSIS " + groupRequestOptional.get());
            return;
        }

        if(protocolMessage.getNumberOfUserInvolved() >= minNumberOfUserInvolved){
            groupRequest.setStatus(RequestStatus.ACCEPTED);
            groupRequestRepository.save(groupRequest);
            List<FilterStatement> filterStatementList = filterStatementRepository.findAllByGroupRequest_Id(groupRequest.getId());
            sendGroupRequestMessage(groupRequest, filterStatementList);
        }
        else{
            groupRequest.setStatus(RequestStatus.REFUSED);
            groupRequestRepository.save(groupRequest);
        }
    }

    @Override
    public void sendGroupRequestMessage(GroupRequest groupRequest, List<FilterStatement> filterStatementList) {
        GroupRequestWrapper groupRequestWrapper = new GroupRequestWrapper(groupRequest, filterStatementList);
        groupRequestPublisher.publishGroupRequest(groupRequestWrapper);
    }

}
