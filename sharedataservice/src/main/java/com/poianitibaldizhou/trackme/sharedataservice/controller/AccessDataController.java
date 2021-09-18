package com.poianitibaldizhou.trackme.sharedataservice.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.poianitibaldizhou.trackme.sharedataservice.assembler.AggregatedDataResourceAssembler;
import com.poianitibaldizhou.trackme.sharedataservice.service.AccessDataService;
import com.poianitibaldizhou.trackme.sharedataservice.util.AggregatedData;
import com.poianitibaldizhou.trackme.sharedataservice.util.Constants;
import com.poianitibaldizhou.trackme.sharedataservice.util.DataWrapper;
import com.poianitibaldizhou.trackme.sharedataservice.util.Views;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Entry point for accessing services regarding the access of data
 */
@RestController
@RequestMapping(Constants.ACCESS_DATA_API)
public class AccessDataController {

    private AccessDataService accessDataService;

    private AggregatedDataResourceAssembler aggregatedDataResourceAssembler;

    public AccessDataController(AccessDataService accessDataService,
                                AggregatedDataResourceAssembler aggregatedDataResourceAssembler) {
        this.accessDataService = accessDataService;
        this.aggregatedDataResourceAssembler = aggregatedDataResourceAssembler;
    }

    /**
     * Retrieves all the health and position data of the user defined inside the individual request
     *
     * @param requestingThirdPartyId id of the third party customer that is trying to access this method
     * @param individualRequestId the id of the individual request made by the third party on a certain user
     * @return a OK http response with all the data defined in the individual request
     */
    @JsonView(Views.Public.class)
    @GetMapping(Constants.GET_INDIVIDUAL_REQUEST_DATA_API)
    public @ResponseBody Resource<DataWrapper> getIndividualRequestData(@RequestHeader(value = Constants.HEADER_THIRD_PARTY_ID) String requestingThirdPartyId,
                                                                        @PathVariable(name = Constants.REQUEST_ID) Long individualRequestId){
        return new Resource<>(accessDataService.getIndividualRequestData(Long.parseLong(requestingThirdPartyId), individualRequestId),
                linkTo(methodOn(AccessDataController.class).getIndividualRequestData(requestingThirdPartyId, individualRequestId)).withSelfRel());
    }

    /**
     * Retrieves the aggregated data requested by the third party {third_party_id} with the group request {request_id}
     *
     * @param requestingThirdPartyId id of the third party customer that is trying to access this method
     * @param groupRequestId the id of the group request regarding the data
     * @return a OK http response with the aggregated data requested
     */
    @JsonView(Views.Public.class)
    @GetMapping(Constants.GET_GROUP_REQUEST_DATA_API)
    public @ResponseBody Resource<AggregatedData> getGroupRequestData(@RequestHeader(value=Constants.HEADER_THIRD_PARTY_ID) String requestingThirdPartyId,
                                                                      @PathVariable(name = Constants.REQUEST_ID) Long groupRequestId){
        return aggregatedDataResourceAssembler.toResource(
                accessDataService.getGroupRequestData(Long.parseLong(requestingThirdPartyId), groupRequestId));
    }

    /**
     * Retrieves all the health and position data of the user {user_id} between two dates: from and to
     * @param requestingUser ssn of the user that is trying to access this method
     * @param from the lower bound of data
     * @param to the upper bound of data
     * @return a OK http response with all the data of the user between the two dates
     */
    @JsonView(Views.Public.class)
    @GetMapping(Constants.GET_OWN_DATA_API)
    public @ResponseBody Resource<DataWrapper> getOwnData(@RequestHeader(value = Constants.HEADER_USER_SSN) String requestingUser,
                                                          @RequestParam(name = Constants.DATE_FROM) Date from,
                                                          @RequestParam(name = Constants.DATE_TO) Date to){
        return new Resource<>(accessDataService.getOwnData(requestingUser, from, to),
                linkTo(methodOn(AccessDataController.class).getOwnData(requestingUser, from, to)).withSelfRel());
    }
}
