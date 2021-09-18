package com.poianitibaldizhou.trackme.individualrequestservice.assembler;

import com.poianitibaldizhou.trackme.individualrequestservice.controller.IndividualRequestController;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.BlockedThirdParty;
import com.poianitibaldizhou.trackme.individualrequestservice.util.Constants;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Component that facilitates the creation of resources regarding the block of third party customers
 */
@Component
public class BlockedThirdPartyResourceAssembler implements ResourceAssembler<BlockedThirdParty, Resource<BlockedThirdParty>> {
    @Override
    public Resource<BlockedThirdParty> toResource(BlockedThirdParty blockedThirdParty) {
        return new Resource<>(blockedThirdParty,
                linkTo(methodOn(IndividualRequestController.class).getUserPendingRequests(
                        blockedThirdParty.getKey().getUser().getSsn()))
                        .withRel(Constants.REL_USER_PENDING_REQUEST));
    }
}
