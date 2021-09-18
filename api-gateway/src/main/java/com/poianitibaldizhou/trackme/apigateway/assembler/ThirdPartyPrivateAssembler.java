package com.poianitibaldizhou.trackme.apigateway.assembler;

import com.poianitibaldizhou.trackme.apigateway.controller.SecuredThirdPartyController;
import com.poianitibaldizhou.trackme.apigateway.util.ThirdPartyPrivateWrapper;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Component that facilitates the creation of resources regarding third party customers non-related with companies
 */
@Component
public class ThirdPartyPrivateAssembler implements ResourceAssembler<ThirdPartyPrivateWrapper, Resource<ThirdPartyPrivateWrapper>>  {

    @Override
    public Resource<ThirdPartyPrivateWrapper> toResource(ThirdPartyPrivateWrapper thirdPartyPrivateWrapper) {
        return new Resource<>(thirdPartyPrivateWrapper,
                linkTo(methodOn(SecuredThirdPartyController.class).
                        getThirdParty(thirdPartyPrivateWrapper.getThirdPartyCustomer())).withSelfRel());
    }
}
