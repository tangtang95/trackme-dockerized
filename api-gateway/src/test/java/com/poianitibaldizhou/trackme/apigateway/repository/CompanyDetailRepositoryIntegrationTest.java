package com.poianitibaldizhou.trackme.apigateway.repository;

import com.poianitibaldizhou.trackme.apigateway.entity.ThirdPartyCustomer;
import com.poianitibaldizhou.trackme.apigateway.filter.pre.AccessControlFilter;
import com.poianitibaldizhou.trackme.apigateway.filter.route.TranslationFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Integration test for custom methods of the repository that regards details of companies related with third party customers
 */
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@Sql({"classpath:IntegrationTestData"})
public class CompanyDetailRepositoryIntegrationTest {

    @Autowired
    private CompanyDetailRepository companyDetailRepository;

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @MockBean
    private AccessControlFilter controlFilter;

    @MockBean
    private TranslationFilter translationFilter;

    /**
     * Test the get of company details related to a third party customer when they are present
     *
     * @throws Exception test failed: no details related with that third party customer is present
     */
    @Test
    public void testFindByThirdPartyCustomer() throws Exception {
        ThirdPartyCustomer thirdPartyCustomer = thirdPartyRepository.findById(1L).orElseThrow(Exception::new);

        assertEquals(thirdPartyCustomer, companyDetailRepository.findByThirdPartyCustomer(thirdPartyCustomer).orElseThrow(Exception::new).getThirdPartyCustomer());
    }

    /**
     * Test the get of company details when they are not present.
     * Two cases may occur: third party customer not registered at all and third party customer registered not
     * as a company
     *
     * @throws Exception test failed: data doesn't match the test
     */
    @Test
    public void testFindByThirdPartyCustomerWhenNotPresent() throws Exception {
        // Not registered at all
        ThirdPartyCustomer thirdPartyCustomer = new ThirdPartyCustomer();
        thirdPartyCustomer.setId(5L);
        thirdPartyCustomer.setEmail("nonPresentmail@provier.it");
        thirdPartyCustomer.setPassword("pass");

        assertTrue(!companyDetailRepository.findByThirdPartyCustomer(thirdPartyCustomer).isPresent());

        // Not registered with company details
        assertTrue(!companyDetailRepository.findByThirdPartyCustomer(thirdPartyRepository.findById(3L).orElseThrow(Exception::new)).isPresent());
    }


}
