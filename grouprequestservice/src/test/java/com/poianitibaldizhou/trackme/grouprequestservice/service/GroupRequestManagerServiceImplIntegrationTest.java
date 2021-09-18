package com.poianitibaldizhou.trackme.grouprequestservice.service;

import com.poianitibaldizhou.trackme.grouprequestservice.entity.GroupRequest;
import com.poianitibaldizhou.trackme.grouprequestservice.util.AggregatorOperator;
import com.poianitibaldizhou.trackme.grouprequestservice.util.GroupRequestWrapper;
import com.poianitibaldizhou.trackme.grouprequestservice.util.RequestType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("classpath:sql/testEventListener.sql")
@ActiveProfiles("usage-message-broker")
public class GroupRequestManagerServiceImplIntegrationTest {

    @Autowired
    private GroupRequestManagerService groupRequestManagerService;

    @Autowired
    @SpyBean
    private InternalCommunicationService internalCommunicationService;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addGroupRequest() throws Exception {
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setThirdPartyId(1L);
        groupRequest.setRequestType(RequestType.ALL);
        groupRequest.setAggregatorOperator(AggregatorOperator.COUNT);
        groupRequest.setCreationTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        GroupRequestWrapper groupRequestWrapper = new GroupRequestWrapper(groupRequest, new ArrayList<>());

        groupRequestManagerService.addGroupRequest(groupRequestWrapper);

        verify(internalCommunicationService, times(1)).sendGroupRequestMessage(any(), any());
    }

}