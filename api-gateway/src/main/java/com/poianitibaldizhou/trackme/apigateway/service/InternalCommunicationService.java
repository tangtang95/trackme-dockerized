package com.poianitibaldizhou.trackme.apigateway.service;

import com.poianitibaldizhou.trackme.apigateway.entity.CompanyDetail;
import com.poianitibaldizhou.trackme.apigateway.entity.PrivateThirdPartyDetail;
import com.poianitibaldizhou.trackme.apigateway.entity.User;

public interface InternalCommunicationService {

    void broadcastUserMessage(User user);
    void broadcastPrivateThirdPartyMessage(PrivateThirdPartyDetail privateDetail);
    void broadcastCompanyThirdPartyMessage(CompanyDetail companyDetail);

}
