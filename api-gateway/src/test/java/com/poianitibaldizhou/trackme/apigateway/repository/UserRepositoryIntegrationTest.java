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
 * Integration test for custom methods of user repository
 */
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@Sql({"classpath:IntegrationTestData"})
public class UserRepositoryIntegrationTest {

    @MockBean
    private AccessControlFilter controlFilter;

    @MockBean
    private TranslationFilter translationFilter;

    @Autowired
    private UserRepository userRepository;

    /**
     * Test the get of a user by username when a user with that username is present in the system
     *
     * @throws Exception test failed: no user with the specified username exists
     */
    @Test
    public void testGetByUsernameWhenPresent() throws Exception {
        assertEquals("username1", userRepository.findByUsername("username1").orElseThrow(Exception::new).getUsername());
    }

    /**
     *
     */
    @Test
    public void testGetByUsernameWhenNotPresent() {
        assertTrue(!userRepository.findByUsername("notPresentUsername").isPresent());
    }
}
