package com.poianitibaldizhou.trackme.apigateway.util;

import com.poianitibaldizhou.trackme.apigateway.entity.PrivateThirdPartyDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * This class wraps the information regarding third party customer that are private (i.e. non company)
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ThirdPartyPrivateWrapper extends ThirdPartyWrapper {
    private transient PrivateThirdPartyDetail privateThirdPartyDetail;
}
