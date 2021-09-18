package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.poianitibaldizhou.trackme.sharedataservice.entity.HealthData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.User;
import com.poianitibaldizhou.trackme.sharedataservice.exception.UserNotFoundException;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles(profiles = {"test"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("classpath:sql/testHealthDataRepository.sql")
public class HealthDataRepositoryIntegrationTest {

    @Autowired
    private HealthDataRepository healthDataRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;

    private HealthData healthData1, healthData2;

    @Before
    public void setUp() throws Exception {
        user1 = userRepository.findById("user1").orElseThrow(() -> new UserNotFoundException("user1"));

        healthData1 = healthDataRepository.findById(1L).orElseThrow(() -> new ResourceNotFoundException("health not found"));
        healthData2 = healthDataRepository.findById(2L).orElseThrow(() -> new ResourceNotFoundException("health not found"));
    }

    @After
    public void tearDown() throws Exception {
        user1 = null;
        healthData1 = null;
        healthData2 = null;
    }

    /**
     * Test findAllByUserAndTimestampBetween when the user exists and has some health data
     * @throws Exception no exception expected
     */
    @Test
    public void findAllByUserAndTimestampBetweenExistingUser() throws Exception {
        Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(2010, 1, 1, 6, 0));
        Timestamp endTime = Timestamp.valueOf(LocalDateTime.of(2010, 1, 1, 9, 0));
        assertThat(healthDataRepository.findAllByUserAndTimestampBetween(user1, startTime, endTime),
                Matchers.containsInAnyOrder(healthData1, healthData2));
    }

    /**
     * Test findAllByUserAndTimestampBetween when the user does not exist
     * @throws Exception no exception expected
     */
    @Test
    public void findAllByUserAndTimestampBetweenNotExistingUser() throws Exception {
        Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(2010, 1, 1, 6, 0));
        Timestamp endTime = Timestamp.valueOf(LocalDateTime.of(2010, 1, 1, 9, 0));
        User user = new User();
        user.setSsn("user3");
        user.setBirthNation("italy");
        user.setBirthCity("brescia");
        user.setBirthDate(new Date(0));
        user.setFirstName("tangtang");
        user.setLastName("zhao");
        assertThat(healthDataRepository.findAllByUserAndTimestampBetween(user, startTime, endTime),
                Matchers.emptyCollectionOf(HealthData.class));
    }

    /**
     * Test findAllByUserAndTimestampBetween when the user exists but has no data on the time defined
     * @throws Exception no exception expected
     */
    @Test
    public void findAllByUserAndTimestampBetweenExistingUserButNoData() throws Exception {
        assertThat(healthDataRepository.findAllByUserAndTimestampBetween(user1, new Timestamp(0), new Timestamp(0)),
                Matchers.emptyCollectionOf(HealthData.class));
    }
}