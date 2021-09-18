package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.poianitibaldizhou.trackme.sharedataservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.sharedataservice.exception.IndividualRequestNotFoundException;
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

import static org.junit.Assert.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles(profiles = {"test"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"classpath:sql/testIndividualRequestRepository.sql"})
public class IndividualRequestRepositoryIntegrationTest {

    @Autowired
    private IndividualRequestRepository individualRequestRepository;

    private IndividualRequest individualRequest1, individualRequest2;

    @Before
    public void setUp() throws Exception {
        individualRequest1 = individualRequestRepository.findById(1L).orElseThrow(() -> new IndividualRequestNotFoundException(1L));
        individualRequest2 = individualRequestRepository.findById(4L).orElseThrow(() -> new IndividualRequestNotFoundException(4L));
    }

    @After
    public void tearDown() throws Exception {
        individualRequest1 = null;
        individualRequest2 = null;
    }

    /**
     * Test findByIdAndThirdPartyId when the individual request exists and matches the third party id
     * @throws Exception no exception expected
     */
    @Test
    public void findByIdAndThirdPartyIdExistingIndividualRequest() throws Exception {
        assertEquals(individualRequest1, individualRequestRepository.findByIdAndThirdPartyId(1L, 1L)
                .orElseThrow(() -> new IndividualRequestNotFoundException(1L)));
        assertEquals(individualRequest2, individualRequestRepository.findByIdAndThirdPartyId(4L, 2L)
                .orElseThrow(() -> new IndividualRequestNotFoundException(2L)));
    }

    /**
     * Test findByIdAndThirdPartyId when the individual request does not match the third part id
     * @throws Exception no exception expected
     */
    @Test(expected = IndividualRequestNotFoundException.class)
    public void findByIdAndThirdPartyIdWithThirdPartyIdNotMatching() throws Exception {
        IndividualRequest individualRequest = individualRequestRepository.findByIdAndThirdPartyId(1L, 2L)
                .orElseThrow(() -> new IndividualRequestNotFoundException(1L));
    }
}