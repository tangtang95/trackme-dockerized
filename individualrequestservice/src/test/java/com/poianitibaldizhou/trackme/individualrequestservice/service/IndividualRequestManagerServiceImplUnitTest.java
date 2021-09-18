package com.poianitibaldizhou.trackme.individualrequestservice.service;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.*;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.IncompatibleDateException;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.RequestNotFoundException;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.UserNotFoundException;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.BlockedThirdPartyRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.IndividualRequestRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.ThirdPartyRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.UserRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit test for the individual request manager service
 */
@RunWith(SpringRunner.class)
public class IndividualRequestManagerServiceImplUnitTest {

    @MockBean
    private IndividualRequestRepository individualRequestRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BlockedThirdPartyRepository blockedThirdPartyRepository;

    @MockBean
    private ThirdPartyRepository thirdPartyRepository;

    private IndividualRequestManagerService requestManagerService;

    @Before
    public void setUp() {
        setUpMockUserRepo(userRepository);
        setUpMockRequestRepo(individualRequestRepository);
        setUpMockBlockedRepo(blockedThirdPartyRepository);
        setUpMockThirdPartyRepo(thirdPartyRepository);
        requestManagerService = new IndividualRequestManagerServiceImpl(individualRequestRepository,
                blockedThirdPartyRepository, userRepository, thirdPartyRepository);
    }



    @After
    public void tearDown() {
        requestManagerService = null;
    }

    /**
     * Set up the mocks for the user repository
     * @param userRepo user repository that will be mocked
     */
    private void setUpMockUserRepo(UserRepository userRepo) {
        User user = new User();
        user.setSsn("user1");
        Mockito.when(userRepo.findById("user1")).thenReturn(java.util.Optional.of(user));

        user = new User();
        user.setSsn("user3");
        Mockito.when(userRepo.findById("user3")).thenReturn(java.util.Optional.ofNullable(user));

        user = new User();
        user.setSsn("user15");
        Mockito.when(userRepo.findById("user15")).thenReturn(java.util.Optional.ofNullable(user));
    }

    /**
     * Set up the mocks for the individual request repository
     * @param requestRepo individual request repository that will be mocked
     */
    private void setUpMockRequestRepo(IndividualRequestRepository requestRepo) {
        IndividualRequest request1 = new IndividualRequest(
                new Timestamp(0), new Date(0), new Date(0), new User("user1"), new ThirdParty(1L, "thirdParty1"));
        request1.setId((long) 1);
        IndividualRequest request2 = new IndividualRequest(
                new Timestamp(10000), new Date(10000), new Date(10000), new User("user1"), new ThirdParty(1L, "thirdParty1"));
        request2.setId((long) 2);
        IndividualRequest request3 = new IndividualRequest(
                new Timestamp(0), new Date(0), new Date(0), new User("user3"), new ThirdParty(1L, "thirdParty1"));
        request3.setId((long) 3);

        List<IndividualRequest> listOfFirstTP = new ArrayList<>();
        listOfFirstTP.add(request1);
        listOfFirstTP.add(request2);

        List<IndividualRequest> listOfSecondTP = new ArrayList<>();
        listOfSecondTP.add(request3);

        Mockito.when(requestRepo.findById((long)1)).thenReturn(java.util.Optional.of(request1));
        Mockito.when(requestRepo.findById((long)2)).thenReturn(java.util.Optional.of(request2));
        Mockito.when(requestRepo.findById((long)3)).thenReturn(java.util.Optional.of(request3));

        Mockito.when(requestRepo.findAllByThirdParty_Id((long) 1)).thenReturn(listOfFirstTP);
        Mockito.when(requestRepo.findAllByThirdParty_Id((long) 2)).thenReturn(listOfSecondTP);

        List<IndividualRequest> listOfUser3 = new ArrayList<>();
        listOfUser3.add(request3);

        Mockito.when(requestRepo.findAllByUserAndStatus(new User("user3"), IndividualRequestStatus.PENDING)).thenReturn(listOfUser3);
    }

    /**
     * Set up the mocks for the blocked third party repository
     * @param blockedRepo blocked third party repository that will be mocked
     */
    private void setUpMockBlockedRepo(BlockedThirdPartyRepository blockedRepo) {
        BlockedThirdPartyKey key = new BlockedThirdPartyKey(new ThirdParty(4L, "thirdParty4"), new User("user1"));
        BlockedThirdParty blockedThirdParty = new BlockedThirdParty();
        blockedThirdParty.setKey(key);
        Mockito.when(blockedRepo.findById(key)).thenReturn(java.util.Optional.ofNullable(blockedThirdParty));
    }

