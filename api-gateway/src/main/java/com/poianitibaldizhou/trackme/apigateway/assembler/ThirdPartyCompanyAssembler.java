package com.poianitibaldizhou.trackme.apigateway.assembler;

import com.poianitibaldizhou.trackme.apigateway.controller.SecuredThirdPartyController;
import com.poianitibaldizhou.trackme.apigateway.util.ThirdPartyCompanyWrapper;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Component that facilitates the creation of resources regarding third party customers related with companies
 */
@Component
public class ThirdPartyCompanyAssembler implements ResourceAssembler<ThirdPartyCompanyWrapper, Resource<ThirdPartyCompanyWrapper>> {

    @Override
    public Resource<ThirdPartyCompanyWrapper> toResource(ThirdPartyCompanyWrapper thirdPartyCompanyWrapper) {
        return new Resource<>(thirdPartyCompanyWrapper,
                linkTo(methodOn(SecuredThirdPartyController.class).
                        getThirdParty(thirdPartyCompanyWrapper.getThirdPartyCustomer())).withSelfRel());
    }
}
