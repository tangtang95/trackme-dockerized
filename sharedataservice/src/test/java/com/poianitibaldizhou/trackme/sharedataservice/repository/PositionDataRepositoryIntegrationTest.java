package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.poianitibaldizhou.trackme.sharedataservice.entity.HealthData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.PositionData;
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
@Sql("classpath:sql/testPositionDataRepository.sql")
public class PositionDataRepositoryIntegrationTest {

    @Autowired
    private PositionDataRepository positionDataRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;

    private PositionData positionData1, positionData2, positionData3;

    @Before
    public void setUp() throws Exception {
        user1 = userRepository.findById("user1").orElseThrow(() -> new UserNotFoundException("user1"));
        positionData1 = positionDataRepository.findById(1L).orElseThrow(() -> new ResourceNotFoundException("position data no found"));
        positionData2 = positionDataRepository.findById(2L).orElseThrow(() -> new ResourceNotFoundException("position data no found"));
        positionData3 = positionDataRepository.findById(3L).orElseThrow(() -> new ResourceNotFoundException("position data no found"));
    }

    @After
    public void tearDown() throws Exception {
        user1 = null;
        positionData1 = null;
        positionData2 = null;
        positionData3 = null;
    }

    /**
     * Test findAllByUserAndTimestampBetween when the user exists and has some position data
     * @throws Exception no exception expected
     */
    @Test
    public void findAllByUserAndTimestampBetweenExistingUser() throws Exception {
        Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(2010, 1, 1, 6, 0));
        Timestamp endTime = Timestamp.valueOf(LocalDateTime.of(2010, 1, 1, 23, 0));
        assertThat(positionDataRepository.findAllByUserAndTimestampBetween(user1, startTime, endTime),
                Matchers.containsInAnyOrder(positionData1, positionData2, positionData3));
    }

    /**
     * Test findAllByUserAndTimestampBetween when the user does not exists
     * @throws Exception no exception expected
     */
    @Test
    public void findAllByUserAndTimestampBetweenNotExistingUser() throws Exception {
        Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(2010, 1, 1, 6, 0));
        Timestamp endTime = Timestamp.valueOf(LocalDateTime.of(2010, 1, 1, 23, 0));
        User user = new User();
        user.setSsn("user3");
        user.setBirthNation("italy");
        user.setBirthCity("brescia");
        user.setBirthDate(new Date(0));
        user.setFirstName("tangtang");
        user.setLastName("zhao");
        assertThat(positionDataRepository.findAllByUserAndTimestampBetween(user, startTime, endTime),
                Matchers.emptyCollectionOf(PositionData.class));
    }
}