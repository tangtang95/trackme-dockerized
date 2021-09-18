package com.poianitibaldizhou.trackme.grouprequestservice.repository;

import com.poianitibaldizhou.trackme.grouprequestservice.entity.FilterStatement;
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

import static org.junit.Assert.assertEquals;

/**
 * Integration test for custom methods of the group request repository
 */
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@Sql({"classpath:testRepositoryDB.sql"})
public class FilterStatementRepositoryIntegrationTest {

    @Autowired
    private FilterStatementRepository filterStatementRepository;

    /**
     * Test that the retrieval of all the filter belongs to the specified group request, and furthermore, test
     * the fact that all the filter statements of that requests are present
     */
    @Test
    public void testFindByGroupRequestId() {
        List<FilterStatement> filterStatements = filterStatementRepository.findAllByGroupRequest_Id(3L);

        filterStatements.forEach(filterStatement -> assertEquals(3L, (long)filterStatement.getGroupRequest().getId()));
        assertEquals(3, filterStatements.size());
    }
}
