package com.poianitibaldizhou.trackme.grouprequestservice.service;

import com.poianitibaldizhou.trackme.grouprequestservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.grouprequestservice.entity.GroupRequest;
import com.poianitibaldizhou.trackme.grouprequestservice.exception.BadOperatorRequestTypeException;
import com.poianitibaldizhou.trackme.grouprequestservice.exception.GroupRequestNotFoundException;
import com.poianitibaldizhou.trackme.grouprequestservice.repository.FilterStatementRepository;
import com.poianitibaldizhou.trackme.grouprequestservice.repository.GroupRequestRepository;
import com.poianitibaldizhou.trackme.grouprequestservice.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Fail.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;


/**
 * Unit test for the implementation of the group request manager service
 */
@RunWith(SpringRunner.class)
public class GroupRequestManagerServiceImplUnitTest {

    @MockBean
    private GroupRequestRepository groupRequestRepository;

    @MockBean
    private FilterStatementRepository filterStatementRepository;

    private GroupRequestManagerServiceImpl groupRequestManagerService;

    @Before
    public void setUp() {
        setUpRepo();

        groupRequestManagerService = new GroupRequestManagerServiceImpl(groupRequestRepository, filterStatementRepository);
    }

    @After
    public void tearDown() {
        groupRequestManagerService = null;
    }

    /**
     * Set up the group request repository and the filter statement repository for the unit test
     */
    private void setUpRepo() {
        // set up the group request repository
        GroupRequest request1 = new GroupRequest();
        GroupRequest request2 = new GroupRequest();
        GroupRequest request3 = new GroupRequest();

        request1.setId(1L);
        request1.setThirdPartyId(1L);
        request1.setCreationTimestamp(new Timestamp(0));
        request1.setStatus(RequestStatus.ACCEPTED);
        request1.setAggregatorOperator(AggregatorOperator.COUNT);
        request1.setRequestType(RequestType.ALL);

        request2.setId(2L);
        request2.setThirdPartyId(1L);
        request2.setCreationTimestamp(new Timestamp(0));
        request2.setStatus(RequestStatus.UNDER_ANALYSIS);
        request2.setAggregatorOperator(AggregatorOperator.MAX);
        request2.setRequestType(RequestType.PRESSURE_MIN);

        request3.setId(3L);
        request3.setThirdPartyId(2L);
        request3.setCreationTimestamp(new Timestamp(0));
        request3.setStatus(RequestStatus.REFUSED);
        request3.setAggregatorOperator(AggregatorOperator.AVG);
        request3.setRequestType(RequestType.HEART_BEAT);

        List<GroupRequest> groupRequestList1 = new ArrayList<>();
        groupRequestList1.add(request1);
        groupRequestList1.add(request2);

        Mockito.when(groupRequestRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(request1));
        Mockito.when(groupRequestRepository.findById(2L)).thenReturn(java.util.Optional.ofNullable(request2));
        Mockito.when(groupRequestRepository.findById(3L)).thenReturn(java.util.Optional.ofNullable(request3));

        Mockito.when(groupRequestRepository.findAllByThirdPartyId(1L)).thenReturn(groupRequestList1);
        Mockito.when(groupRequestRepository.findAllByThirdPartyId(2L)).thenReturn(Collections.singletonList(request3));

        // Set up the filter statement repository

        FilterStatement filterStatement1 = new FilterStatement();
        FilterStatement filterStatement2 = new FilterStatement();
        FilterStatement filterStatement3 = new FilterStatement();

        filterStatement1.setId(1L);
        filterStatement1.setGroupRequest(request1);
        filterStatement1.setColumn(FieldType.HEART_BEAT);
        filterStatement1.setComparisonSymbol(ComparisonSymbol.LESS);
        filterStatement1.setValue("100");

        filterStatement2.setId(2L);
        filterStatement2.setGroupRequest(request1);
        filterStatement2.setColumn(FieldType.PRESSURE_MAX);
        filterStatement2.setComparisonSymbol(ComparisonSymbol.EQUALS);
        filterStatement2.setValue("125");

        filterStatement3.setId(3L);
        filterStatement3.setGroupRequest(request2);
        filterStatement3.setColumn(FieldType.PRESSURE_MIN);
        filterStatement3.setComparisonSymbol(ComparisonSymbol.GREATER);
        filterStatement3.setValue("86");

        Mockito.when(filterStatementRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(filterStatement1));
        Mockito.when(filterStatementRepository.findById(2L)).thenReturn(java.util.Optional.ofNullable(filterStatement2));
        Mockito.when(filterStatementRepository.findById(3L)).thenReturn(java.util.Optional.ofNullable(filterStatement3));

        List<FilterStatement> list = new ArrayList<>();
        list.add(filterStatement1);
        list.add(filterStatement2);

        Mockito.when(filterStatementRepository.findAllByGroupRequest_Id(1L)).thenReturn(list);
        Mockito.when(filterStatementRepository.findAllByGroupRequest_Id(2L)).thenReturn(Collections.singletonList(filterStatement3));
    }

