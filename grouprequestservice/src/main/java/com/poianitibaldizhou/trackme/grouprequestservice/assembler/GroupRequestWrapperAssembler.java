package com.poianitibaldizhou.trackme.grouprequestservice.assembler;

import com.poianitibaldizhou.trackme.grouprequestservice.controller.GroupRequestController;
import com.poianitibaldizhou.trackme.grouprequestservice.util.GroupRequestWrapper;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Component that facilitates the creation of resources regarding a group request
 */
@Component
public class GroupRequestWrapperAssembler implements ResourceAssembler<GroupRequestWrapper, Resource<GroupRequestWrapper>>{


    @Override
    public Resource<GroupRequestWrapper> toResource(GroupRequestWrapper groupRequestWrapper) {
        return new Resource<>(groupRequestWrapper,
                linkTo(methodOn(GroupRequestController.class).getRequest(groupRequestWrapper.getGroupRequest().getThirdPartyId().toString(), groupRequestWrapper.getGroupRequest().getId())).withSelfRel()
                );
    }
}
