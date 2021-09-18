package com.poianitibaldizhou.trackme.individualrequestservice.assembler;

import com.poianitibaldizhou.trackme.individualrequestservice.controller.IndividualRequestController;
import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestWrapper;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Component that facilitates the creation of resources regarding the individual request
 */
@Component
public class IndividualRequestWrapperResourceAssembler implements ResourceAssembler<IndividualRequestWrapper, Resource<IndividualRequestWrapper>> {
    @Override
    public Resource<IndividualRequestWrapper> toResource(IndividualRequestWrapper individualRequest) {
        return new Resource<>(individualRequest,
                linkTo(methodOn(IndividualRequestController.class).getRequestById(
                        individualRequest.getThirdPartyId().toString(),
                        individualRequest.getUserSsn() ,
                        individualRequest.getId()))
                        .withSelfRel());
    }
}