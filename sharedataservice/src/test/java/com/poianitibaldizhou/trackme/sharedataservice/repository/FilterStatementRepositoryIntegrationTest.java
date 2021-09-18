package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.poianitibaldizhou.trackme.sharedataservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.sharedataservice.entity.GroupRequest;
import com.poianitibaldizhou.trackme.sharedataservice.util.AggregatorOperator;
import com.poianitibaldizhou.trackme.sharedataservice.util.ComparisonSymbol;
import com.poianitibaldizhou.trackme.sharedataservice.util.FieldType;
import com.poianitibaldizhou.trackme.sharedataservice.util.RequestType;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.sql.Timestamp;

import static org.junit.Assert.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles(profiles = {"test"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilterStatementRepositoryIntegrationTest {

    @Autowired
    private FilterStatementRepository filterStatementRepository;

    @Autowired
    private GroupRequestRepository groupRequestRepository;

    private GroupRequest groupRequest, emptyGroupRequest;

    private FilterStatement filterStatement1, filterStatement2;

    @Before
    public void setUp() throws Exception {
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setId(1L);
        groupRequest.setThirdPartyId(1L);
        groupRequest.setRequestType(RequestType.ALL);
        groupRequest.setAggregatorOperator(AggregatorOperator.COUNT);
        groupRequest.setCreationTimestamp(new Timestamp(0));
        this.groupRequest = groupRequestRepository.save(groupRequest);
        GroupRequest groupRequest1 = new GroupRequest();
        groupRequest1.setId(2L);
        groupRequest1.setThirdPartyId(2L);
        groupRequest1.setRequestType(RequestType.USER_SSN);
        groupRequest1.setAggregatorOperator(AggregatorOperator.COUNT);
        groupRequest1.setCreationTimestamp(new Timestamp(0));
        emptyGroupRequest = groupRequestRepository.save(groupRequest1);
        filterStatement1 = FilterStatement.newFilterStatement(null, FieldType.BIRTH_YEAR,
                "1990", ComparisonSymbol.EQUALS, this.groupRequest);
        filterStatement2 = FilterStatement.newFilterStatement(null, FieldType.BIRTH_CITY,
                "Milano", ComparisonSymbol.GREATER, this.groupRequest);
        filterStatementRepository.save(filterStatement1);
        filterStatementRepository.save(filterStatement2);
    }

    @After
    public void tearDown() throws Exception {
        groupRequest = null;
        emptyGroupRequest = null;
    }

    /**
     * Test findAllByGroupRequest_Id when the group request exists
     * @throws Exception no exception expected
     */
    @Test
    public void findAllByGroupRequest_IdExistingGroupRequest() throws Exception {
        assertThat(filterStatementRepository.findAllByGroupRequest_Id(groupRequest.getId()),
                Matchers.containsInAnyOrder(filterStatement1, filterStatement2));
    }

    /**
     * Test findAllByGroupRequest_Id when the group request has empty filter statements attached
     * @throws Exception no exception expected
     */
    @Test
    public void findAllByGroupRequest_IdExistingEmptyGroupRequest() throws Exception{
        assertThat(filterStatementRepository.findAllByGroupRequest_Id(emptyGroupRequest.getId()),
                Matchers.emptyCollectionOf(FilterStatement.class));
    }

    /**
     * Test findAllByGroupRequest_Id when the group request does not exist
     * @throws Exception no exception expected
     */
    @Test
    public void findAllByGroupRequest_IdNotExistingGroupRequest() throws Exception {
        assertThat(filterStatementRepository.findAllByGroupRequest_Id(9999L),
                Matchers.emptyCollectionOf(FilterStatement.class));
    }

}