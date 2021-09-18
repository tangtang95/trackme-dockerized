package com.poianitibaldizhou.trackme.individualrequestservice.service;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.ThirdParty;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.User;
import com.poianitibaldizhou.trackme.individualrequestservice.message.protocol.ThirdPartyProtocolMessage;
import com.poianitibaldizhou.trackme.individualrequestservice.message.protocol.UserProtocolMessage;
import com.poianitibaldizhou.trackme.individualrequestservice.message.publisher.IndividualRequestPublisher;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.ThirdPartyRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.UserRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Profile("usage-message-broker")
@Service
public class InternalCommunicationServiceImpl implements InternalCommunicationService {

    private final UserRepository userRepository;
    private final ThirdPartyRepository thirdPartyRepository;
    private final IndividualRequestPublisher individualRequestPublisher;

    public InternalCommunicationServiceImpl(UserRepository userRepository, ThirdPartyRepository thirdPartyRepository,
                                            IndividualRequestPublisher individualRequestPublisher) {
        this.userRepository = userRepository;
        this.thirdPartyRepository = thirdPartyRepository;
        this.individualRequestPublisher = individualRequestPublisher;
    }

    @Override
    public void handleUserCreated(UserProtocolMessage userProtocolMessage) {
        if(!UserProtocolMessage.validateMessage(userProtocolMessage)){
            log.error("FATAL ERROR: Received a user which has not all attributes non-null " + userProtocolMessage);
            return;
        }
        if(userRepository.existsById(userProtocolMessage.getSsn())){
            log.error("FATAL ERROR: Received a user which is already in the DB");
            return;
        }
        User user = new User(userProtocolMessage.getSsn());
        userRepository.save(user);
    }

    @Override
    public void sendIndividualRequest(IndividualRequest individualRequest) {
        individualRequestPublisher.publishIndividualRequest(individualRequest);
    }

    @Override
    public void handlePrivateThirdParty(ThirdPartyProtocolMessage protocolMessage) {
        ThirdParty thirdParty = new ThirdParty();
        thirdParty.setId(protocolMessage.getId());
        thirdParty.setIdentifierName(protocolMessage.getName() + Constants.SPACE + protocolMessage.getSurname());
        thirdPartyRepository.save(thirdParty);
    }

    @Override
    public void handleCompanyThirdParty(ThirdPartyProtocolMessage protocolMessage) {
        ThirdParty thirdParty = new ThirdParty();
        thirdParty.setId(protocolMessage.getId());
        thirdParty.setIdentifierName(protocolMessage.getCompanyName());
        thirdPartyRepository.save(thirdParty);
    }

}
