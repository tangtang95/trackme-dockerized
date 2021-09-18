package com.poianitibaldizhou.trackme.individualrequestservice.service;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.*;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.*;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.*;
import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestStatus;
import com.poianitibaldizhou.trackme.individualrequestservice.util.ResponseType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of the upload response service.
 */
@Slf4j
@Service
public class UploadResponseServiceImpl implements UploadResponseService {

    private final UserRepository userRepository;
    private final BlockedThirdPartyRepository blockedThirdPartyRepository;
    private final IndividualRequestRepository individualRequestRepository;
    private final ResponseRepository responseRepository;
    private final ThirdPartyRepository thirdPartyRepository;

    private InternalCommunicationService internalCommunicationService;

    /**
     * Creates an individual request manager service.
     * It needs some repository in order to make some operations on data (e.g. saving an uploaded response).
     *
     * @param userRepository repository regarding the user
     * @param blockedThirdPartyRepository repository regarding the third party customers that have been blocked by som
     *                                    users
     * @param individualRequestRepository repository regarding the individual request
     * @param individualResponseRepository repository regarding the responses to individual requests
     */
    public UploadResponseServiceImpl(UserRepository userRepository, BlockedThirdPartyRepository blockedThirdPartyRepository,
                                     IndividualRequestRepository individualRequestRepository,
                                     ResponseRepository individualResponseRepository,
                                     ThirdPartyRepository thirdPartyRepository) {
        this.userRepository = userRepository;
        this.blockedThirdPartyRepository = blockedThirdPartyRepository;
        this.individualRequestRepository = individualRequestRepository;
        this.responseRepository = individualResponseRepository;
        this.thirdPartyRepository = thirdPartyRepository;
    }

    public void setInternalCommunicationService(InternalCommunicationService internalCommunicationService){
        this.internalCommunicationService = internalCommunicationService;
    }

    @Transactional
    @Override
    public Response addResponse(Long requestID, ResponseType response, User user) {
        // Check that the response is a valid one
        User finalUser = user;
        user = userRepository.findById(user.getSsn()).orElseThrow(() -> new UserNotFoundException(finalUser));
        IndividualRequest request = individualRequestRepository.findById(requestID).orElseThrow(() -> new RequestNotFoundException(requestID));

        if(responseRepository.findById(requestID).isPresent())
            throw new ResponseAlreadyPresentException(requestID);

        if(individualRequestRepository.findById(requestID).map(IndividualRequest::getUser).
                filter(userRequest -> !userRequest.equals(finalUser)).isPresent()) {
            throw new NonMatchingUserException(user.getSsn());
        }

        // Save the individual response in the database
        Response individualResponse = new Response();
        individualResponse.setRequest(request);
        individualResponse.setResponse(response);
        individualResponse.setAcceptanceTimeStamp(Timestamp.valueOf(LocalDateTime.now()));

        // Update the status of the request according to the type of resposne
        if (response == ResponseType.ACCEPT) {
            request.setStatus(IndividualRequestStatus.ACCEPTED);
        } else if (response == ResponseType.REFUSE) {
            request.setStatus(IndividualRequestStatus.REFUSED);
        }

        Response savedResponse = responseRepository.saveAndFlush(individualResponse);

        if(request.getStatus().equals(IndividualRequestStatus.ACCEPTED)) {
            if (Objects.nonNull(internalCommunicationService)) {
                Objects.requireNonNull(internalCommunicationService).sendIndividualRequest(request);
            }
            else{
                log.error("FATAL ERROR: InternalCommunicationService null, maybe due to the settings of active profiles");
            }
        }

        return savedResponse;
    }

    @Transactional
    @Override
    public BlockedThirdParty addBlock(User user, Long thirdPartyID) {
        // Check that the block is valid (i.e. user registered, a request exist (and also a refused one), and no other block exists
        if(!userRepository.findById(user.getSsn()).isPresent())
            throw new UserNotFoundException(user);
        List<IndividualRequest> requestList = individualRequestRepository.findAllByThirdParty_Id(thirdPartyID);

        if(requestList.stream()
                .noneMatch(individualRequest -> individualRequest.getUser().equals(user))) {
            throw new ThirdPartyNotFoundException(thirdPartyID);
        }
        if(requestList.stream().noneMatch(individualRequest -> individualRequest.getUser().equals(user) &&
                individualRequest.getStatus().equals(IndividualRequestStatus.REFUSED))) {
            throw new ThirdPartyRefusedRequestNotFoundException(thirdPartyID);
        }

        ThirdParty thirdParty = thirdPartyRepository.findById(thirdPartyID)
                .orElseThrow(() -> new ThirdPartyNotFoundException(thirdPartyID));

        BlockedThirdPartyKey key = new BlockedThirdPartyKey(thirdParty, user);
        if(blockedThirdPartyRepository.findById(key).isPresent()) {
            throw new BlockAlreadyPerformedException(thirdPartyID);
        }

        // Save the blocked third party
        BlockedThirdParty blockedThirdParty = new BlockedThirdParty();
        blockedThirdParty.setKey(key);
        blockedThirdParty.setBlockDate(Date.valueOf(LocalDate.now()));

        // all the pending request for that user becomes refused
        individualRequestRepository.findAllByThirdParty_Id(thirdPartyID).stream().
                filter(individualRequest -> individualRequest.getStatus().equals(IndividualRequestStatus.PENDING) &&
                        individualRequest.getUser().getSsn().equals(user.getSsn())).forEach(
                individualRequest -> individualRequest.setStatus(IndividualRequestStatus.REFUSED)
        );

        return blockedThirdPartyRepository.saveAndFlush(blockedThirdParty);
    }
}
