package com.poianitibaldizhou.trackme.grouprequestservice.controller;

import com.poianitibaldizhou.trackme.grouprequestservice.assembler.GroupRequestWrapperAssembler;
import com.poianitibaldizhou.trackme.grouprequestservice.exception.ImpossibleAccessException;
import com.poianitibaldizhou.trackme.grouprequestservice.service.GroupRequestManagerService;
import com.poianitibaldizhou.trackme.grouprequestservice.util.Constants;
import com.poianitibaldizhou.trackme.grouprequestservice.util.GroupRequestWrapper;
import com.poianitibaldizhou.trackme.grouprequestservice.util.RequestStatus;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Entry point for accessing the service that regards the group requests
 */
@RestController
@RequestMapping(path = Constants.GROUP_REQUEST_API)
public class GroupRequestController {

    private final GroupRequestManagerService requestManagerService;

    private final GroupRequestWrapperAssembler groupRequestWrapperAssembler;

    /**
     * Creates a new entry point for accessing the service that regards the group requests
     *
     * @param requestManagerService service that manages the group requests: needed in order to
     *                                        accessing the business functions of the service
     * @param groupRequestWrapperAssembler assembler for group request wrapper that adds hypermedia content (HAL)
     */
    GroupRequestController(GroupRequestManagerService requestManagerService,
                           GroupRequestWrapperAssembler groupRequestWrapperAssembler) {
        this.requestManagerService = requestManagerService;
        this.groupRequestWrapperAssembler = groupRequestWrapperAssembler;
    }

    // GET METHODS

    /**
     * This method will return a group request, with its related filter statements, identified by a certain id.
     *
     * @param requestingThirdParty id of the third party customer that is accessing this method
     * @param id id of the interested group request
     * @return resource containing the requested individual request
     */
    @GetMapping(Constants.GROUP_REQUEST_BY_ID_API)
    public @ResponseBody Resource<Object> getRequest(@RequestHeader(Constants.HEADER_THIRD_PARTY_ID) String requestingThirdParty,
                                                     @PathVariable Long id) {
        Long thirdPartyId = Long.parseLong(requestingThirdParty);

        GroupRequestWrapper groupRequestWrapper = requestManagerService.getById(id);

        if(!groupRequestWrapper.getGroupRequest().getThirdPartyId().equals(thirdPartyId))
            throw new ImpossibleAccessException();

        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(GroupRequestController.class).getRequest(groupRequestWrapper.getGroupRequest().getThirdPartyId().toString(),
                groupRequestWrapper.getGroupRequest().getId())).withSelfRel());
        if(groupRequestWrapper.getGroupRequest().getStatus() == RequestStatus.ACCEPTED)
            links.add(new Link(Constants.FAKE_URL + Constants.EXT_API_ACCESS_GROUP_REQUEST_DATA + "/" + groupRequestWrapper.getGroupRequest().getId(),
                    Constants.EXT_API_ACCESS_GROUP_REQUEST_DATA_REL));

        return new Resource<>(groupRequestWrapper, links);
    }

    /**
     * This method will access all the group requests performed by a certain third party customer
     *
     * @param requestingThirdParty id of the third party customer that is accessing this method
     * @return  set of resources of size 2: the first item is the set of demanded requests, embedded with their own
     * link. The second one provides a self reference to this method
     */
    @GetMapping(Constants.GROUP_REQUEST_BY_THIRD_PARTY_API)
    public @ResponseBody Resources<Resource<GroupRequestWrapper>> getRequestByThirdParty(
            @RequestHeader(value = Constants.HEADER_THIRD_PARTY_ID) String requestingThirdParty) {
        Long thirdPartyId = Long.parseLong(requestingThirdParty);

        List<GroupRequestWrapper> requestWrappers = requestManagerService.getByThirdPartyId(thirdPartyId);

        List<Resource<GroupRequestWrapper>> requests = requestWrappers.stream()
                .map(groupRequestWrapperAssembler::toResource).collect(Collectors.toList());

        return new Resources<>(requests,
                linkTo(methodOn(GroupRequestController.class).getRequestByThirdParty(requestingThirdParty)).withSelfRel());
    }

    // POST METHODS

    /**
     * This method will create a new group request
     *
     * @param requestingThirdParty id of the third party customer that is accessing this method
     * @param groupRequestWrapper group request that is asked to be added inside the system
     * @return an entity containing information about the created request, embedded with useful links to accessing
     * the new resource
     * @throws URISyntaxException due to the creation of a new uri resource: this will throw some exception if the syntax
     * is not well expressed
     */
    @PostMapping(Constants.NEW_GROUP_REQUEST_API)
    public @ResponseBody ResponseEntity<?> newRequest(@RequestHeader(Constants.HEADER_THIRD_PARTY_ID) String requestingThirdParty,
                                                      @RequestBody GroupRequestWrapper groupRequestWrapper) throws URISyntaxException {
        Long thirdPartyId = Long.parseLong(requestingThirdParty);

        groupRequestWrapper.getGroupRequest().setThirdPartyId(thirdPartyId);
        groupRequestWrapper.getFilterStatementList().forEach(
                filterStatement -> filterStatement.setGroupRequest(groupRequestWrapper.getGroupRequest()));

        Resource<GroupRequestWrapper> resource = groupRequestWrapperAssembler.toResource(requestManagerService.addGroupRequest(groupRequestWrapper));

        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }
}
