package com.poianitibaldizhou.trackme.apigateway.repository;

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
 * Integration test for custom methods of third party customer repository
 */
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@Sql({"classpath:IntegrationTestData"})
public class ThirdPartyRepositoryIntegrationTest {

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @MockBean
    private AccessControlFilter controlFilter;

    @MockBean
    private TranslationFilter translationFilter;


    /**
     * Test the get of a third party customer by email when a third party customer with that email is present
     *
     * @throws Exception test failed: no customer with the speficied email exists
     */
    @Test
    public void testGetByEmailWhenPresent() throws Exception {
        assertEquals("tp2@provider.com",thirdPartyRepository.findByEmail("tp2@provider.com").orElseThrow(Exception::new).getEmail());
    }


    /**
     * Test the get of a third party customer by email when no customer with that email is registered
     */
    @Test
    public void testGetByEmailWhenNotPresent() {
        assertTrue(!thirdPartyRepository.findByEmail("nonExistingMail").isPresent());
    }


}
