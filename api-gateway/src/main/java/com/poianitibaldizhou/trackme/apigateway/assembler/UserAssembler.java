package com.poianitibaldizhou.trackme.apigateway.assembler;

import com.poianitibaldizhou.trackme.apigateway.controller.SecuredUserController;
import com.poianitibaldizhou.trackme.apigateway.entity.User;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Component that facilitates the creation of resources regarding the user
 */
@Component
public class UserAssembler implements ResourceAssembler<User, Resource<User>>{

    @Override
    public Resource<User> toResource(User user) {
        return new Resource<>(user,
                linkTo(methodOn(SecuredUserController.class).getUser(user)).withSelfRel());
    }
}
