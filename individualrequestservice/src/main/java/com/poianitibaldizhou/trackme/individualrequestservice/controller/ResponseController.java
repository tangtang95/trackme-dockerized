package com.poianitibaldizhou.trackme.individualrequestservice.controller;

import com.poianitibaldizhou.trackme.individualrequestservice.assembler.BlockedThirdPartyResourceAssembler;
import com.poianitibaldizhou.trackme.individualrequestservice.assembler.ResponseResourceAssembler;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.BlockedThirdParty;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.Response;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.User;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.BadResponseTypeException;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.ImpossibleAccessException;
import com.poianitibaldizhou.trackme.individualrequestservice.service.IndividualRequestManagerService;
import com.poianitibaldizhou.trackme.individualrequestservice.service.UploadResponseService;
import com.poianitibaldizhou.trackme.individualrequestservice.util.Constants;
import com.poianitibaldizhou.trackme.individualrequestservice.util.ResponseType;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Entry point for accessing the service that regards the responses to individual request
 */
@RestController
@RequestMapping(path = Constants.RESPONSE_API)
public class ResponseController {

    private final UploadResponseService uploadResponseService;
    private final ResponseResourceAssembler responseAssembler;
    private final BlockedThirdPartyResourceAssembler blockAssembler;
    private final IndividualRequestManagerService individualRequestManagerService;

    /**
     * Creates a new entry point for accessing the service that regards the responses to individual requests
     *
     * @param uploadResponseService service that manages the responses: needed in order to access
     *                              the business functions of the service
     * @param responseAssembler assembler for responses that adds hypermedia content (HAL)
     * @param blockAssembler assembler for blocks of third party customer that adds hypermedia content (HAL)
     * @param individualRequestManagerService service that manages the individual request, useful to provide
     *                                        some kind of authorization to the methods
     */
    ResponseController(UploadResponseService uploadResponseService,
                       ResponseResourceAssembler responseAssembler,
                       BlockedThirdPartyResourceAssembler blockAssembler,
                       IndividualRequestManagerService individualRequestManagerService){
        this.responseAssembler = responseAssembler;
        this.uploadResponseService = uploadResponseService;
        this.blockAssembler = blockAssembler;
        this.individualRequestManagerService = individualRequestManagerService;
    }

    /**
     * Add a new response to a certain request.
     *
     * @param requestingUser user accessing this method
     * @param requestID id of the request that is replied with this call
     * @param response type of response (e.g. accept the request)
     * @return an http 201 created message that contains the newly formed link
     */
    @PostMapping(Constants.NEW_RESPONSE_API)
    public @ResponseBody ResponseEntity<?> newResponse(@RequestHeader(value = Constants.HEADER_USER_SSN) String requestingUser,
                                                       @PathVariable Long requestID,
                                                       @RequestBody String response) {
        if(!requestingUser.equals(individualRequestManagerService.getRequestById(requestID).getUser().getSsn()))
            throw new ImpossibleAccessException();

        ResponseType responseType;

        if (response.equals(ResponseType.ACCEPT.toString())) {
            responseType = ResponseType.ACCEPT;
        } else if (response.equals(ResponseType.REFUSE.toString())) {
            responseType = ResponseType.REFUSE;
        } else {
            throw new BadResponseTypeException(response);
        }

        Response newResponse = uploadResponseService.addResponse(requestID, responseType, new User(requestingUser));
        Resource<Response> resource = responseAssembler.toResource(newResponse);
        if(responseType == ResponseType.REFUSE)
            resource.add(linkTo(methodOn(ResponseController.class).blockThirdParty(requestingUser,
                    newResponse.getRequest().getThirdParty().getId())).withRel(Constants.REL_BLOCK_TP));

        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    /**
     * Add a block on a certain third party customer for a specific user
     *
     * @param requestingUser user accessing the method
     * @param thirdParty identified of the third party that will be blocked
     * @return an http 201 created message that contains the newly formed link
     */
    @PostMapping(Constants.NEW_BLOCK_API)
    public @ResponseBody ResponseEntity<?> blockThirdParty(@RequestHeader(value = Constants.HEADER_USER_SSN) String requestingUser,
                                                           @PathVariable Long thirdParty) {
        BlockedThirdParty blockedThirdParty = uploadResponseService.addBlock(new User(requestingUser), thirdParty);
        Resource<BlockedThirdParty> resource = blockAssembler.toResource(blockedThirdParty);
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }
}
