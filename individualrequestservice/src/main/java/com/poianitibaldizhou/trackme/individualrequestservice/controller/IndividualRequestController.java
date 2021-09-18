package com.poianitibaldizhou.trackme.individualrequestservice.controller;

import com.poianitibaldizhou.trackme.individualrequestservice.assembler.IndividualRequestWrapperResourceAssembler;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.ThirdParty;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.User;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.ImpossibleAccessException;
import com.poianitibaldizhou.trackme.individualrequestservice.service.IndividualRequestManagerService;
import com.poianitibaldizhou.trackme.individualrequestservice.util.Constants;
import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestStatus;
import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestWrapper;
import org.apache.logging.log4j.util.Strings;
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
 * Entry point for accessing the service that regards the individual request
 */
@RestController
@RequestMapping(path = Constants.REQUEST_API)
public class IndividualRequestController {

    private final IndividualRequestManagerService requestManagerService;

    private final IndividualRequestWrapperResourceAssembler assembler;

    /**
     * Creates a new entry point for accessing the service that regards the individual request
     *
     * @param individualRequestManagerService service that manages the individual request: needed in order to
     *                                        accessing the business functions of the service
     * @param assembler assembler for individual request that adds hypermedia content (HAL)
     */
    IndividualRequestController(IndividualRequestManagerService individualRequestManagerService, IndividualRequestWrapperResourceAssembler assembler) {
        this.requestManagerService = individualRequestManagerService;
        this.assembler = assembler;
    }

    // User and third party access point to the service

    /**
     * This method will return a request identified with a certain id, provided with some useful links
     *
     * @param requestingThirdParty third party customer that is accessing this method
     * @param requestingUser user that is accessing this method
     * @param id id of the demanded request
     * @return resource containing the individual request
     */
    @GetMapping(path = Constants.REQUEST_BY_ID_API)
    public @ResponseBody Resource<Object> getRequestById(@RequestHeader(value = Constants.HEADER_THIRD_PARTY_ID) String requestingThirdParty,
                                                                    @RequestHeader(value = Constants.HEADER_USER_SSN) String requestingUser,
                                                                    @PathVariable Long id) {
        IndividualRequestWrapper request = IndividualRequestWrapper.convertIntoWrapper(requestManagerService.getRequestById(id));

        if(requestingUser.isEmpty() && requestingThirdParty.isEmpty())
            throw new ImpossibleAccessException();

        if(!requestingThirdParty.isEmpty() && request.getThirdPartyId() != Long.parseLong(requestingThirdParty)) {
                throw new ImpossibleAccessException();
        }

        if(!requestingUser.isEmpty() && !request.getUserSsn().equals(requestingUser))
                throw new ImpossibleAccessException();

        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(IndividualRequestController.class).getRequestById(
                request.getThirdPartyId().toString(),
                request.getUserSsn() ,
                request.getId()))
                .withSelfRel());
        if(!requestingThirdParty.isEmpty() && request.getStatus() == IndividualRequestStatus.ACCEPTED) {
            links.add(new Link(Constants.FAKE_URL + Constants.EXT_API_ACCESS_INDIVIDUAL_REQUEST_DATA + "/" + request.getId(),
                    Constants.EXTP_API_ACCESS_INDIVIDUAL_REQUEST_DATA_REL));
        }
        if(requestingThirdParty.isEmpty() && request.getStatus() == IndividualRequestStatus.PENDING) {
            links.add(linkTo(methodOn(ResponseController.class).newResponse(
                    request.getUserSsn(),
                    request.getId(),
                    Strings.EMPTY)).withRel(Constants.REL_ADD_RESPONSE));
        }
        return new Resource<>(request, links);
    }


    // User access point to the service

    /**
     * This method will return the requests of a certain user, that are marked with status PENDING
     *
     * @param requestingUser user that is accessing this method
     * @return set of resources of size 2: the first item is the set of pending requests, embedded with
     * their own link. The second one provides a self reference to this method.
     */
    @GetMapping(path = Constants.PENDING_REQUEST_BY_USER_API)
    public @ResponseBody Resources<Resource<IndividualRequestWrapper>> getUserPendingRequests(
            @RequestHeader(value = Constants.HEADER_USER_SSN) String requestingUser) {
        User user = new User(requestingUser);

        List<Resource<IndividualRequestWrapper>> pendingRequests = requestManagerService.getUserPendingRequests(user).stream()
                .map(IndividualRequestWrapper::convertIntoWrapper)
                .map(assembler::toResource).collect(Collectors.toList());

        return new Resources<>(pendingRequests,
                linkTo(methodOn(IndividualRequestController.class).getUserPendingRequests(requestingUser)).withSelfRel());
    }

    // Third party customer access point to the service

    /**
     * This method will return the requests performed by a certain third party customer
     *
     * @param requestingThirdParty third party that requests the access to this method
     * @return set of resources of size 2: the first item is the set of demanded requests, embedded with their own
     * link. The second one provides a self reference to this method
     */
    @GetMapping(path = Constants.REQUEST_BY_THIRD_PARTY_ID)
    public @ResponseBody Resources<Resource<IndividualRequestWrapper>> getThirdPartyRequests(
            @RequestHeader(value = Constants.HEADER_THIRD_PARTY_ID) String requestingThirdParty) {

        Long thirdParty = Long.parseLong(requestingThirdParty);

        List<Resource<IndividualRequestWrapper>> requests = requestManagerService.getThirdPartyRequests(thirdParty).stream()
                .map(IndividualRequestWrapper::convertIntoWrapper)
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(requests,
                linkTo(methodOn(IndividualRequestController.class).getThirdPartyRequests(requestingThirdParty)).withSelfRel());
    }

    /**
     * Add a new request to the set of individual request.
     * The request will not be added in the case in which it is performed on a non existing user.
     *
     * @param requestingThirdParty id of the third party that is requesting this method
     * @param ssn the request regards the user identified by this field
     * @param newRequest request that will be added to the system
     * @return an http 201 created message that contains the newly formed link
     * @throws URISyntaxException due to the creation of a new URI resource
     */
    @PostMapping(path = Constants.NEW_REQUEST_API)
    public @ResponseBody ResponseEntity<?> newRequest(
            @RequestHeader(value = Constants.HEADER_THIRD_PARTY_ID) String requestingThirdParty,
            @PathVariable String ssn,
            @RequestBody IndividualRequest newRequest) throws URISyntaxException {

        newRequest.setUser(new User(ssn));
        newRequest.setThirdParty(new ThirdParty(Long.parseLong(requestingThirdParty)));

        Resource<IndividualRequestWrapper> resource = assembler.toResource(IndividualRequestWrapper
                .convertIntoWrapper(requestManagerService.addRequest(newRequest)));

        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }
}
