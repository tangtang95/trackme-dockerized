package com.poianitibaldizhou.trackme.sharedataservice.controller;

import com.poianitibaldizhou.trackme.sharedataservice.assembler.AggregatedDataResourceAssembler;
import com.poianitibaldizhou.trackme.sharedataservice.entity.HealthData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.PositionData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.User;
import com.poianitibaldizhou.trackme.sharedataservice.exception.GroupRequestNotFoundException;
import com.poianitibaldizhou.trackme.sharedataservice.exception.IndividualRequestNotFoundException;
import com.poianitibaldizhou.trackme.sharedataservice.exception.UserNotFoundException;
import com.poianitibaldizhou.trackme.sharedataservice.service.AccessDataService;
import com.poianitibaldizhou.trackme.sharedataservice.util.AggregatedData;
import com.poianitibaldizhou.trackme.sharedataservice.util.Constants;
import com.poianitibaldizhou.trackme.sharedataservice.util.DataWrapper;
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
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AccessDataController.class)
@ActiveProfiles(profiles = {"test"})
@Import({AggregatedDataResourceAssembler.class})
public class AccessDataControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccessDataService service;

    private static final Long THIRD_PARTY_ID = 1L;
    private static final Long GROUP_REQUEST_ID = 3L;
    private static final Long INDIVIDUAL_REQUEST_ID = 2L;
    private static final String USER_ID = "user1";

    /**
     * Test get individual request data when it will return a data wrapper
     * @throws Exception no exception expected
     */
    @Test
    public void getIndividualRequestDataSuccessful() throws Exception {
        DataWrapper dataWrapper = new DataWrapper();
        List<HealthData> healthDataList = new ArrayList<>();
        healthDataList.add(HealthData.newHealthData(1L, new Timestamp(0), new User(), 3, 1, 1, 1));
        healthDataList.add(HealthData.newHealthData(2L, new Timestamp(0), new User(), 4, 1, 1, 1));
        List<PositionData> positionDataList = new ArrayList<>();
        positionDataList.add(PositionData.newPositionData(1L, new Timestamp(0), new User(), 1.0, 1.0));
        positionDataList.add(PositionData.newPositionData(2L, new Timestamp(0), new User(), 3.0, 2.0));
        dataWrapper.setHealthDataList(healthDataList);
        dataWrapper.setPositionDataList(positionDataList);

        given(service.getIndividualRequestData(THIRD_PARTY_ID, INDIVIDUAL_REQUEST_ID)).willReturn(dataWrapper);

        mvc.perform(get("/dataretrieval/individualrequests/" + INDIVIDUAL_REQUEST_ID)
                .accept(MediaTypes.HAL_JSON_VALUE).header(Constants.HEADER_THIRD_PARTY_ID, THIRD_PARTY_ID.toString()))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.positionDataList", hasSize(2)))
                .andExpect(jsonPath("$.positionDataList[*].timestamp", containsInAnyOrder("1970-01-01T00:00:00.000+0000", "1970-01-01T00:00:00.000+0000")))
                .andExpect(jsonPath("$.positionDataList[*].latitude", containsInAnyOrder(1.0, 3.0)))
                .andExpect(jsonPath("$.positionDataList[*].longitude", containsInAnyOrder(1.0, 2.0)))
                .andExpect(jsonPath("$.healthDataList", hasSize(2)))
                .andExpect(jsonPath("$.healthDataList[*].timestamp",
                        containsInAnyOrder("1970-01-01T00:00:00.000+0000", "1970-01-01T00:00:00.000+0000")))
                .andExpect(jsonPath("$.healthDataList[*].heartBeat",
                        containsInAnyOrder(3, 4)))
                .andExpect(jsonPath("$.healthDataList[*].pressureMin",
                        containsInAnyOrder(1, 1)))
                .andExpect(jsonPath("$.healthDataList[*].pressureMax",
                        containsInAnyOrder(1, 1)))
                .andExpect(jsonPath("$.healthDataList[*].bloodOxygenLevel",
                        containsInAnyOrder(1, 1)))
                .andExpect(jsonPath("$._links.self.href",
                        is("http://localhost/dataretrieval/individualrequests/" + INDIVIDUAL_REQUEST_ID)));
    }

    /**
     * Test get individual request data when the service throw an individual request not found exception
     * @throws Exception IndividualRequestNotFoundException
     */
    @Test
    public void getIndividualRequestDataWhenThrowException() throws Exception {
        given(service.getIndividualRequestData(THIRD_PARTY_ID, INDIVIDUAL_REQUEST_ID))
                .willThrow(new IndividualRequestNotFoundException(INDIVIDUAL_REQUEST_ID));

        mvc.perform(get("/dataretrieval/individualrequests/" + INDIVIDUAL_REQUEST_ID)
                .header(Constants.HEADER_THIRD_PARTY_ID, THIRD_PARTY_ID.toString()))
                .andExpect(status().isNotFound());
    }

    /**
     * Test get group request data when the service return a value
     * @throws Exception no exception expected
     */
    @Test
    public void getGroupRequestDataSuccessful() throws Exception {
        AggregatedData output = AggregatedData.newAggregatedData(THIRD_PARTY_ID, GROUP_REQUEST_ID, 2.0,
                new Timestamp(0));

        given(service.getGroupRequestData(THIRD_PARTY_ID, GROUP_REQUEST_ID)).willReturn(output);

        mvc.perform(get("/dataretrieval/grouprequests/" + GROUP_REQUEST_ID).
                header("thirdPartyId", THIRD_PARTY_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value", is(2.0)))
                .andExpect(jsonPath("$.generatedTimestamp", is("1970-01-01T00:00:00.000+0000")))
                .andExpect(jsonPath("$._links.self.href",
                        is("http://localhost/dataretrieval/grouprequests/" + GROUP_REQUEST_ID)));
    }

    /**
     * Test get group request data when the service throw a group request not found exception
     * @throws Exception GroupRequestNotFoundException
     */
    @Test
    public void getGroupRequestDataWhenThrowException() throws Exception {
        given(service.getGroupRequestData(THIRD_PARTY_ID, GROUP_REQUEST_ID))
                .willThrow(new GroupRequestNotFoundException(GROUP_REQUEST_ID));

        mvc.perform(get("/dataretrieval/grouprequests/" + GROUP_REQUEST_ID)
                .header(Constants.HEADER_THIRD_PARTY_ID, THIRD_PARTY_ID.toString()))
                .andExpect(status().isNotFound());
    }


    /**
     * Test get own data when the it will return a data wrapper
     *
     * @throws Exception no exception expected
     */
    @Test
    public void getOwnDataSuccessful() throws Exception {
        DataWrapper dataWrapper = new DataWrapper();
        List<HealthData> healthDataList = new ArrayList<>();
        healthDataList.add(HealthData.newHealthData(1L, new Timestamp(0), new User(), 3, 1, 1, 1));
        healthDataList.add(HealthData.newHealthData(2L, new Timestamp(0), new User(), 4, 1, 1, 1));
        List<PositionData> positionDataList = new ArrayList<>();
        positionDataList.add(PositionData.newPositionData(1L, new Timestamp(0), new User(), 1.0, 1.0));
        positionDataList.add(PositionData.newPositionData(2L, new Timestamp(0), new User(), 3.0, 2.0));
        dataWrapper.setHealthDataList(healthDataList);
        dataWrapper.setPositionDataList(positionDataList);

        given(service.getOwnData(USER_ID, Date.valueOf("1970-01-01"), Date.valueOf("1970-01-02"))).willReturn(dataWrapper);

        mvc.perform(get("/dataretrieval/users?from=1970-01-01&to=1970-01-02")
                .header(Constants.HEADER_USER_SSN, USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.positionDataList", hasSize(2)))
                .andExpect(jsonPath("$.positionDataList[*].timestamp", containsInAnyOrder("1970-01-01T00:00:00.000+0000", "1970-01-01T00:00:00.000+0000")))
                .andExpect(jsonPath("$.positionDataList[*].latitude", containsInAnyOrder(1.0, 3.0)))
                .andExpect(jsonPath("$.positionDataList[*].longitude", containsInAnyOrder(1.0, 2.0)))
                .andExpect(jsonPath("$.healthDataList", hasSize(2)))
                .andExpect(jsonPath("$.healthDataList[*].timestamp",
                        containsInAnyOrder("1970-01-01T00:00:00.000+0000", "1970-01-01T00:00:00.000+0000")))
                .andExpect(jsonPath("$.healthDataList[*].heartBeat",
                        containsInAnyOrder(3, 4)))
                .andExpect(jsonPath("$.healthDataList[*].pressureMin",
                        containsInAnyOrder(1, 1)))
                .andExpect(jsonPath("$.healthDataList[*].pressureMax",
                        containsInAnyOrder(1, 1)))
                .andExpect(jsonPath("$.healthDataList[*].bloodOxygenLevel",
                        containsInAnyOrder(1, 1)))
                .andExpect(jsonPath("$._links.self.href",
                        is("http://localhost/dataretrieval/users?from=1970-01-01&to=1970-01-02")));
    }

    /**
     * Test get own data when the service launch an exception
     *
     * @throws Exception UserNotFoundException
     */
    @Test
    public void getOwnDataWhenThrowException() throws Exception{
        given(service.getOwnData("user2", Date.valueOf("1970-01-01"), Date.valueOf("1970-01-02")))
                .willThrow(new UserNotFoundException("user2"));

        mvc.perform(get("/dataretrieval/users/user2?from=1970-01-01&to=1970-01-02").header(Constants.HEADER_USER_SSN, "user2"))
                .andExpect(status().isNotFound());

    }

}