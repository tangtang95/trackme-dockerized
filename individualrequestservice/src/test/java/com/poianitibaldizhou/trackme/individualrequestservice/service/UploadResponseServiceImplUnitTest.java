package com.poianitibaldizhou.trackme.individualrequestservice.service;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.*;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.*;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.*;
import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestStatus;
import com.poianitibaldizhou.trackme.individualrequestservice.util.ResponseType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit test for the uploading response service
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UploadResponseServiceImplUnitTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BlockedThirdPartyRepository blockedThirdPartyRepository;

    @MockBean
    private IndividualRequestRepository individualRequestRepository;

    @MockBean
    private ResponseRepository responseRepository;

    @MockBean
    private ThirdPartyRepository thirdPartyRepository;

    private UploadResponseServiceImpl uploadResponseService;

    @Before
    public void setUp() {
        setUpUserRepository();
        setUpThirdPartyRepository();
        setUpRequestRepository();
        setUpBlockRepository();
        setUpResponseRepository();

        uploadResponseService = new UploadResponseServiceImpl(userRepository, blockedThirdPartyRepository, individualRequestRepository,
                responseRepository, thirdPartyRepository);
    }

    private void setUpThirdPartyRepository() {
        when(thirdPartyRepository.findById(2L)).thenReturn(Optional.of(new ThirdParty(2L, "thirdParty2")));
        when(thirdPartyRepository.findById(1L)).thenReturn(Optional.of(new ThirdParty(1L, "thirdParty1")));
        when(thirdPartyRepository.findById(3L)).thenReturn(Optional.of(new ThirdParty(3L, "thirdParty3")));
        when(thirdPartyRepository.findById(4L)).thenReturn(Optional.of(new ThirdParty(4L, "thirdParty4")));
    }

    private void setUpResponseRepository() {
        ThirdParty thirdParty = new ThirdParty();
        thirdParty.setId(2L);
        thirdParty.setIdentifierName("thirdParty2");
        IndividualRequest request2 = new IndividualRequest(
                new Timestamp(10000), new Date(10000), new Date(10000), new User("user1"), thirdParty);
        request2.setId((long) 2);

        Response response = new Response();
        //response.setRequestID((long) 2);
        response.setRequest(request2);
        response.setResponse(ResponseType.REFUSE);
        response.setAcceptanceTimeStamp(new Timestamp(0));

        when(responseRepository.findById((long) 2)).thenReturn(java.util.Optional.of(response));
    }

    private void setUpRequestRepository() {
        ThirdParty thirdParty1 = new ThirdParty();
        thirdParty1.setId(1L);
        thirdParty1.setIdentifierName("thirdParty1");
        IndividualRequest request1 = new IndividualRequest(
                new Timestamp(0), new Date(0), new Date(0), new User("user1"), thirdParty1);
        request1.setId((long) 1);

        ThirdParty thirdParty2 = new ThirdParty();
        thirdParty2.setId(2L);
        thirdParty2.setIdentifierName("thirdParty2");
        IndividualRequest request2 = new IndividualRequest(
                new Timestamp(10000), new Date(10000), new Date(10000), new User("user1"), thirdParty2);
        request2.setId((long) 2);
        request2.setStatus(IndividualRequestStatus.REFUSED);

        ThirdParty thirdParty3 = new ThirdParty();
        thirdParty3.setId(3L);
        thirdParty3.setIdentifierName("thirdParty3");
        IndividualRequest request3 = new IndividualRequest(
                new Timestamp(0), new Date(0), new Date(0), new User("user3"), thirdParty3);
        request3.setId((long) 3);

        ThirdParty thirdParty4 = new ThirdParty();
        thirdParty4.setId(1L);
        thirdParty4.setIdentifierName("thirdParty4");
        IndividualRequest request4 = new IndividualRequest(new Timestamp(0), new Date(0), new Date(0), new User("user4"), thirdParty4);
        request4.setId((long) 4);
        request4.setStatus(IndividualRequestStatus.REFUSED);

        when(individualRequestRepository.findById((long)1)).thenReturn(java.util.Optional.of(request1));
        when(individualRequestRepository.findById((long)2)).thenReturn(java.util.Optional.of(request2));
        when(individualRequestRepository.findById((long)3)).thenReturn(java.util.Optional.of(request3));
        when(individualRequestRepository.findById((long)4)).thenReturn(java.util.Optional.of(request4));

        List<IndividualRequest> list = new ArrayList<>();
        list.add(request1);
        list.add(request4);

        when(individualRequestRepository.findAllByThirdParty_Id((long) 1)).thenReturn(list);
        when(individualRequestRepository.findAllByThirdParty_Id((long) 2)).thenReturn(Collections.singletonList(request2));
        when(individualRequestRepository.findAllByThirdParty_Id((long) 3)).thenReturn(Collections.singletonList(request3));
    }

    private void setUpBlockRepository() {
        ThirdParty thirdParty = new ThirdParty();
        thirdParty.setId(2L);
        thirdParty.setIdentifierName("thirdParty2");
        BlockedThirdPartyKey blockedThirdPartyKey = new BlockedThirdPartyKey(thirdParty, new User("user1"));
        BlockedThirdParty blockedThirdParty = new BlockedThirdParty();
        blockedThirdParty.setKey(blockedThirdPartyKey);
        blockedThirdParty.setBlockDate(new Date(0));

        when(blockedThirdPartyRepository.findById(blockedThirdPartyKey)).thenReturn(java.util.Optional.of(blockedThirdParty));
    }

    /**
     * Set up the user repository, with three users
     */
    private void setUpUserRepository() {
        User user = new User();
        user.setSsn("user1");
        when(userRepository.findById("user1")).thenReturn(java.util.Optional.of(user));

        user = new User();
        user.setSsn("user2");
        when(userRepository.findById("user2")).thenReturn(java.util.Optional.of(user));

        user = new User();
        user.setSsn("user3");
        when(userRepository.findById("user3")).thenReturn(java.util.Optional.of(user));

        user = new User();
        user.setSsn("user4");
        when(userRepository.findById("user4")).thenReturn(java.util.Optional.of(user));
    }
    

    @After
    public void tearDown() {
        uploadResponseService = null;
    }

    /**
     * Test the add of a response when everything is fine, that means that the user is registered, that a request
     * to that user is present, that the user calling the service is matched with the one of the request and that
     * no response is already present
     */
    @Test
    public void testAddResponseWithAccept() throws Exception{
        uploadResponseService.addResponse((long) 1, ResponseType.ACCEPT, new User("user1"));

        assertEquals(IndividualRequestStatus.ACCEPTED, individualRequestRepository.findById(1L).orElseThrow(Exception::new).getStatus());
        verify(responseRepository, times(1)).saveAndFlush(any(Response.class));
    }

    /**
     * Test the add of a response when everything is fine, that means that the user is registered, that a request
     * to that user is present, that the user calling the service is matched with the one of the request and that
     * no response is already present
     */
    @Test
    public void testAddResponseWithRefuse() throws Exception{
        uploadResponseService.addResponse((long) 1, ResponseType.REFUSE, new User("user1"));

        assertEquals(IndividualRequestStatus.REFUSED, individualRequestRepository.findById(1L).orElseThrow(Exception::new).getStatus());
        verify(responseRepository, times(1)).saveAndFlush(any(Response.class));
    }

    /**
     * Test the add of a response when it is performed by a non existing user
     */
    @Test(expected = UserNotFoundException.class)
    public void testAddResponseNonExistingUser() {
        uploadResponseService.addResponse((long) 1, ResponseType.ACCEPT, new User("notExistingUser"));
    }

    /**
     * Test the add of a response when it is performed on a non existing request
     */
    @Test(expected = RequestNotFoundException.class)
    public void testAddResponseNonExistingRequest() {
        uploadResponseService.addResponse((long)100, ResponseType.REFUSE, new User("user1"));
    }

    /**
     * Test the add of a response when a response for that request is already present
     */
    @Test(expected = ResponseAlreadyPresentException.class)
    public void testAddResponseWhenResponseAlreadyPresent() {
        uploadResponseService.addResponse((long)2, ResponseType.REFUSE, new User("user2"));
    }

    /**
     * Test the add of a response when the user who is responding is not the one related with the request
     */
    @Test(expected = NonMatchingUserException.class)
    public void testAddResponseNonMatchingUser() {
        uploadResponseService.addResponse((long) 1, ResponseType.REFUSE, new User("user2"));
    }

    /**
     * Test the block when the user is not registered
     */
    @Test(expected = UserNotFoundException.class)
    public void testBlockOfNonExistingUser() {
        uploadResponseService.addBlock(new User("nonExistingUser"), (long) 1);
    }

    /**
     * Test the block when no third party has performed request toward that user
     */
    @Test(expected = ThirdPartyNotFoundException.class)
    public void testBlockWhenNoThirdHasPerformedRequestToThatUser() {
        uploadResponseService.addBlock(new User("user1"), (long)3);
    }

    /**
     * Test the block when the third party is already blocked from that user
     */
    @Test(expected = BlockAlreadyPerformedException.class)
    public void testBlockWhenAlreadyPerformed() {
        uploadResponseService.addBlock(new User("user1"), (long)2);
    }

    /**
     * Test the block when the block is inserted successfully
     */
    @Test
    public void testBlock() throws Exception{
        List<IndividualRequest> requestList = individualRequestRepository.findAllByThirdParty_Id(1L);
        List<Long> idOfPendingRequest = requestList.stream().
                filter(individualRequest -> individualRequest.getStatus().equals(IndividualRequestStatus.PENDING))
                .map(IndividualRequest::getId).collect(Collectors.toList());

        uploadResponseService.addBlock(new User("user4"), (long)1);

        ThirdParty thirdParty = new ThirdParty();
        thirdParty.setId(1L);
        thirdParty.setIdentifierName("thirdParty1");
        BlockedThirdPartyKey blockedThirdPartyKey = new BlockedThirdPartyKey(thirdParty, new User("user4"));
        BlockedThirdParty blockedThirdParty = new BlockedThirdParty();
        blockedThirdParty.setKey(blockedThirdPartyKey);
        blockedThirdParty.setBlockDate(Date.valueOf(LocalDate.now()));

        verify(blockedThirdPartyRepository, times(1)).saveAndFlush(blockedThirdParty);

        for(Long id : idOfPendingRequest) {
            if(individualRequestRepository.findById(id).orElseThrow(Exception::new).getUser().equals(new User("user4")))
                assertEquals(IndividualRequestStatus.REFUSED, individualRequestRepository.findById(id).orElseThrow(Exception::new).getStatus());
        }
    }
}

