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
 * Integration test for custom methods of the repository that regards private details related with third party customers
 */
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@Sql({"classpath:IntegrationTestData"})
public class PrivateThirdPartyDetailRepositoryIntegrationTest {

    @Autowired
    private PrivateThirdPartyDetailRepository privateThirdPartyDetailRepository;

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @MockBean
    private AccessControlFilter controlFilter;

    @MockBean
    private TranslationFilter translationFilter;

    /**
     * Test the research of private details by third party customer when a private detail for a specified customer
     * exists
     *
     * @throws Exception data not present in the sql script
     */
    @Test
    public void testFindByThirdPartyCustomerWhenPresent() throws Exception {
        ThirdPartyCustomer thirdPartyCustomer = thirdPartyRepository.findById(3L).orElseThrow(Exception::new);

        assertEquals(thirdPartyCustomer,
                privateThirdPartyDetailRepository.findByThirdPartyCustomer(thirdPartyCustomer).orElseThrow(Exception::new).getThirdPartyCustomer());
    }


    /**
     * Test the get of private details when they are not present.
     * Two cases may occur: third party customer not registered at all and third party customer registered
     * as a company
     *
     * @throws Exception test failed: data doesn't match the test
     */
    @Test
    public void testFindByThirdPartyCustomerWhenNotPresent() throws Exception {
        // Not registered at all
        ThirdPartyCustomer thirdPartyCustomer = new ThirdPartyCustomer();
        thirdPartyCustomer.setId(100L);
        thirdPartyCustomer.setEmail("notPresentMail@provider.com");
        thirdPartyCustomer.setPassword("pass");

        assertTrue(!privateThirdPartyDetailRepository.findByThirdPartyCustomer(thirdPartyCustomer).isPresent());

        // Registered as a company
        thirdPartyCustomer = thirdPartyRepository.findById(1L).orElseThrow(Exception::new);
        assertTrue(!privateThirdPartyDetailRepository.findByThirdPartyCustomer(thirdPartyCustomer).isPresent());
    }
}