    /**
     * Test the get of group request by id, when the request with the specified id is present
     */
    @Test
    public void testGetGroupRequestById() {
        GroupRequest groupRequest = groupRequestManagerService.getById(1L).getGroupRequest();
        assertEquals(new Long(1), groupRequest.getId());
    }

    /**
     * Test the get of group request by id, when the request with the specified id is not present
     */
    @Test(expected = GroupRequestNotFoundException.class)
    public void testGetGroupRequestByIdWhenNotPresent() {
        groupRequestManagerService.getById(100L);
    }

    /**
     * Test get group request by third party id
     */
    @Test
    public void testGetRequestByThirdPartyId() {
        List<GroupRequestWrapper> groupRequestWrapperList = groupRequestManagerService.getByThirdPartyId(1L);

        for(GroupRequestWrapper groupRequestWrapper : groupRequestWrapperList) {
            assertEquals(new Long(1), groupRequestWrapper.getGroupRequest().getThirdPartyId());
            for(FilterStatement filterStatement : groupRequestWrapper.getFilterStatementList()) {
                assertEquals(groupRequestWrapper.getGroupRequest().getId(), filterStatement.getGroupRequest().getId());
            }
        }
    }

    /**
     * Test get group request by third party it when the third party is present
     */
    @Test
    public void testGetRequestByThirdPartyIdWhenNoRequest() {
        assertTrue(groupRequestManagerService.getByThirdPartyId(100L).isEmpty());
    }

    /**
     * Test the add of a new group request
     */
    @Test
    public void testAddRequestTest() {
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setId(4L);
        groupRequest.setThirdPartyId(2L);
        groupRequest.setCreationTimestamp(new Timestamp(0));
        groupRequest.setStatus(RequestStatus.REFUSED);
        groupRequest.setAggregatorOperator(AggregatorOperator.COUNT);
        groupRequest.setRequestType(RequestType.ALL);

        FilterStatement filterStatement1 = new FilterStatement();
        FilterStatement filterStatement2 = new FilterStatement();

        filterStatement1.setGroupRequest(groupRequest);
        filterStatement1.setId(1L);
        filterStatement1.setColumn(FieldType.HEART_BEAT);
        filterStatement1.setComparisonSymbol(ComparisonSymbol.LESS);
        filterStatement1.setValue("100");

        filterStatement2.setGroupRequest(groupRequest);
        filterStatement1.setId(2L);
        filterStatement2.setColumn(FieldType.PRESSURE_MAX);
        filterStatement2.setComparisonSymbol(ComparisonSymbol.EQUALS);
        filterStatement2.setValue("125");

        List<FilterStatement> filterStatements = new ArrayList<>();
        filterStatements.add(filterStatement1);
        filterStatements.add(filterStatement2);

        Mockito.when(groupRequestRepository.saveAndFlush(any(GroupRequest.class))).thenReturn(groupRequest);
        Mockito.when(filterStatementRepository.save(filterStatement1)).thenReturn(filterStatement1);
        Mockito.when(filterStatementRepository.save(filterStatement2)).thenReturn(filterStatement2);
        Mockito.when(filterStatementRepository.findAllByGroupRequest_Id(1L)).thenReturn(filterStatements);

        groupRequestManagerService.addGroupRequest(new GroupRequestWrapper(groupRequest, filterStatements));
        verify(groupRequestRepository, times(1)).saveAndFlush(any(GroupRequest.class));
        verify(filterStatementRepository, times(2)).save(any(FilterStatement.class));
    }

