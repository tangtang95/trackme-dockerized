package com.poianitibaldizhou.trackme.sharedataservice.assembler;

import com.poianitibaldizhou.trackme.sharedataservice.controller.SendDataController;
import com.poianitibaldizhou.trackme.sharedataservice.util.DataWrapper;
import com.poianitibaldizhou.trackme.sharedataservice.util.ResourceDataWrapper;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Component that facilitates the creation of resources regarding data wrappers of users
 */
@Component
public class ResourceDataWrapperResourceAssembler implements ResourceAssembler<ResourceDataWrapper, Resource<ResourceDataWrapper>>{

    @Override
    public Resource<ResourceDataWrapper> toResource(ResourceDataWrapper dataWrapper) {
        return new Resource<>(dataWrapper,
                linkTo(methodOn(SendDataController.class).sendClusterOfData(dataWrapper.getUserSsn(),
                        new DataWrapper())).withSelfRel());
    }

}
