package com.poianitibaldizhou.trackme.individualrequestservice.assembler;

import com.poianitibaldizhou.trackme.individualrequestservice.controller.IndividualRequestController;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.Response;
import com.poianitibaldizhou.trackme.individualrequestservice.util.Constants;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Component that facilitates the creation of resources regarding the individual response
 */
@Component
public class ResponseResourceAssembler implements ResourceAssembler<Response, Resource<Response>> {

    @Override
    public Resource<Response> toResource(Response response) {
        return new Resource<>(response,
                linkTo(methodOn(IndividualRequestController.class).getUserPendingRequests(
                        response.getRequest().getUser().getSsn()))
                        .withRel(Constants.REL_USER_PENDING_REQUEST),
                linkTo(methodOn(IndividualRequestController.class).getRequestById(
                        response.getRequest().getThirdParty().toString(),
                        response.getRequest().getUser().getSsn(),
                        response.getRequest().getId()))
                        .withRel(Constants.REL_REQUEST));
    }
}
