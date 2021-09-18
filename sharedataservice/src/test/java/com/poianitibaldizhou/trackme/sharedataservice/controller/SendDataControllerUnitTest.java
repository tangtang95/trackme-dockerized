package com.poianitibaldizhou.trackme.sharedataservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poianitibaldizhou.trackme.sharedataservice.assembler.HealthDataResourceAssembler;
import com.poianitibaldizhou.trackme.sharedataservice.assembler.PositionDataResourceAssembler;
import com.poianitibaldizhou.trackme.sharedataservice.assembler.ResourceDataWrapperResourceAssembler;
import com.poianitibaldizhou.trackme.sharedataservice.entity.HealthData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.PositionData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.User;
import com.poianitibaldizhou.trackme.sharedataservice.exception.UserNotFoundException;
import com.poianitibaldizhou.trackme.sharedataservice.service.SendDataService;
import com.poianitibaldizhou.trackme.sharedataservice.util.Constants;
import com.poianitibaldizhou.trackme.sharedataservice.util.DataWrapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.sql.Timestamp;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SendDataController.class)
@ActiveProfiles(profiles = {"test"})
@Import({HealthDataResourceAssembler.class, PositionDataResourceAssembler.class, ResourceDataWrapperResourceAssembler.class})
public class SendDataControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SendDataService service;

    private static final String USER_1 = "user1";
    private static final String USER_NOT_FOUND = "userNotFound";
    private User user;

    @Before
    public void setUp() throws Exception {
        user = new User();
        user.setSsn(USER_1);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setBirthDate(new Date(0));
        user.setBirthCity("city");
        user.setBirthNation("nation");
    }

    @After
    public void tearDown() throws Exception {
        user = null;
    }

    /**
     * Test of send health data when the user exists
     * @throws Exception no exception expected
     */
    @Test
    public void sendHealthDataWithExistingUser() throws Exception {
        HealthData input = HealthData.newHealthData(null, new Timestamp(0),null, 1, 1, 1, 1);
        HealthData output = HealthData.newHealthData(1L, new Timestamp(0), user, 1, 1, 1, 1);

        given(service.sendHealthData(USER_1, input)).willReturn(output);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(input);

        mvc.perform(post(Constants.SEND_DATA_API + Constants.SEND_HEALTH_DATA_API).
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").content(json).header(Constants.HEADER_USER_SSN, USER_1))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.timestamp", is("1970-01-01T00:00:00.000+0000")))
                .andExpect(jsonPath("$.heartBeat", is(output.getHeartBeat())))
                .andExpect(jsonPath("$.pressureMin", is(output.getPressureMin())))
                .andExpect(jsonPath("$.pressureMax", is(output.getPressureMax())))
                .andExpect(jsonPath("$.bloodOxygenLevel", is(output.getBloodOxygenLevel())))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/datacollection/healthdata")));

    }

    /**
     * Test of send health data when the user does not exist.
     * @throws Exception the UserNotFoundException will be handled by UserNotFoundAdvice
     */
    @Test
    public void sendHealthDataWithNotExistingUser() throws Exception {
        HealthData input = HealthData.newHealthData(null, new Timestamp(0), null, 1, 1, 1, 1);

        given(service.sendHealthData(USER_NOT_FOUND, input)).willThrow(new UserNotFoundException(USER_NOT_FOUND));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(input);

        mvc.perform(post(Constants.SEND_DATA_API + Constants.SEND_HEALTH_DATA_API).
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").content(json).header(Constants.HEADER_USER_SSN, USER_NOT_FOUND))
                .andExpect(status().isNotFound());
    }

    /**
     * Test of send position data when the user exists
     * @throws Exception no exception expected
     */
    @Test
    public void sendPositionDataWithExistingUser() throws Exception {
        PositionData input = PositionData.newPositionData(null, new Timestamp(0), null, 1.0, 1.0);
        PositionData output = PositionData.newPositionData(1L, new Timestamp(0), user, 1.0, 1.0);

        given(service.sendPositionData(USER_1, input)).willReturn(output);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(input);

        mvc.perform(post(Constants.SEND_DATA_API + Constants.SEND_POSITION_DATA_API).
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").content(json).header(Constants.HEADER_USER_SSN, USER_1))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.timestamp", is("1970-01-01T00:00:00.000+0000")))
                .andExpect(jsonPath("$.latitude", is(output.getLatitude())))
                .andExpect(jsonPath("$.longitude", is(output.getLongitude())))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/datacollection/positiondata")));

    }

    /**
     * Test of send position data when the user does not exist
     * @throws Exception the UserNotFoundException will be handled by UserNotFoundAdvice
     */
    @Test
    public void sendPositionDataWithNotExistingUser() throws Exception {
        PositionData input = PositionData.newPositionData(1L, new Timestamp(0), null, 1.0, 1.0);

        given(service.sendPositionData(USER_NOT_FOUND, input)).willThrow(new UserNotFoundException(USER_NOT_FOUND));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(input);

        mvc.perform(post(Constants.SEND_DATA_API + Constants.SEND_POSITION_DATA_API).
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").content(json).header(Constants.HEADER_USER_SSN, USER_NOT_FOUND))
                .andExpect(status().isNotFound());
    }

    /**
     * Test of send cluster of data when the user exists
     * @throws Exception no exception expected
     */
    @Test
    public void sendClusterOfDataWithExistingUser() throws Exception {
        DataWrapper input = new DataWrapper();
        PositionData inputPD1 = PositionData.newPositionData(null, new Timestamp(0), null, 1.0, 1.0);
        HealthData inputHD1 = HealthData.newHealthData(null, new Timestamp(0), null, 1, 1, 1, 1);
        HealthData inputHD2 = HealthData.newHealthData(null, new Timestamp(1), null, 2, 1, 1, 1);

        input.addHealthData(inputHD1);
        input.addHealthData(inputHD2);
        input.addPositionData(inputPD1);

        DataWrapper output = new DataWrapper();
        PositionData outputPD1 =PositionData.newPositionData(1L, new Timestamp(0), user, 1.0, 1.0);
        HealthData outputHD1 = HealthData.newHealthData(1L, new Timestamp(0), user, 1, 1, 1, 1);
        HealthData outputHD2 = HealthData.newHealthData(2L, new Timestamp(1), user, 2, 1, 1, 1);

        output.addHealthData(outputHD1);
        output.addHealthData(outputHD2);
        output.addPositionData(outputPD1);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(input);

        given(service.sendClusterOfData(USER_1, input)).willReturn(output);

        mvc.perform(post(Constants.SEND_DATA_API + Constants.SEND_DATA_CLUSTER_API)
                .contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").content(json).header(Constants.HEADER_USER_SSN, USER_1))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/datacollection/clusterdata")))
                .andExpect(jsonPath("$.positionDataList", hasSize(1)))
                .andExpect(jsonPath("$.positionDataList[*].timestamp", contains("1970-01-01T00:00:00.000+0000")))
                .andExpect(jsonPath("$.positionDataList[*].latitude", contains(outputPD1.getLatitude())))
                .andExpect(jsonPath("$.positionDataList[*].longitude", contains(outputPD1.getLongitude())))
                .andExpect(jsonPath("$.positionDataList[*]._links.self.href",
                        contains("http://localhost/datacollection/positiondata")))
                .andExpect(jsonPath("$.healthDataList", hasSize(2)))
                .andExpect(jsonPath("$.healthDataList[*].timestamp",
                        containsInAnyOrder("1970-01-01T00:00:00.000+0000", "1970-01-01T00:00:00.001+0000")))
                .andExpect(jsonPath("$.healthDataList[*].heartBeat",
                        containsInAnyOrder(outputHD1.getHeartBeat(), outputHD2.getHeartBeat())))
                .andExpect(jsonPath("$.healthDataList[*].pressureMin",
                        containsInAnyOrder(outputHD1.getPressureMin(), outputHD2.getPressureMin())))
                .andExpect(jsonPath("$.healthDataList[*].pressureMax",
                        containsInAnyOrder(outputHD1.getPressureMax(), outputHD2.getPressureMax())))
                .andExpect(jsonPath("$.healthDataList[*].bloodOxygenLevel",
                        containsInAnyOrder(outputHD1.getBloodOxygenLevel(), outputHD2.getBloodOxygenLevel())))
                .andExpect(jsonPath("$.healthDataList[*]._links.self.href",
                        containsInAnyOrder("http://localhost/datacollection/healthdata",
                                "http://localhost/datacollection/healthdata")));
    }

    /**
     * Test of send cluster of data when the user does not exist
     * @throws Exception the UserNotFoundException will be handled by UserNotFoundAdvice
     */
    @Test
    public void sendClusterOfDataWithNotExistingUser() throws Exception {
        DataWrapper input = new DataWrapper();
        PositionData inputPD1 = PositionData.newPositionData(null, new Timestamp(0), null, 1.0, 1.0);
        HealthData inputHD1 = HealthData.newHealthData(null, new Timestamp(0), null, 1, 1, 1, 1);
        HealthData inputHD2 = HealthData.newHealthData(null, new Timestamp(0), null, 1, 1, 1, 1);

        input.addHealthData(inputHD1);
        input.addHealthData(inputHD2);
        input.addPositionData(inputPD1);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(input);

        given(service.sendClusterOfData(USER_NOT_FOUND, input)).willThrow(new UserNotFoundException(USER_NOT_FOUND));

        mvc.perform(post(Constants.SEND_DATA_API + Constants.SEND_DATA_CLUSTER_API).
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").content(json).header(Constants.HEADER_USER_SSN, USER_NOT_FOUND))
                .andExpect(status().isNotFound());

    }

}