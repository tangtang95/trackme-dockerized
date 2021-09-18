package com.poianitibaldizhou.trackme.sharedataservice.assembler;

import com.poianitibaldizhou.trackme.sharedataservice.controller.AccessDataController;
import com.poianitibaldizhou.trackme.sharedataservice.util.AggregatedData;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Component that facilitates the creation of resources regarding aggregate data of group requests
 */
@Component
public class AggregatedDataResourceAssembler implements ResourceAssembler<AggregatedData, Resource<AggregatedData>>{

    @Override
    public Resource<AggregatedData> toResource(AggregatedData aggregatedData) {
        return new Resource<>(aggregatedData,
                linkTo(methodOn(AccessDataController.class).getGroupRequestData(aggregatedData.getThirdPartyId().toString(),
                        aggregatedData.getGroupRequestId())).withSelfRel());
    }
}
