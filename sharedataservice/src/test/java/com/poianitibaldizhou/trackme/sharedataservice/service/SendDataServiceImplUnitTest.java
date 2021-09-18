package com.poianitibaldizhou.trackme.sharedataservice.service;

import com.poianitibaldizhou.trackme.sharedataservice.entity.HealthData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.PositionData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.User;
import com.poianitibaldizhou.trackme.sharedataservice.exception.UserNotFoundException;
import com.poianitibaldizhou.trackme.sharedataservice.repository.HealthDataRepository;
import com.poianitibaldizhou.trackme.sharedataservice.repository.PositionDataRepository;
import com.poianitibaldizhou.trackme.sharedataservice.repository.UserRepository;
import com.poianitibaldizhou.trackme.sharedataservice.util.DataWrapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class SendDataServiceImplUnitTest {

    private static final String USER_1 = "user1";
    private static final String USER_NOT_FOUND = "userNotFound";

    @Mock
    private DataWrapper dataWrapper;

    @Mock
    private User user1;
    @Mock
    private UserRepository userRepository;

    @Mock
    private HealthData healthData1, healthData2, healthData3;
    @Mock
    private HealthDataRepository healthDataRepository;

    @Mock
    private PositionData positionData1, positionData2;
    @Mock
    private PositionDataRepository positionDataRepository;

    @InjectMocks
    private SendDataServiceImpl sendDataService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        setUpUserRepository();
        setUpHealthDataRepository();
        setUpPositionDataRepository();
    }

    private void setUpUserRepository() {
        when(userRepository.findById(USER_1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(USER_NOT_FOUND)).thenReturn(Optional.empty());
    }

    private void setUpHealthDataRepository() {
        List<HealthData> healthDataList = new ArrayList<>();
        healthDataList.add(healthData1);
        healthDataList.add(healthData2);
        healthDataList.add(healthData3);
        when(dataWrapper.getHealthDataList()).thenReturn(healthDataList);
        when(healthDataRepository.save(healthData1)).thenReturn(healthData1);
    }

    private void setUpPositionDataRepository() {
        List<PositionData> positionDataList = new ArrayList<>();
        positionDataList.add(positionData1);
        positionDataList.add(positionData2);
        when(dataWrapper.getPositionDataList()).thenReturn(positionDataList);
        when(positionDataRepository.save(positionData1)).thenReturn(positionData1);
    }

    @After
    public void tearDown() throws Exception {
        sendDataService = null;
        userRepository = null;
        healthDataRepository = null;
        positionDataRepository = null;
        sendDataService = null;
    }

    /**
     * Test of send health data contains which the user exist contains the database
     * @throws Exception no exception expected
     */
    @Test
    public void testSendHealthDataSuccessful() throws Exception {
        sendDataService.sendHealthData(USER_1, healthData1);
        verify(healthDataRepository, times(1)).save(healthData1);
    }

    /**
     * Test of send health data in which the user does not exist
     * @throws Exception UserNotFoundException
     */
    @Test(expected = UserNotFoundException.class)
    public void sendHealthDataUserNotFound() throws Exception {
        sendDataService.sendHealthData(USER_NOT_FOUND, healthData1);
    }


    /**
     * Test of send position data contains which the user exist contains the database
     * @throws Exception no exception expected
     */
    @Test
    public void sendPositionDataSuccessful() throws Exception {
        sendDataService.sendPositionData(USER_1, positionData1);
        verify(positionDataRepository, times(1)).save(positionData1);
    }

    /**
     * Test of send position data contains which the user exist contains the database
     * @throws Exception no exception expected
     */
    @Test(expected = UserNotFoundException.class)
    public void sendPositionDataUserNotFound() throws Exception {
        sendDataService.sendPositionData(USER_NOT_FOUND, positionData1);
    }

    /**
     * Test of send position data and health data contains which the user exist contains the database
     * @throws Exception no exception expected
     */
    @Test
    public void sendClusterOfDataSuccessful() throws Exception {
        sendDataService.sendClusterOfData(USER_1, dataWrapper);
        verify(positionDataRepository, times(1)).save(positionData1);
        verify(positionDataRepository,times(1)).save(positionData2);
        verify(healthDataRepository,times(1)).save(healthData1);
        verify(healthDataRepository, times(1)).save(healthData2);
        verify(healthDataRepository, times(1)).save(healthData3);
    }

    /**
     * Test of send position data and health data contains which the user exist contains the database
     * @throws Exception no exception expected
     */
    @Test(expected = UserNotFoundException.class)
    public void sendClusterOfDataUserNotFound() throws Exception {
        sendDataService.sendClusterOfData(USER_NOT_FOUND, dataWrapper);
    }


}