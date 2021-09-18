package com.trackme.trackmeapplication.service.util;

import com.trackme.trackmeapplication.service.exception.InvalidHealthDataException;
import com.trackme.trackmeapplication.localdb.entity.HealthData;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

@RunWith(Enclosed.class)
public class HealthDataInspectorImplTest {

    private static HealthData newHealthData(Timestamp timestamp, Integer heartbeat, Integer pressureMin,
                                            Integer pressureMax, Integer bloodOxygenLevel) {
        HealthData healthData = new HealthData();
        healthData.setTimestamp(timestamp);
        healthData.setPressureMin(pressureMin);
        healthData.setHeartbeat(heartbeat);
        healthData.setPressureMax(pressureMax);
        healthData.setBloodOxygenLevel(bloodOxygenLevel);
        return healthData;
    }

    private static Date getDate(int year, int month, int day) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(year, month, day);
        return calendar.getTime();
    }


    @RunWith(Parameterized.class)
    public static class ParameterTest {

        private HealthDataInspector healthDataInspector;
        private HealthData healthData;
        private boolean resultTest;

        public ParameterTest(Date birthDate, HealthData healthData, boolean isGraveCondition) {
            this.healthDataInspector = new HealthDataInspectorImpl(birthDate);
            this.healthData = healthData;
            this.resultTest = isGraveCondition;
        }


        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    // 0: Heartbeat lower than the lower bound by 1
                    {
                            getDate(1990, 7, 5),
                            newHealthData(Timestamp.valueOf("2018-10-10 00:00:00"), 29, 60, 90, 95),
                            true
                    },
                    // 1: Heartbeat equals the lower bound
                    {
                            getDate(1990, 7, 5),
                            newHealthData(Timestamp.valueOf("2018-10-10 00:00:00"), 30, 60, 90, 95),
                            false
                    },
                    // 2: Heartbeat equals upper bound
                    {
                            getDate(1990, 7, 5),
                            newHealthData(Timestamp.valueOf("2020-10-10 00:00:00"), 190, 60, 90, 95),
                            false
                    },
                    // 3: Heartbeat greater than upper bound by 1
                    {
                            getDate(1990, 7, 5),
                            newHealthData(Timestamp.valueOf("2020-10-10 00:00:00"), 191, 60, 90, 95),
                            true
                    },
                    // 4: PressureMin less than lower bound by 1
                    {
                            getDate(1990, 7, 5),
                            newHealthData(Timestamp.valueOf("2020-10-10 00:00:00"), 60, 39, 90, 95),
                            true
                    },
                    // 5: ressureMin equals lower bound
                    {
                            getDate(1990, 7, 5),
                            newHealthData(Timestamp.valueOf("2020-10-10 00:00:00"), 60, 40, 90, 95),
                            false
                    },
                    // 6: PressureMin equals upper bound
                    {
                            getDate(1990, 7, 5),
                            newHealthData(Timestamp.valueOf("2020-10-10 00:00:00"), 60, 100, 110, 95),
                            false
                    },
                    // 7: PressureMin greater than upper bound by 1
                    {
                            getDate(1990, 7, 5),
                            newHealthData(Timestamp.valueOf("2020-10-10 00:00:00"), 60, 101, 110, 95),
                            true
                    },
                    // 8: Pressure max problem
                    {
                            getDate(1990, 7, 5),
                            newHealthData(Timestamp.valueOf("2020-10-10 00:00:00"), 60, 60, 220, 95),
                            true
                    },
                    // 9: Blood oxygen level problem problem
                    {
                            getDate(1990, 7, 5),
                            newHealthData(Timestamp.valueOf("2020-10-10 00:00:00"), 60, 60, 220, 70),
                            true
                    },
                    // 10: 2 Health problems: heart beat + pressure min
                    {
                            getDate(1990, 7, 5),
                            newHealthData(Timestamp.valueOf("2020-10-10 00:00:00"), 28, 22, 100, 95),
                            true
                    },
                    // 11: 3 Health problems: pressure max + pressure min + blood oxygen level
                    {
                            getDate(1990, 7, 5),
                            newHealthData(Timestamp.valueOf("2020-10-10 00:00:00"), 60, 22, 220, 70),
                            true
                    },
                    // 12: 4 Health problems
                    {
                            getDate(1990, 7, 5),
                            newHealthData(Timestamp.valueOf("2020-10-10 00:00:00"), 22, 22, 220, 70),
                            true
                    }
            });
        }

        @Test
        public void isGraveConditionHeartBeatProblem() throws Exception {
            assertEquals(resultTest, healthDataInspector.isGraveCondition(healthData));
        }
    }

    public static class NonParameterTest {

        private HealthDataInspector healthDataInspector;

        @Before
        public void setUp() throws Exception {
            this.healthDataInspector = new HealthDataInspectorImpl(getDate(1995, 10, 2));
        }

        @Test
        public void isGraveConditionInvalidDateSinceTimestampBeforeBirthDate() throws Exception {
            HealthData healthData = newHealthData(Timestamp.valueOf("1995-10-1 00:00:00"), 1, 30, 100, 95);
            assertFalse(healthDataInspector.isGraveCondition(healthData));
        }

        @Test(expected = InvalidHealthDataException.class)
        public void isGraveConditionInvalidDataDueToPressure() throws Exception {
            HealthData healthData = newHealthData(Timestamp.valueOf("2010-10-3 00:00:00"), 1, 10, 9, 95);
            healthDataInspector.isGraveCondition(healthData);
        }

        @Test(expected = InvalidHealthDataException.class)
        public void isGraveConditionInvalidDataDueToBloodOxygenLevelGreater100() throws Exception {
            HealthData healthData = newHealthData(Timestamp.valueOf("2010-10-3 00:00:00"), 1, 10, 100, 101);
            healthDataInspector.isGraveCondition(healthData);
        }

        @Test(expected = InvalidHealthDataException.class)
        public void isGraveConditionInvalidDataDueToBloodOxygenLevelLess0() throws Exception {
            HealthData healthData = newHealthData(Timestamp.valueOf("2010-10-3 00:00:00"), 1, 10, 100, -1);
            healthDataInspector.isGraveCondition(healthData);
        }

        @Test(expected = InvalidHealthDataException.class)
        public void isGraveConditionInvalidDataDueToNullValue1() throws Exception {
            HealthData healthData = newHealthData(Timestamp.valueOf("2010-10-3 00:00:00"), null, 10, 100, 95);
            healthDataInspector.isGraveCondition(healthData);
        }

        @Test(expected = InvalidHealthDataException.class)
        public void isGraveConditionInvalidDataDueToNullValue2() throws Exception {
            HealthData healthData = newHealthData(Timestamp.valueOf("2010-10-3 00:00:00"), 10, null, 100, 95);
            healthDataInspector.isGraveCondition(healthData);
        }

        @Test(expected = InvalidHealthDataException.class)
        public void isGraveConditionInvalidDataDueToNullValue3() throws Exception {
            HealthData healthData = newHealthData(Timestamp.valueOf("2010-10-3 00:00:00"), 10, 10, null, 95);
            healthDataInspector.isGraveCondition(healthData);
        }

        @Test(expected = InvalidHealthDataException.class)
        public void isGraveConditionInvalidDataDueToNullValue4() throws Exception {
            HealthData healthData = newHealthData(Timestamp.valueOf("2010-10-3 00:00:00"), 10, 10, 100, null);
            healthDataInspector.isGraveCondition(healthData);
        }

        @Test(expected = InvalidHealthDataException.class)
        public void isGraveConditionInvalidDataDueToNullValue5() throws Exception {
            HealthData healthData = newHealthData(Timestamp.valueOf("2010-10-3 00:00:00"), null, null, 100, 95);
            healthDataInspector.isGraveCondition(healthData);
        }

        @Test(expected = InvalidHealthDataException.class)
        public void isGraveConditionInvalidDataDueToNullValue6() throws Exception {
            HealthData healthData = newHealthData(Timestamp.valueOf("2010-10-3 00:00:00"), null, null, null, 95);
            healthDataInspector.isGraveCondition(healthData);
        }

        @Test(expected = InvalidHealthDataException.class)
        public void isGraveConditionInvalidDataDueToNullValue7() throws Exception {
            HealthData healthData = newHealthData(Timestamp.valueOf("2010-10-3 00:00:00"), null, null, null, null);
            healthDataInspector.isGraveCondition(healthData);
        }

        @Test(expected = InvalidHealthDataException.class)
        public void isGraveConditionInvalidDataDueToNullValue8() throws Exception {
            HealthData healthData = newHealthData(null, 10, 10, 100, 95);
            healthDataInspector.isGraveCondition(healthData);
        }

        @Test(expected = InvalidHealthDataException.class)
        public void isGraveConditionInvalidDataDueToNullValue9() throws Exception {
            HealthData healthData = new HealthData();
            healthDataInspector.isGraveCondition(healthData);
        }

    }


}