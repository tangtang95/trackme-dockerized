package com.poianitibaldizhou.trackme.apigateway.util;

import com.poianitibaldizhou.trackme.apigateway.entity.ThirdPartyCustomer;
import lombok.Data;

import java.io.Serializable;

/**
 * General wrapper of a third party customer that contains only the customer
 */
@Data
public class ThirdPartyWrapper{
    private ThirdPartyCustomer thirdPartyCustomer;
}
