package com.poianitibaldizhou.trackme.grouprequestservice.controller;

import com.poianitibaldizhou.trackme.grouprequestservice.assembler.GroupRequestWrapperAssembler;
import com.poianitibaldizhou.trackme.grouprequestservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.grouprequestservice.entity.GroupRequest;
import com.poianitibaldizhou.trackme.grouprequestservice.exception.BadOperatorRequestTypeException;
import com.poianitibaldizhou.trackme.grouprequestservice.exception.GroupRequestNotFoundException;
import com.poianitibaldizhou.trackme.grouprequestservice.service.GroupRequestManagerService;
import com.poianitibaldizhou.trackme.grouprequestservice.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit test for the group request controller
 */
@RunWith(SpringRunner.class)
@WebMvcTest(GroupRequestController.class)
@Import({GroupRequestWrapperAssembler.class})
public class GroupRequestControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GroupRequestManagerService service;

    /**
     * Test group request by id
     *
     * @throws Exception due to the mvc mock get method
     */
    @Test
    public void getRequestById() throws Exception {
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setId(1L);
        groupRequest.setRequestType(RequestType.HEART_BEAT);
        groupRequest.setAggregatorOperator(AggregatorOperator.COUNT);
        groupRequest.setStatus(RequestStatus.UNDER_ANALYSIS);
        groupRequest.setCreationTimestamp(new Timestamp(0));
        groupRequest.setThirdPartyId(1L);

        FilterStatement filterStatement = new FilterStatement();
        filterStatement.setGroupRequest(groupRequest);
        filterStatement.setColumn(FieldType.HEART_BEAT);
        filterStatement.setValue("100");
        filterStatement.setComparisonSymbol(ComparisonSymbol.LESS);
        filterStatement.setId(1L);

        GroupRequestWrapper groupRequestWrapper = new GroupRequestWrapper(groupRequest, Collections.singletonList(filterStatement));

        given(service.getById(1L)).willReturn(groupRequestWrapper);

        mvc.perform(get(Constants.GROUP_REQUEST_API + "/id/1").accept(MediaTypes.HAL_JSON_VALUE).header(Constants.HEADER_THIRD_PARTY_ID, "1"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupRequest.creationTimestamp", is("1970-01-01T00:00:00.000+0000")))
                .andExpect(jsonPath("$.groupRequest.aggregatorOperator", is(groupRequest.getAggregatorOperator().toString())))
                .andExpect(jsonPath("$.groupRequest.requestType", is(groupRequest.getRequestType().toString())))
                .andExpect(jsonPath("$.groupRequest.status", is(groupRequest.getStatus().toString())))
                .andExpect(jsonPath("$.filterStatementList", hasSize(1)))
                .andExpect(jsonPath("$.filterStatementList[0].column", is(filterStatement.getColumn().toString())))
                .andExpect(jsonPath("$.filterStatementList[0].value", is(filterStatement.getValue())))
                .andExpect(jsonPath("$.filterStatementList[0].comparisonSymbol", is(filterStatement.getComparisonSymbol().toString())))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost"+Constants.GROUP_REQUEST_API+"/id/1")));

    }

    /**
     * Test the get of a request by a specified id, when no request with that id is present
     *
     * @throws Exception due to mock mvc get perform
     */
    @Test
    public void testGetRequestByIdWhenRequestNotPresent() throws Exception{
        given(service.getById(1L)).willThrow(new GroupRequestNotFoundException(1L));

        mvc.perform(get(Constants.GROUP_REQUEST_API+ "/id/1").accept(MediaTypes.HAL_JSON_VALUE)
                .header(Constants.HEADER_THIRD_PARTY_ID, "1"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isNotFound());

    }

    /**
     * Test the get of group request performed by a specified third party customer
     *
     * @throws Exception due to mock mvc get perform
     */
    @Test
    public void testGetRequestByThirdPartyId() throws Exception {
        GroupRequest groupRequest1 = new GroupRequest();
        groupRequest1.setId(1L);
        groupRequest1.setRequestType(RequestType.PRESSURE_MAX);
        groupRequest1.setAggregatorOperator(AggregatorOperator.MAX);
        groupRequest1.setStatus(RequestStatus.REFUSED);
        groupRequest1.setCreationTimestamp(new Timestamp(0));
        groupRequest1.setThirdPartyId(1L);

        FilterStatement filterStatement1 = new FilterStatement();
        filterStatement1.setGroupRequest(groupRequest1);
        filterStatement1.setColumn(FieldType.BLOOD_OXYGEN_LEVEL);
        filterStatement1.setValue("70");
        filterStatement1.setComparisonSymbol(ComparisonSymbol.EQUALS);
        filterStatement1.setId(1L);

        GroupRequest groupRequest2 = new GroupRequest();
        groupRequest2.setId(2L);
        groupRequest2.setRequestType(RequestType.BIRTH_CITY);
        groupRequest2.setAggregatorOperator(AggregatorOperator.DISTINCT_COUNT);
        groupRequest2.setStatus(RequestStatus.REFUSED);
        groupRequest2.setCreationTimestamp(new Timestamp(0));
        groupRequest2.setThirdPartyId(1L);

        FilterStatement filterStatement2 = new FilterStatement();
        filterStatement2.setGroupRequest(groupRequest2);
        filterStatement2.setColumn(FieldType.HEART_BEAT);
        filterStatement2.setValue("100");
        filterStatement2.setComparisonSymbol(ComparisonSymbol.LESS);
        filterStatement2.setId(2L);

        List<GroupRequestWrapper> groupRequestWrapperList = new ArrayList<>();
        groupRequestWrapperList.add(new GroupRequestWrapper(groupRequest1, Collections.singletonList(filterStatement1)));
        groupRequestWrapperList.add(new GroupRequestWrapper(groupRequest2, Collections.singletonList(filterStatement2)));

        given(service.getByThirdPartyId(1L)).willReturn(groupRequestWrapperList);

        mvc.perform(get(Constants.GROUP_REQUEST_API+ Constants.GROUP_REQUEST_BY_THIRD_PARTY_API)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header(Constants.HEADER_THIRD_PARTY_ID, "1"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.groupRequestWrapperList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.groupRequestWrapperList[*].groupRequest.creationTimestamp",
                        containsInAnyOrder("1970-01-01T00:00:00.000+0000", "1970-01-01T00:00:00.000+0000")))
                .andExpect(jsonPath("$._embedded.groupRequestWrapperList[*].groupRequest.aggregatorOperator",
                        containsInAnyOrder(groupRequest1.getAggregatorOperator().toString(), groupRequest2.getAggregatorOperator().toString())))
                .andExpect(jsonPath("$._embedded.groupRequestWrapperList[*].groupRequest.requestType",
                        containsInAnyOrder(groupRequest1.getRequestType().toString(), groupRequest2.getRequestType().toString())))
                .andExpect(jsonPath("$._embedded.groupRequestWrapperList[*].groupRequest.status",
                        containsInAnyOrder(groupRequest1.getStatus().toString(), groupRequest2.getStatus().toString())))
                .andExpect(jsonPath("$._embedded.groupRequestWrapperList[*].filterStatementList[*].column",
                        containsInAnyOrder(filterStatement1.getColumn().toString(), filterStatement2.getColumn().toString())))
                .andExpect(jsonPath("$._embedded.groupRequestWrapperList[*].filterStatementList[*].value",
                        containsInAnyOrder(filterStatement1.getValue(), filterStatement2.getValue())))
                .andExpect(jsonPath("$._embedded.groupRequestWrapperList[*].filterStatementList[*].comparisonSymbol",
                        containsInAnyOrder(filterStatement1.getComparisonSymbol().toString(), filterStatement2.getComparisonSymbol().toString())))
                .andExpect(jsonPath("$._embedded.groupRequestWrapperList[*]._links.self.href",
                        containsInAnyOrder("http://localhost"+Constants.GROUP_REQUEST_API+"/id/1", "http://localhost"+Constants.GROUP_REQUEST_API+"/id/2")))
                .andExpect(jsonPath("$._links.self.href",
                        is("http://localhost"+Constants.GROUP_REQUEST_API+Constants.GROUP_REQUEST_BY_THIRD_PARTY_API)));
    }

    /**
     * Test the add of a new request when everything is fine
     */
    @Test
    public void testAddNewRequest() throws Exception {
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setId(1L);
        groupRequest.setRequestType(RequestType.PRESSURE_MAX);
        groupRequest.setAggregatorOperator(AggregatorOperator.MAX);
        groupRequest.setStatus(RequestStatus.REFUSED);
        groupRequest.setCreationTimestamp(new Timestamp(0));
        groupRequest.setThirdPartyId(1L);

        FilterStatement filterStatement1 = new FilterStatement();
        filterStatement1.setGroupRequest(groupRequest);
        filterStatement1.setColumn(FieldType.LATITUDE);
        filterStatement1.setValue("100000");
        filterStatement1.setComparisonSymbol(ComparisonSymbol.LESS);
        filterStatement1.setId(1L);

        FilterStatement filterStatement2 = new FilterStatement();
        filterStatement2.setGroupRequest(groupRequest);
        filterStatement2.setColumn(FieldType.LATITUDE);
        filterStatement2.setValue("50000");
        filterStatement2.setComparisonSymbol(ComparisonSymbol.GREATER);
        filterStatement2.setId(2L);

        List<FilterStatement> filterStatementList = new ArrayList<>();
        filterStatementList.add(filterStatement1);
        filterStatementList.add(filterStatement2);

        GroupRequestWrapper groupRequestWrapper = new GroupRequestWrapper(groupRequest, filterStatementList);

        given(service.addGroupRequest(any(GroupRequestWrapper.class))).willReturn(groupRequestWrapper);

        String json = "\n" +
                "{\n" +
                "   \"groupRequest\":{\n" +
                "      \"creationTimestamp\":\"1970-01-01T00:00:00.000+0000\",\n" +
                "      \"aggregatorOperator\":\"MAX\",\n" +
                "      \"requestType\":\"PRESSURE_MAX\",\n" +
                "      \"status\":\"REFUSED\"\n" +
                "   },\n" +
                "   \"filterStatementList\":[\n" +
                "      {\n" +
                "         \"column\":\"LATITUDE\",\n" +
                "         \"value\":\"100000\",\n" +
                "         \"comparisonSymbol\":\"LESS\",\n" +
                "         \"groupRequest\":{\n" +
                "            \"creationTimestamp\":\"1970-01-01T00:00:00.000+0000\",\n" +
                "            \"aggregatorOperator\":\"MAX\",\n" +
                "            \"requestType\":\"PRESSURE_MAX\",\n" +
                "            \"status\":\"REFUSED\"\n" +
                "         }\n" +
                "      },\n" +
                "      {\n" +
                "         \"column\":\"LATITUDE\",\n" +
                "         \"value\":\"50000\",\n" +
                "         \"comparisonSymbol\":\"GREATER\",\n" +
                "         \"groupRequest\":{\n" +
                "            \"creationTimestamp\":\"1970-01-01T00:00:00.000+0000\",\n" +
                "            \"aggregatorOperator\":\"MAX\",\n" +
                "            \"requestType\":\"PRESSURE_MAX\",\n" +
                "            \"status\":\"REFUSED\"\n" +
                "         }\n" +
                "      }\n" +
                "   ],\n" +
                "   \"_links\":{\n" +
                "      \"self\":{\n" +
                "         \"href\":\"http://localhost"+Constants.GROUP_REQUEST_API+"/id/1\"\n" +
                "      }\n" +
                "   }\n" +
                "}";

        mvc.perform(post(Constants.GROUP_REQUEST_API+Constants.NEW_GROUP_REQUEST_API).
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").
                content(json).header(Constants.HEADER_THIRD_PARTY_ID, "1"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("groupRequest.creationTimestamp", is("1970-01-01T00:00:00.000+0000")))
                .andExpect(jsonPath("groupRequest.aggregatorOperator", is(groupRequest.getAggregatorOperator().toString())))
                .andExpect(jsonPath("groupRequest.requestType", is(groupRequest.getRequestType().toString())))
                .andExpect(jsonPath("groupRequest.status", is(groupRequest.getStatus().toString())))
                .andExpect(jsonPath("filterStatementList[0].column", is(filterStatement1.getColumn().toString())))
                .andExpect(jsonPath("filterStatementList[0].value", is(filterStatement1.getValue())))
                .andExpect(jsonPath("filterStatementList[0].comparisonSymbol", is(filterStatement1.getComparisonSymbol().toString())))
                .andExpect(jsonPath("filterStatementList[0].groupRequest.creationTimestamp", is("1970-01-01T00:00:00.000+0000")))
                .andExpect(jsonPath("filterStatementList[0].groupRequest.aggregatorOperator", is(groupRequest.getAggregatorOperator().toString())))
                .andExpect(jsonPath("filterStatementList[0].groupRequest.requestType", is(groupRequest.getRequestType().toString())))
                .andExpect(jsonPath("filterStatementList[0].groupRequest.status", is(groupRequest.getStatus().toString())))
                .andExpect(jsonPath("filterStatementList[1].column", is(filterStatement2.getColumn().toString())))
                .andExpect(jsonPath("filterStatementList[1].value", is(filterStatement2.getValue())))
                .andExpect(jsonPath("filterStatementList[1].comparisonSymbol", is(filterStatement2.getComparisonSymbol().toString())))
                .andExpect(jsonPath("filterStatementList[1].groupRequest.creationTimestamp", is("1970-01-01T00:00:00.000+0000")))
                .andExpect(jsonPath("filterStatementList[1].groupRequest.aggregatorOperator", is(groupRequest.getAggregatorOperator().toString())))
                .andExpect(jsonPath("filterStatementList[1].groupRequest.requestType", is(groupRequest.getRequestType().toString())))
                .andExpect(jsonPath("filterStatementList[1].groupRequest.status", is(groupRequest.getStatus().toString())))
                .andExpect(jsonPath("_links.self.href", is("http://localhost"+Constants.GROUP_REQUEST_API+"/id/1")));
    }

    /**
     * Test the add of a new request when the operator is not matched with the request type
     *
     * @throws Exception due to the mock mvc method post
     */
    @Test
    public void testAddNewRequestWhenBadOperators() throws Exception{
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setThirdPartyId(1L);
        groupRequest.setId(1L);
        groupRequest.setCreationTimestamp(new Timestamp(0));
        groupRequest.setAggregatorOperator(AggregatorOperator.AVG);
        groupRequest.setRequestType(RequestType.ALL);

        GroupRequestWrapper groupRequestWrapper = new GroupRequestWrapper(groupRequest, new ArrayList<>());

        given(service.addGroupRequest(groupRequestWrapper)).willThrow(new BadOperatorRequestTypeException(
                groupRequest.getAggregatorOperator(), groupRequest.getRequestType()));

        mvc.perform(post(Constants.GROUP_REQUEST_API+Constants.NEW_GROUP_REQUEST_API).header(Constants.HEADER_THIRD_PARTY_ID, "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testIllegalHeader() throws Exception {

    }
}
