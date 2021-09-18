package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.poianitibaldizhou.trackme.sharedataservice.entity.GroupRequest;
import com.poianitibaldizhou.trackme.sharedataservice.exception.GroupRequestNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles(profiles = {"test"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"classpath:sql/testGroupRequestRepository.sql"})
public class GroupRequestRepositoryIntegrationTest {

    @Autowired
    private GroupRequestRepository groupRequestRepository;

    private GroupRequest groupRequest1, groupRequest2, groupRequest3;

    @Before
    public void setUp() throws Exception {
        groupRequest1 = groupRequestRepository.findById(1L).orElseThrow(() -> new GroupRequestNotFoundException(1L));
        groupRequest2 = groupRequestRepository.findById(2L).orElseThrow(() -> new GroupRequestNotFoundException(2L));
        groupRequest3 = groupRequestRepository.findById(3L).orElseThrow(() -> new GroupRequestNotFoundException(3L));
    }

    @After
    public void tearDown() throws Exception {
        groupRequest1 = null;
        groupRequest2 = null;
        groupRequest3 = null;
    }

    /**
     * Test findByIdAndThirdPartyId when the request id exists and matches the third party
     * @throws Exception no exception expected
     */
    @Test
    public void findByIdAndThirdPartyIdExistingRequestId() throws Exception {
        assertEquals(groupRequest1, groupRequestRepository.findByIdAndThirdPartyId(1L, 1L)
                .orElseThrow(() -> new GroupRequestNotFoundException(1L)));
        assertEquals(groupRequest2, groupRequestRepository.findByIdAndThirdPartyId(2L, 2L)
                .orElseThrow(() -> new GroupRequestNotFoundException(2L)));
        assertEquals(groupRequest3, groupRequestRepository.findByIdAndThirdPartyId(3L, 1L)
                .orElseThrow(() -> new GroupRequestNotFoundException(3L)));
    }
    /**
     * Test findByIdAndThirdPartyId when the request id exists but does not match the third party
     * @throws Exception no exception expected
     */
    @Test(expected = GroupRequestNotFoundException.class)
    public void findByIdAndThirdPartyIdThirdPartyIdNotMatching() throws Exception {
        assertEquals(groupRequest1, groupRequestRepository.findByIdAndThirdPartyId(1L, 2L)
                .orElseThrow(() -> new GroupRequestNotFoundException(1L)));
    }

    /**
     * Test findByIdAndThirdPartyId when the request id does not exist
     * @throws Exception no exception expected
     */
    @Test(expected = GroupRequestNotFoundException.class)
    public void findByIdAndThirdPartyIdNotExistingRequestId() throws Exception {
        assertEquals(groupRequest1, groupRequestRepository.findByIdAndThirdPartyId(2L, 1L)
                .orElseThrow(() -> new GroupRequestNotFoundException(2L)));
    }

}