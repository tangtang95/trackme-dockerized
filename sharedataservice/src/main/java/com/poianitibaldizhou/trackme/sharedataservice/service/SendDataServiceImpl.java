package com.poianitibaldizhou.trackme.sharedataservice.service;

import com.poianitibaldizhou.trackme.sharedataservice.util.DataWrapper;
import com.poianitibaldizhou.trackme.sharedataservice.entity.HealthData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.PositionData;
import com.poianitibaldizhou.trackme.sharedataservice.exception.UserNotFoundException;
import com.poianitibaldizhou.trackme.sharedataservice.repository.HealthDataRepository;
import com.poianitibaldizhou.trackme.sharedataservice.repository.PositionDataRepository;
import com.poianitibaldizhou.trackme.sharedataservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the send data service.
 */
@Service
public class SendDataServiceImpl implements SendDataService{

    private UserRepository userRepository;
    private HealthDataRepository healthDataRepository;
    private PositionDataRepository positionDataRepository;

    /**
     * Constructor.
     * Initialize SendDataService with autowired @repositories
     *
     * @param userRepository the @repository of users
     * @param healthDataRepository the @repository of health data
     * @param positionDataRepository the @repository of position data
     */
    public SendDataServiceImpl(UserRepository userRepository, HealthDataRepository healthDataRepository,
                               PositionDataRepository positionDataRepository){
        this.userRepository = userRepository;
        this.healthDataRepository = healthDataRepository;
        this.positionDataRepository = positionDataRepository;
    }

    @Transactional
    @Override
    public HealthData sendHealthData(String userId, HealthData healthData) {
        return userRepository.findById(userId).map(user -> {
            healthData.setUser(user);
            return healthDataRepository.save(healthData);
        }).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Transactional
    @Override
    public PositionData sendPositionData(String userId, PositionData positionData) {
        return userRepository.findById(userId).map(user ->{
            positionData.setUser(user);
            return positionDataRepository.save(positionData);
        }).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Transactional
    @Override
    public DataWrapper sendClusterOfData(String userId, DataWrapper data) {
        return userRepository.findById(userId).map(user -> {
            data.getHealthDataList().forEach(healthData -> {
                healthData.setUser(user);
                healthDataRepository.save(healthData);
            });
            data.getPositionDataList().forEach(positionData -> {
                positionData.setUser(user);
                positionDataRepository.save(positionData);
            });
            return data;
        }).orElseThrow(() -> new UserNotFoundException(userId));
    }
}
