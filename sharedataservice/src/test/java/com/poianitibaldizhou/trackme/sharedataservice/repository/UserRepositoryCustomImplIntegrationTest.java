package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poianitibaldizhou.trackme.sharedataservice.entity.*;
import com.poianitibaldizhou.trackme.sharedataservice.exception.UserNotFoundException;
import com.poianitibaldizhou.trackme.sharedataservice.util.AggregatorOperator;
import com.poianitibaldizhou.trackme.sharedataservice.util.ComparisonSymbol;
import com.poianitibaldizhou.trackme.sharedataservice.util.FieldType;
import com.poianitibaldizhou.trackme.sharedataservice.util.RequestType;
import org.junit.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.util.ResourceUtils;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(value = Enclosed.class)
@ActiveProfiles(profiles = {"test"})
public class UserRepositoryCustomImplIntegrationTest {

    @RunWith(value = Parameterized.class)
    @DataJpaTest
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public static class ParameterizedPart {

        @ClassRule
        public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

        @Rule
        public final SpringMethodRule springMethodRule = new SpringMethodRule();

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private HealthDataRepository healthDataRepository;

        @Autowired
        private PositionDataRepository positionDataRepository;

        private GroupRequest groupRequest;
        private List<FilterStatement> filterStatementList;
        private Double result;

        static FilterStatement newFilterStatement(FieldType fieldType, ComparisonSymbol symbol, String value) {
            FilterStatement filterStatement = new FilterStatement();
            filterStatement.setColumn(fieldType);
            filterStatement.setComparisonSymbol(symbol);
            filterStatement.setValue(value);
            return filterStatement;
        }

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    // A total of 11 data on user that has both position data and health data (ssn = 1, ssn = 9, ssn = 3)
                    {AggregatorOperator.COUNT, RequestType.ALL,
                            new ArrayList<>(),
                            11.0},
                    // There are 3 users that has both position data and health data
                    {AggregatorOperator.DISTINCT_COUNT, RequestType.BIRTH_CITY,
                            new ArrayList<>(),
                            3.0},
                    {AggregatorOperator.AVG, RequestType.BIRTH_YEAR,
                            Arrays.asList(newFilterStatement(FieldType.HEART_BEAT, ComparisonSymbol.GREATER, "100")),
                            1960.0},
                    {AggregatorOperator.MAX, RequestType.HEART_BEAT,
                            Arrays.asList(newFilterStatement(FieldType.LATITUDE, ComparisonSymbol.GREATER, "-7")),
                            191.0},
                    {AggregatorOperator.MIN, RequestType.PRESSURE_MIN,
                            Arrays.asList(newFilterStatement(FieldType.BIRTH_CITY, ComparisonSymbol.NOT_EQUALS, "New Tiffanyfort")),
                            66.0},
                    {AggregatorOperator.DISTINCT_COUNT, RequestType.USER_SSN,
                            Arrays.asList(newFilterStatement(FieldType.BIRTH_CITY, ComparisonSymbol.EQUALS, "Gerardland")),
                            1.0},
                    {AggregatorOperator.MIN, RequestType.BLOOD_OXYGEN_LEVEL,
                            Arrays.asList(newFilterStatement(FieldType.POSITION_TIMESTAMP, ComparisonSymbol.GREATER, "0")),
                            50.0},
                    {AggregatorOperator.MAX, RequestType.PRESSURE_MAX,
                            Arrays.asList(
                                    newFilterStatement(FieldType.HEALTH_TIMESTAMP, ComparisonSymbol.GREATER, "0"),
                                    newFilterStatement(FieldType.POSITION_TIMESTAMP, ComparisonSymbol.GREATER, "0")),
                            167.0}
            });
        }

        public ParameterizedPart(AggregatorOperator aggregatorOperator, RequestType requestType,
                                 List<FilterStatement> filterStatements, Double result) {
            this.groupRequest = new GroupRequest();
            this.groupRequest.setAggregatorOperator(aggregatorOperator);
            this.groupRequest.setRequestType(requestType);
            this.filterStatementList = filterStatements;
            this.result = result;
        }

        @Before
        public void setUp() throws Exception {
            setUpUserRepository();
            setUpHealthDataRepository();
            setUpPositionDataRepository();
        }

        private void setUpUserRepository() throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            List<User> users = mapper
                    .readValue(ResourceUtils.getFile("classpath:userRepoCustomImplUnitTestResource/users.json"),
                            new TypeReference<List<User>>(){});
            userRepository.saveAll(users);
        }

        private void setUpHealthDataRepository() throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            List<HealthData> healthDataList = mapper
                    .readValue(ResourceUtils.getFile("classpath:userRepoCustomImplUnitTestResource/healthData.json"),
                            new TypeReference<List<HealthData>>(){});
            healthDataList.stream().map(healthData -> {
                User user = userRepository.findById(healthData.getUser().getSsn())
                        .orElseThrow(() -> new UserNotFoundException(healthData.getUser().getSsn()));
                healthData.setUser(user);
                return healthData;
            }).forEach(healthData -> healthDataRepository.save(healthData));
        }

        private void setUpPositionDataRepository() throws IOException{
            ObjectMapper mapper = new ObjectMapper();
            List<PositionData> positionDataList = mapper
                    .readValue(ResourceUtils.getFile("classpath:userRepoCustomImplUnitTestResource/positionData.json"),
                            new TypeReference<List<PositionData>>(){});
            positionDataList.stream().map(positionData -> {
                User user = userRepository.findById(positionData.getUser().getSsn())
                        .orElseThrow(() -> new UserNotFoundException(positionData.getUser().getSsn()));
                positionData.setUser(user);
                return positionData;
            }).forEach(positionData -> positionDataRepository.save(positionData));
        }

        @After
        public void tearDown() throws Exception {
            groupRequest = null;
            filterStatementList = null;
            result = null;
        }

        /**
         * Test getAggregatedData on all the parameters defined
         * @throws Exception no exception expected
         */
        @Transactional
        @Test
        public void getAggregatedData() throws Exception {
            assertEquals(result,
                    userRepository.getAggregatedData(groupRequest.getAggregatorOperator(),
                            groupRequest.getRequestType(), filterStatementList));
        }

    }

}