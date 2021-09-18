package com.poianitibaldizhou.trackme.grouprequestservice.service;

import com.poianitibaldizhou.trackme.grouprequestservice.util.GroupRequestWrapper;

import java.util.List;

/**
 * Interface provided in order to offer the service that manages that group requests
 */
public interface GroupRequestManagerService {

    /**
     * Return the information related to a group request identified by a certain id
     *
     * @param id id of the group request that will be retrieved
     * @return wrapper that contains the group request with all the related filter statements
     */
    GroupRequestWrapper getById(Long id);

    /**
     * Return the information related to the group request performed by a third party customer: all the request
     * that he has performed are taken into consideration
     *
     * @param thirdPartyId information on group request performed by the third party customer identified by this
     *                     number
     * @return list of wrapper of group request performed by the specified third party customer
     */
    List<GroupRequestWrapper> getByThirdPartyId(Long thirdPartyId);

    /**
     * Add a group request with the status of under analysis
     *
     * @param groupRequestWrapper group request performed by the third party customer
     * @return group request that has been added
     */
    GroupRequestWrapper addGroupRequest(GroupRequestWrapper groupRequestWrapper);

}
