package com.poianitibaldizhou.trackme.apigateway.message.publisher;

import com.poianitibaldizhou.trackme.apigateway.entity.CompanyDetail;
import com.poianitibaldizhou.trackme.apigateway.entity.PrivateThirdPartyDetail;

public interface ThirdPartyPublisher {

    /**
     * Send the private third party detail to the message broker (rabbit-mq)
     *
     * @param privateThirdPartyDetail the private third party detail to be sent
     */
    void publishPrivateThirdPartyCreated(PrivateThirdPartyDetail privateThirdPartyDetail);

    /**
     * Send the company third party detail to the message broker (rabbit-mq)
     *
     * @param companyDetail the company third party detail to be sent
     */
    void publishCompanyThirdPartyCreated(CompanyDetail companyDetail);

}