    /**
     * Test the add of a new group request when the aggregator can be matched with the type of the request
     */
    @Test
    public void testAddRequestWithValidOperatorAndRequestType() {
        List<AggregatorOperator> aggregatorOperatorList = new ArrayList<>();
        aggregatorOperatorList.add(AggregatorOperator.COUNT);
        aggregatorOperatorList.add(AggregatorOperator.DISTINCT_COUNT);

        for(AggregatorOperator aggregatorOperator : AggregatorOperator.values()) {
            for(RequestType requestType : RequestType.values()) {
                if(requestType.isNumber() || !requestType.isNumber() && aggregatorOperatorList.contains(aggregatorOperator)) {
                    GroupRequest groupRequest = new GroupRequest();
                    groupRequest.setId(4L);
                    groupRequest.setThirdPartyId(2L);
                    groupRequest.setCreationTimestamp(new Timestamp(0));
                    groupRequest.setStatus(RequestStatus.REFUSED);
                    groupRequest.setAggregatorOperator(AggregatorOperator.COUNT);
                    groupRequest.setRequestType(RequestType.ALL);

                    Mockito.when(groupRequestRepository.saveAndFlush(any(GroupRequest.class))).thenReturn(groupRequest);
                    Mockito.when(filterStatementRepository.findAllByGroupRequest_Id(1L)).thenReturn(new ArrayList<>());

                    groupRequestManagerService.addGroupRequest(new GroupRequestWrapper(groupRequest, new ArrayList<>()));
                    verify(groupRequestRepository).saveAndFlush(any(GroupRequest.class));
                    reset(groupRequestRepository);
                }
            }
        }
    }

    /**
     * Test the add of a new group request when the aggregator operator can't be matched with the type of request type
     */
    @Test
    public void testAddRequestWithInvalidOperatorAndRequestType() {
        GroupRequest groupRequest;
        List<AggregatorOperator> criticalAggregatorOperators = new ArrayList<>();
        criticalAggregatorOperators.add(AggregatorOperator.MAX);
        criticalAggregatorOperators.add(AggregatorOperator.MIN);
        criticalAggregatorOperators.add(AggregatorOperator.AVG);

        List<RequestType> criticalRequestType = new ArrayList<>();
        criticalRequestType.add(RequestType.ALL);
        criticalRequestType.add(RequestType.BIRTH_CITY);
        criticalRequestType.add(RequestType.USER_SSN);

        for (AggregatorOperator aggregatorOperator : criticalAggregatorOperators) {
            for(RequestType requestType : criticalRequestType) {
                groupRequest = new GroupRequest();
                groupRequest.setId(4L);
                groupRequest.setThirdPartyId(2L);
                groupRequest.setCreationTimestamp(new Timestamp(0));
                groupRequest.setStatus(RequestStatus.REFUSED);
                groupRequest.setAggregatorOperator(aggregatorOperator);
                groupRequest.setRequestType(requestType);

                try {
                    groupRequestManagerService.addGroupRequest(new GroupRequestWrapper(groupRequest, new ArrayList<>()));
                    fail("Exception expected");
                } catch(BadOperatorRequestTypeException e) {

                }
            }
        }
    }

}