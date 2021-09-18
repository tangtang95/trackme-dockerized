package com.poianitibaldizhou.trackme.sharedataservice.assembler;

import com.poianitibaldizhou.trackme.sharedataservice.controller.SendDataController;
import com.poianitibaldizhou.trackme.sharedataservice.entity.PositionData;
import com.poianitibaldizhou.trackme.sharedataservice.util.DataWrapper;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Component that facilitates the creation of resources regarding the position data of users
 */
@Component
public class PositionDataResourceAssembler implements ResourceAssembler<PositionData, Resource<PositionData>> {

    @Override
    public Resource<PositionData> toResource(PositionData positionData) {
        return new Resource<>(positionData,
                linkTo(methodOn(SendDataController.class).sendPositionData(positionData.getUser().getSsn(),
                        positionData)).withSelfRel());
    }
}
