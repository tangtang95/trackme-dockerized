package com.poianitibaldizhou.trackme.grouprequestservice.repository;

import com.poianitibaldizhou.trackme.grouprequestservice.entity.GroupRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Integration test for custom methods of the group request repository
 */
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@Sql({"classpath:testRepositoryDB.sql"})
public class GroupRequestRepositoryIntegrationTest {

    @Autowired
    private GroupRequestRepository groupRequestRepository;

    /**
     * Test that all the requests returned belongs to the specified third party id, furthermore,
     * a check is added to the size of the list, since it must contains all the requests of that
     * third party customer
     */
    @Test
    public void testFindAllByThirdPartyId() {
        List<GroupRequest> requestList = groupRequestRepository.findAllByThirdPartyId(1L);

        // then: expect that the requests are related with the third party specified
        for (GroupRequest request : requestList) {
            Assert.assertEquals(1L, (long) request.getThirdPartyId());
        }

        Assert.assertEquals(2, requestList.size());
    }
}