    /**
     * Set up the mocks for the third party repository
     * @param thirdPartyRepository third party repository that will be mocked
     */
    private void setUpMockThirdPartyRepo(ThirdPartyRepository thirdPartyRepository) {
        when(thirdPartyRepository.findById(4L)).thenReturn(Optional.of(new ThirdParty(4L, "thirdParty4")));
    }

    /**
     * Test the getRequestById method, when a request with a certain is present in the repository
     */
    @Test
    public void getRequestByIdTest(){
        IndividualRequest individualRequest = requestManagerService.getThirdPartyRequests((long) 1).get(0);

        assertEquals(individualRequest, requestManagerService.getRequestById(individualRequest.getId()));
    }

    /**
     * Test the getRequestById method, when a request with a certain id is not present in the repository
     */
    @Test(expected = RequestNotFoundException.class)
    public void getRequestByIdWhenRequestNotPresent() {
        requestManagerService.getRequestById((long)1000);
    }

    /**
     * Test the getThirdPartyRequests method, when a request performed by a specific third party is present in the
     * repository
     */
    @Test
    public void getThirdPartyRequestsTest() {
        List<IndividualRequest> requestList = requestManagerService.getThirdPartyRequests((long) 2);
        assertEquals(1, requestList.size());
        assertEquals((long)3, (long)requestList.get(0).getId());
    }

    /**
     * Test the getThirdPartyRequests method, when no request was performed by a given third party customer
     */
    @Test
    public void getThirdPartyRequestsTestWhenRequestNotPresent() {
        assertTrue(requestManagerService.getThirdPartyRequests((long) 5).isEmpty());
    }

    /**
     * Test the add of a new request, when the user related with it is present (i.e. registered), and when
     * that user didn't block the petitioner
     */
    @Test
    public void addRequestTest() {
        IndividualRequest newRequest = new IndividualRequest(new Timestamp(0), new Date(0), new Date(0), new User("user3"),
                new ThirdParty(4L, "thirdParty4"));
        requestManagerService.addRequest(newRequest);

        verify(individualRequestRepository, times(1)).save(any(IndividualRequest.class));
    }

    /**
     * Test the add of a new request, when the user related with is not present (i.e. not registered)
     */
    @Test(expected = UserNotFoundException.class)
    public void addRequestTestWhenUserNotPresent() {
        requestManagerService.addRequest(new IndividualRequest(new Timestamp(0), new Date(0), new Date(0), new User("user4"),
                new ThirdParty(1L, "thirdParty1")));
    }

    /**
     * Test the add of a new request, when the user related with it is present (i.e. registered), but has already
     * blocked the petitioner
     */
    @Test
    public void addRequestTestWhenBlocked() {
        IndividualRequest newRequest = new IndividualRequest(new Timestamp(0), new Date(0), new Date(0), new User("user1"),
                new ThirdParty(4L, "thirdParty4"));

        requestManagerService.addRequest(newRequest);

        verify(individualRequestRepository, times(1)).save(any(IndividualRequest.class));
    }

    /**
     * Test the add of a new request when the dates specified are incompatibles
     */
    @Test(expected = IncompatibleDateException.class)
    public void addRequestIncompatibleDates() {
        IndividualRequest request = new IndividualRequest(new Timestamp(0), new Date(1), new Date(0), new User("user3"),
                new ThirdParty(4L, "thirdParty4"));

        requestManagerService.addRequest(request);
    }

    /**
     * Test the retrieval of the pending request related with a specified user, when the user is not registered
     * into the system
     */
    @Test(expected = UserNotFoundException.class)
    public void getUserPendingRequestTestWhenUserNotRegistered() {
        requestManagerService.getUserPendingRequests(new User("notPresentUser"));
    }

    /**
     * Test the retrieval of the pending request related with a specified user, when the user is registered into the
     * system but no pending requests are present
     */
    @Test
    public void getUserPendingRequestTestWhenListEmpty() {
        assertTrue(requestManagerService.getUserPendingRequests(new User("user15")).isEmpty());
    }

    /**
     * Test the retrieval of the pending request related with a specified user when the user is registered
     * and has some pending requests
     */
    @Test
    public void getUserPendingRequestTest() {
        List<IndividualRequest> requestList = requestManagerService.getUserPendingRequests(new User("user3"));
        assertEquals(1,requestList.size());
        assertEquals("user3", requestList.get(0).getUser().getSsn());
        assertEquals(IndividualRequestStatus.PENDING, requestList.get(0).getStatus());
    }
}
