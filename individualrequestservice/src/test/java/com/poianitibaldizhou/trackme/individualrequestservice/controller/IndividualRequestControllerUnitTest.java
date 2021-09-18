package com.poianitibaldizhou.trackme.individualrequestservice.controller;

import com.poianitibaldizhou.trackme.individualrequestservice.assembler.IndividualRequestWrapperResourceAssembler;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.ThirdParty;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.User;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.RequestNotFoundException;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.UserNotFoundException;
import com.poianitibaldizhou.trackme.individualrequestservice.service.IndividualRequestManagerService;
import com.poianitibaldizhou.trackme.individualrequestservice.util.Constants;
import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Individual test of the controller
 */
@RunWith(SpringRunner.class)
@WebMvcTest(IndividualRequestController.class)
@Import({IndividualRequestWrapperResourceAssembler.class})
@ActiveProfiles("test")
public class IndividualRequestControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IndividualRequestManagerService service;

    /**
     * Test the retrieval of the pending request of a certain user, when this list is non empty (in particular,
     * size 1)
     *
     * @throws Exception exception due to mock mvc method get
     */
    @Test
    public void getUserRequestTest() throws Exception {
        ThirdParty thirdParty = new ThirdParty();
        thirdParty.setId(2L);
        thirdParty.setIdentifierName("thirdParty2");
        IndividualRequest request = new IndividualRequest(new Timestamp(0), new Date(0), new Date(0), new User("user1"), thirdParty);
        request.setId((long) 3);

        List<IndividualRequest> allRequests = Collections.singletonList(request);
        given(service.getUserPendingRequests(new User("user1"))).willReturn(allRequests);

        mvc.perform(get(Constants.REQUEST_API + Constants.PENDING_REQUEST_BY_USER_API).accept(MediaTypes.HAL_JSON_VALUE).
                header(Constants.HEADER_USER_SSN, "user1"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.individualRequestWrapperList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.individualRequestWrapperList[0].thirdPartyName", is("thirdParty2")))
                .andExpect(jsonPath("$._embedded.individualRequestWrapperList[0].status", is(IndividualRequestStatus.PENDING.toString())))
                .andExpect(jsonPath("$._embedded.individualRequestWrapperList[0].timestamp", is("1970-01-01T00:00:00.000+0000")))
                .andExpect(jsonPath("$._embedded.individualRequestWrapperList[0].startDate", is(new Date(0).toString())))
                .andExpect(jsonPath("$._embedded.individualRequestWrapperList[0].endDate", is(new Date(0).toString())))
                .andExpect(jsonPath("$._embedded.individualRequestWrapperList[0]._links.self.href",
                        is("http://localhost"+Constants.REQUEST_API+"/id/3")))
                .andExpect(jsonPath("$._links.self.href",
                        is("http://localhost"+Constants.REQUEST_API+Constants.PENDING_REQUEST_BY_USER_API)));
    }

    /**
     * Test the retrieval of the pending request of a certain user, when this list is empty
     *
     * @throws Exception exception due to mock mvc method get
     */
    @Test
    public void getUserRequestTestWhenTheListIsEmpty() throws Exception {
        mvc.perform(get(Constants.REQUEST_API+ Constants.PENDING_REQUEST_BY_USER_API).accept(MediaTypes.HAL_JSON_VALUE)
                .header(Constants.HEADER_USER_SSN, "user1"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href",
                        is("http://localhost"+Constants.REQUEST_API+Constants.PENDING_REQUEST_BY_USER_API)));
    }

    /**
     * Test the retrieval of the pending request of a certain user, when the user is not registered into the system
     *
     * @throws Exception exception due to mock mvc method get
     */
    @Test
    public void getUserRequestTestWhenUserNotRegistered() throws Exception {
        given(service.getUserPendingRequests(new User("user1"))).willThrow(new UserNotFoundException(new User("user1")));

        mvc.perform(get(Constants.REQUEST_API + Constants.PENDING_REQUEST_BY_USER_API)
                .accept(MediaTypes.HAL_JSON_VALUE).header(Constants.HEADER_USER_SSN, "user1"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test the access to the rest api provided by the IndividualRequest controller.
     * In particular this method tests the retrieval of requests performed by a certain third party customer
     * when the set of his requests is non-empty
     *
     * @throws Exception exception to mock mvc method perform
     */
    @Test
    public void getThirdPartyRequestTest() throws Exception {
        ThirdParty thirdParty = new ThirdParty();
        thirdParty.setId(1L);
        thirdParty.setIdentifierName("thirdParty1");
        IndividualRequest request = new IndividualRequest(new Timestamp(0), new Date(0), new Date(0), new User("user1"), thirdParty);
        request.setId((long)1);

        List<IndividualRequest> allRequests = Collections.singletonList(request);
        given(service.getThirdPartyRequests((long) 1)).willReturn(allRequests);

        mvc.perform(get(Constants.REQUEST_API + Constants.REQUEST_BY_THIRD_PARTY_ID).accept(MediaTypes.HAL_JSON_VALUE)
                .header(Constants.HEADER_THIRD_PARTY_ID, "1"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.individualRequestWrapperList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.individualRequestWrapperList[0].thirdPartyName", is("thirdParty1")))
                .andExpect(jsonPath("$._embedded.individualRequestWrapperList[0].status", is(IndividualRequestStatus.PENDING.toString())))
                .andExpect(jsonPath("$._embedded.individualRequestWrapperList[0].timestamp", is("1970-01-01T00:00:00.000+0000")))
                .andExpect(jsonPath("$._embedded.individualRequestWrapperList[0].startDate", is(new Date(0).toString())))
                .andExpect(jsonPath("$._embedded.individualRequestWrapperList[0].endDate", is(new Date(0).toString())))
                .andExpect(jsonPath("$._embedded.individualRequestWrapperList[0]._links.self.href",
                        is("http://localhost"+Constants.REQUEST_API+"/id/1")))
                .andExpect(jsonPath("$._links.self.href",
                        is("http://localhost"+Constants.REQUEST_API+Constants.REQUEST_BY_THIRD_PARTY_ID)));
    }

    /**
     * Test the access to the rest api provided by the IndividualRequestController.
     * In particular this method tests the retrieval of requests performed by a certain third party customer
     * when the set of his requests is empty
     *
     * @throws Exception exception due to mock mvc method perform
     */
    @Test
    public void getThirdPartyRequestWhenNoRequestPerformedTest() throws Exception {
        mvc.perform(get(Constants.REQUEST_API + Constants.REQUEST_BY_THIRD_PARTY_ID)
                .accept(MediaTypes.HAL_JSON_VALUE).header(Constants.HEADER_THIRD_PARTY_ID, "2"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href",
                        is("http://localhost"+Constants.REQUEST_API+Constants.REQUEST_BY_THIRD_PARTY_ID)));
    }

    /**
     * Test the access to the rest api provided by the IndividualRequestController.
     * In particular, this method tests the retrieval of requests with a certain id, when that requests is
     * not present
     *
     * @throws Exception exception due to mock mvc method perform
     */
    @Test
    public void getRequestByIdTestWhenRequestNotFound() throws Exception {
        given(service.getRequestById((long) 1)).willThrow(new RequestNotFoundException((long)1));

        mvc.perform(get(Constants.REQUEST_API+"/id/1").accept(MediaTypes.HAL_JSON_VALUE)
                .header(Constants.HEADER_THIRD_PARTY_ID, "1")
        .header(Constants.HEADER_USER_SSN, "user1"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test the access to the rest api provided by the IndividualRequestController.
     * In particular, this method tests the retrieval of requests with a certain id, when that requests is
     * present. A user is accessing the method in this case.
     *
     * @throws Exception exception due to mock mvc method perform
     */
    @Test
    public void getRequestByIdTestWhenUser() throws Exception {
        ThirdParty thirdParty = new ThirdParty();
        thirdParty.setId(1L);
        thirdParty.setIdentifierName("thirdParty");
        IndividualRequest request = new IndividualRequest(new Timestamp(0), new Date(0), new Date(0), new User("user1"), thirdParty);
        request.setId((long)1);

        given(service.getRequestById((long) 1)).willReturn(request);

        mvc.perform(get(Constants.REQUEST_API+"/id/1").accept(MediaTypes.HAL_JSON_VALUE)
                .header(Constants.HEADER_USER_SSN, "user1")
                .header(Constants.HEADER_THIRD_PARTY_ID, ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(request.getStatus().toString())))
                //.andExpect(jsonPath("$.timestamp", is(request.getTimestamp().toString())))
                .andExpect(jsonPath("$.startDate", is(request.getStartDate().toString())))
                .andExpect(jsonPath("$.endDate", is(request.getEndDate().toString())))
                .andExpect(jsonPath("$.thirdPartyName", is(request.getThirdParty().getIdentifierName())))
                .andExpect(jsonPath("$._links.self.href",
                        is("http://localhost"+Constants.REQUEST_API+"/id/1")))
                .andExpect(jsonPath("$._links.addResponse.href",
                        is("http://localhost" + Constants.RESPONSE_API + "/requests/1")));
    }

    /**
     * Test the access to the rest api provided by the IndividualRequestController.
     * In particular, this method tests the retrieval of requests with a certain id, when that accepted request is
     * present. A third party is accessing the method in this case.
     *
     * @throws Exception exception due to mock mvc method perform
     */
    @Test
    public void getAcceptedRequestByIdTestWhenTp() throws Exception {
        ThirdParty thirdParty = new ThirdParty();
        thirdParty.setId(1L);
        thirdParty.setIdentifierName("thirdParty");
        IndividualRequest request = new IndividualRequest(new Timestamp(0), new Date(0), new Date(0), new User("user1"), thirdParty);
        request.setId((long)1);
        request.setStatus(IndividualRequestStatus.ACCEPTED);

        given(service.getRequestById((long) 1)).willReturn(request);

        mvc.perform(get(Constants.REQUEST_API+"/id/1").accept(MediaTypes.HAL_JSON_VALUE)
                .header(Constants.HEADER_USER_SSN, "")
                .header(Constants.HEADER_THIRD_PARTY_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(request.getStatus().toString())))
                .andExpect(jsonPath("$.startDate", is(request.getStartDate().toString())))
                .andExpect(jsonPath("$.endDate", is(request.getEndDate().toString())))
                .andExpect(jsonPath("$.thirdPartyName", is(request.getThirdParty().getIdentifierName())))
                .andExpect(jsonPath("$._links.self.href",
                        is("http://localhost"+Constants.REQUEST_API+"/id/1")))
                .andExpect(jsonPath("$._links.accessData.href",
                        is(Constants.FAKE_URL + "/sharedataservice/dataretrieval/individualrequests/1")));

    }

    /**
     * Test the access to the rest api provided by the IndividualRequestController.
     * In particular, this method tests the retrieval of requests with a certain id, when that pending request is
     * present. A third party is accessing the method in this case.
     *
     * @throws Exception exception due to mock mvc method perform
     */
    @Test
    public void getPendingRequestByIdTestWhenTp() throws Exception {
        ThirdParty thirdParty = new ThirdParty();
        thirdParty.setId(1L);
        thirdParty.setIdentifierName("thirdParty");
        IndividualRequest request = new IndividualRequest(new Timestamp(0), new Date(0), new Date(0), new User("user1"), thirdParty);
        request.setId((long)1);
        request.setStatus(IndividualRequestStatus.PENDING);

        given(service.getRequestById((long) 1)).willReturn(request);

        mvc.perform(get(Constants.REQUEST_API+"/id/1").accept(MediaTypes.HAL_JSON_VALUE)
                .header(Constants.HEADER_USER_SSN, "")
                .header(Constants.HEADER_THIRD_PARTY_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(request.getStatus().toString())))
                .andExpect(jsonPath("$.startDate", is(request.getStartDate().toString())))
                .andExpect(jsonPath("$.endDate", is(request.getEndDate().toString())))
                .andExpect(jsonPath("$.thirdPartyName", is(request.getThirdParty().getIdentifierName())))
                .andExpect(jsonPath("$._links.self.href",
                        is("http://localhost"+Constants.REQUEST_API+"/id/1")));
    }

    /**
     * Test the access to the rest api provided by the IndividualRequestController.
     * In particular, this method tests the add of a new request, when the related user exists
     *
     * @throws Exception exception due to mock mvc method perform
     */
    @Test
    public void newRequestTest() throws Exception {
        ThirdParty thirdParty = new ThirdParty();
        thirdParty.setId(1L);

        IndividualRequest request = new IndividualRequest();
        request.setUser(new User("user1"));
        request.setThirdParty(thirdParty);
        request.setStartDate(new Date(0));
        request.setEndDate(new Date(0));
        request.setId((long) 1);
        request.setStatus(IndividualRequestStatus.PENDING);
        request.setMotivation("motivation");

        ThirdParty thirdPartyMocked = new ThirdParty();
        thirdPartyMocked.setId(1L);
        thirdPartyMocked.setIdentifierName("thirdParty1");
        IndividualRequest mockedRequest = new IndividualRequest();
        mockedRequest.setUser(new User("user1"));
        mockedRequest.setThirdParty(thirdPartyMocked);
        mockedRequest.setStartDate(new Date(0));
        mockedRequest.setEndDate(new Date(0));
        mockedRequest.setId((long) 1);
        mockedRequest.setTimestamp(new Timestamp(0));
        mockedRequest.setStatus(IndividualRequestStatus.PENDING);
        mockedRequest.setMotivation("motivation");

        String json = "{\n" +
                "\t\"id\": 1,\n" +
                "\t\"status\": \"PENDING\",\n" +
                "\t\"startDate\": \"1970-01-01\",\n" +
                "\t\"endDate\": \"1970-01-01\",\n" +
                "\t\"motivation\": \"motivation\""+
                "}";

        given(service.addRequest(request)).willReturn(mockedRequest);

        mvc.perform(post(Constants.REQUEST_API+"/user1").
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").
                content(json)
                .header(Constants.HEADER_THIRD_PARTY_ID, "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is(IndividualRequestStatus.PENDING.toString())))
                .andExpect(jsonPath("$.timestamp", is("1970-01-01T00:00:00.000+0000")))
                .andExpect(jsonPath("$.startDate", is(request.getStartDate().toString())))
                .andExpect(jsonPath("$.endDate", is(request.getEndDate().toString())))
                .andExpect(jsonPath("$.thirdPartyName", is("thirdParty1")))
                .andExpect(jsonPath("$.motivation", is(request.getMotivation())))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost"+Constants.REQUEST_API+"/id/1")));
    }

    /**
     * Test the access to the rest api provided by the IndividualRequestController.
     * In particular, this method tests the add of a new request, when the related user exists
     *
     * @throws Exception exception due to mock mvc method perform
     */
    @Test
    public void newRequestTestWhenRelatedUserNotExist() throws Exception {
        given(service.addRequest(any(IndividualRequest.class))).willThrow(new UserNotFoundException(new User("user1")));

        String json = "{\n" +
                "\t\"id\": 1,\n" +
                "\t\"status\": \"PENDING\",\n" +
                "\t\"startDate\": \"1970-01-01\",\n" +
                "\t\"endDate\": \"1970-01-01\",\n" +
                "\t\"ssn\": \"user1\",\n" +
                "\t\"thirdParty\": 1\n" +
                "}";

        mvc.perform(post(Constants.REQUEST_API+"/user1")
                .contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8")
                .content(json)
                .header(Constants.HEADER_THIRD_PARTY_ID, "1"))
                .andExpect(status().isNotFound());
    }
}
