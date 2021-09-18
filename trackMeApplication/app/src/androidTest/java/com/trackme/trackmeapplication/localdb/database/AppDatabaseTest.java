package com.trackme.trackmeapplication.localdb.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.trackme.trackmeapplication.localdb.dao.EmergencyCallDao;
import com.trackme.trackmeapplication.localdb.dao.HealthDataDao;
import com.trackme.trackmeapplication.localdb.dao.PositionDataDao;
import com.trackme.trackmeapplication.localdb.entity.EmergencyCall;
import com.trackme.trackmeapplication.localdb.entity.HealthData;
import com.trackme.trackmeapplication.localdb.entity.PositionData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class AppDatabaseTest {

    private HealthDataDao healthDataDao;
    private PositionDataDao positionDataDao;
    private EmergencyCallDao emergencyCallDao;

    private AppDatabase appDatabase;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        healthDataDao = appDatabase.getHealthDataDao();
        positionDataDao = appDatabase.getPositionDataDao();
        emergencyCallDao = appDatabase.getEmergencyCallDao();
    }

    @After
    public void tearDown() throws Exception {
        appDatabase = null;
        healthDataDao = null;
        positionDataDao = null;
        emergencyCallDao = null;
    }

    /**
     * Test insert health data when all values are non null
     *
     * @throws Exception no exception expected
     */
    @Test
    public void insertHealthData() throws Exception {
        HealthData inputData = new HealthData();
        inputData.setTimestamp(Timestamp.valueOf("2010-10-10 00:00:00"));
        inputData.setPressureMin(30);
        inputData.setPressureMax(50);
        inputData.setHeartbeat(60);
        inputData.setBloodOxygenLevel(90);
        healthDataDao.insert(inputData);

        List<HealthData> outputData = healthDataDao.getAll();
        assertEquals(1, outputData.size());
        assertEquals(inputData.getTimestamp(), outputData.get(0).getTimestamp());
        assertEquals(inputData.getHeartbeat(), outputData.get(0).getHeartbeat());
        assertEquals(inputData.getPressureMin(), outputData.get(0).getPressureMin());
        assertEquals(inputData.getPressureMax(), outputData.get(0).getPressureMax());
        assertEquals(inputData.getBloodOxygenLevel(), outputData.get(0).getBloodOxygenLevel());
    }

    /**
     * Test insert health data when at least a value is null
     *
     * @throws Exception SQLiteConstraintException
     */
    @Test(expected = SQLiteConstraintException.class)
    public void insertHealthDataFailDueToNullValue() throws Exception {
        HealthData inputData = new HealthData();
        inputData.setTimestamp(Timestamp.valueOf("2010-10-10 00:00:00"));
        inputData.setPressureMin(30);
        inputData.setPressureMax(50);
        inputData.setHeartbeat(null);
        inputData.setBloodOxygenLevel(90);
        healthDataDao.insert(inputData);
    }

    /**
     * Test delete all health data
     *
     * @throws Exception no exception expected
     */
    @Test
    public void deleteAllHealthData() throws Exception {
        HealthData inputData1 = new HealthData();
        inputData1.setTimestamp(Timestamp.valueOf("2010-10-10 00:00:00"));
        inputData1.setPressureMin(30);
        inputData1.setPressureMax(50);
        inputData1.setHeartbeat(60);
        inputData1.setBloodOxygenLevel(90);
        healthDataDao.insert(inputData1);

        HealthData inputData2 = new HealthData();
        inputData2.setTimestamp(Timestamp.valueOf("2010-10-10 00:00:00"));
        inputData2.setPressureMin(30);
        inputData2.setPressureMax(50);
        inputData2.setHeartbeat(60);
        inputData2.setBloodOxygenLevel(90);
        healthDataDao.insert(inputData2);

        List<HealthData> outputData = healthDataDao.getAll();
        assertEquals(2, outputData.size());
        healthDataDao.deleteAll();
        assertEquals(Collections.emptyList(), healthDataDao.getAll());
    }

    /**
     * Test insert position data when all values are non null
     *
     * @throws Exception no exception expected
     */
    @Test
    public void insertPositionData() throws Exception {
        PositionData inputData = new PositionData();
        inputData.setLatitude(30.0);
        inputData.setLongitude(20.0);
        positionDataDao.insert(inputData);

        List<PositionData> outputData = positionDataDao.getAll();
        assertEquals(1, outputData.size());
        assertEquals(inputData.getTimestamp(), outputData.get(0).getTimestamp());
        assertEquals(inputData.getLatitude(), outputData.get(0).getLatitude());
        assertEquals(inputData.getLongitude(), outputData.get(0).getLongitude());
    }

    /**
     * Test insert position data when at least a value is null
     *
     * @throws Exception SQLiteConstraintException
     */
    @Test(expected = SQLiteConstraintException.class)
    public void insertPositionDataFailDueToNullValue() throws Exception {
        PositionData inputData = new PositionData();
        inputData.setLatitude(null);
        inputData.setLongitude(20.0);
        positionDataDao.insert(inputData);
    }

    /**
     * Test delete all position data
     *
     * @throws Exception no exception expected
     */
    @Test
    public void deleteAllPositionData() throws Exception {
        PositionData inputData1 = new PositionData();
        inputData1.setLatitude(30.0);
        inputData1.setLongitude(20.0);
        positionDataDao.insert(inputData1);

        PositionData inputData2 = new PositionData();
        inputData2.setLatitude(30.0);
        inputData2.setLongitude(20.0);
        positionDataDao.insert(inputData2);

        List<PositionData> outputData = positionDataDao.getAll();
        assertEquals(2, outputData.size());
        positionDataDao.deleteAll();
        assertEquals(Collections.emptyList(), positionDataDao.getAll());
    }

    /**
     * Test insert emergency call when all values are non null
     *
     * @throws Exception no exception expected
     */
    @Test
    public void insertEmergencyCall() throws Exception {
        EmergencyCall input = new EmergencyCall();
        input.setPhoneNumber("911");
        input.setTimestamp(Timestamp.valueOf("2010-10-10 00:00:00"));
        emergencyCallDao.insert(input);

        List<EmergencyCall> output = emergencyCallDao.getAll();
        assertEquals(1, output.size());
        assertEquals(input.getPhoneNumber(), output.get(0).getPhoneNumber());
        assertEquals(input.getTimestamp(), output.get(0).getTimestamp());
    }

    /**
     * Test get number of recent calls (those within a hour)
     *
     * @throws Exception no exception expected
     */
    @Test
    public void getNumberOfRecentCalls() throws Exception {
        EmergencyCall input1 = new EmergencyCall();
        input1.setPhoneNumber("911");
        Calendar calendar1 = new GregorianCalendar(TimeZone.getTimeZone("GMT+0"));
        calendar1.add(Calendar.HOUR, -2);
        calendar1.add(Calendar.MINUTE, 1);
        input1.setTimestamp(new Timestamp(calendar1.getTime().getTime()));
        emergencyCallDao.insert(input1);

        EmergencyCall input2 = new EmergencyCall();
        input2.setPhoneNumber("911");
        input2.setTimestamp(Timestamp.valueOf("2010-10-10 00:00:00"));
        emergencyCallDao.insert(input2);

        EmergencyCall input3 = new EmergencyCall();
        input3.setPhoneNumber("911");
        input3.setTimestamp(Timestamp.valueOf("2010-12-16 00:00:00"));
        emergencyCallDao.insert(input3);

        EmergencyCall input4 = new EmergencyCall();
        input4.setPhoneNumber("911");
        Calendar calendar2 = new GregorianCalendar(TimeZone.getTimeZone("GMT+0"));
        calendar2.add(Calendar.HOUR, -2);
        calendar2.add(Calendar.MINUTE, -1);
        input4.setTimestamp(new Timestamp(calendar2.getTime().getTime()));
        emergencyCallDao.insert(input4);

        List<Double> diff = emergencyCallDao.getDifference();

        assertEquals(1L, emergencyCallDao.getNumberOfRecentCalls());
    }

    /**
     * Test delete all recent calls (those within a hour)
     *
     * @throws Exception no exception expected
     */
    @Test
    public void deleteAllRecentCalls() throws Exception{
        EmergencyCall input1 = new EmergencyCall();
        input1.setPhoneNumber("911");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -2);
        calendar.add(Calendar.MINUTE, 1);
        Date now = calendar.getTime();
        input1.setTimestamp(new Timestamp(now.getTime()));
        emergencyCallDao.insert(input1);

        EmergencyCall input2 = new EmergencyCall();
        input2.setPhoneNumber("112");
        input2.setTimestamp(Timestamp.valueOf("2010-10-10 00:00:00"));
        emergencyCallDao.insert(input2);

        EmergencyCall input3 = new EmergencyCall();
        input3.setPhoneNumber("112");
        input3.setTimestamp(Timestamp.valueOf("2010-12-16 00:00:00"));
        emergencyCallDao.insert(input3);

        emergencyCallDao.deleteAllRecentCalls();
        List<EmergencyCall> emergencyCalls = emergencyCallDao.getAll();
        assertEquals(2, emergencyCalls.size());
        for (EmergencyCall ec : emergencyCalls) {
            assertNotEquals("911", ec.getPhoneNumber());
        }
    }
}