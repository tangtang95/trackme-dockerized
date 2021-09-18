package com.poianitibaldizhou.trackme.apigateway.service;

import com.poianitibaldizhou.trackme.apigateway.entity.CompanyDetail;
import com.poianitibaldizhou.trackme.apigateway.entity.PrivateThirdPartyDetail;
import com.poianitibaldizhou.trackme.apigateway.entity.User;
import com.poianitibaldizhou.trackme.apigateway.message.publisher.ThirdPartyPublisher;
import com.poianitibaldizhou.trackme.apigateway.message.publisher.UserPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("usage-message-broker")
@Service
public class InternalCommunicationServiceImpl implements InternalCommunicationService{

    private UserPublisher userPublisher;
    private ThirdPartyPublisher thirdPartyPublisher;

    public InternalCommunicationServiceImpl(UserPublisher userPublisher, ThirdPartyPublisher thirdPartyPublisher) {
        this.userPublisher = userPublisher;
        this.thirdPartyPublisher = thirdPartyPublisher;
    }

    @Override
    public void broadcastUserMessage(User user) {
        userPublisher.publishUserCreated(user);
    }

    @Override
    public void broadcastPrivateThirdPartyMessage(PrivateThirdPartyDetail privateDetail) {
        thirdPartyPublisher.publishPrivateThirdPartyCreated(privateDetail);
    }

    @Override
    public void broadcastCompanyThirdPartyMessage(CompanyDetail companyDetail) {
        thirdPartyPublisher.publishCompanyThirdPartyCreated(companyDetail);
    }
}
