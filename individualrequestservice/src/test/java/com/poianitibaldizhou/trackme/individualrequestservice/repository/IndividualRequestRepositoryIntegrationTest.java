package com.poianitibaldizhou.trackme.individualrequestservice.repository;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.User;
import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Integration test among the individual request repository and the database.
 * Unit test is not reasonable in this case.
 * Check: https://stackoverflow.com/questions/23435937/how-to-test-spring-data-repositories
 */
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@Sql("classpath:RepositoryTest.sql")
public class IndividualRequestRepositoryIntegrationTest {

    @Autowired
    private IndividualRequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Before
    public void setUpPersistentUnit() {

    }

    // Test retrieve by third party id

    /**
     * Test the custom method of repository that retrieves all the requests associated with a certain third party
     * customer.
     * In the test, three requests are given in an embedded database, two of them regards the same third party.
     */
    @Test
    public void retrieveRequestByThirdPartyID() {
        // given: three request (see set up)


        // when: the third party is present and is related with two of them
        List<IndividualRequest> requestList = requestRepository.findAllByThirdParty_Id((long) 1);

        // then: expect that the requests are related with the third party specified
        for(int i = 0; i < requestList.size(); i++) {
            assertEquals((long)1, (long)requestList.get(i).getThirdParty().getId());
        }
    }

    // Test retrieving by user and status

    /**
     * Test the custom method of repository that retrieves all the requests associated with a certain third party
     * customer.
     * In the test, three requests are given in an embedded database, two of them regards the same third party.
     */
    @Test
    public void retrieveByUserAndStatus() {
        // given: three request (see setup)

        // when: the list of the accepted request by user1 is requested
        List<IndividualRequest> requestList = requestRepository.findAllByUserAndStatus(new User("user1"), IndividualRequestStatus.ACCEPTED);

        // then: expect that the size of retrieval is 1 and that the accepted request is the one present in the list
        for(IndividualRequest request : requestList) {
            assertEquals("user1", request.getUser().getSsn());
            assertEquals(IndividualRequestStatus.ACCEPTED, request.getStatus());
        }
    }
}
