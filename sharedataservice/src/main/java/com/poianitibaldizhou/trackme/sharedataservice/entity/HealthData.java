package com.poianitibaldizhou.trackme.sharedataservice.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.poianitibaldizhou.trackme.sharedataservice.util.Views;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * JPA entity object regarding the health data of a specific user
 */
@Data
@Entity
public class HealthData {

    @Id
    @JsonView(Views.Internal.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonView(Views.Internal.class)
    @ManyToOne
    @JoinColumn(name = "user_ssn", nullable = false)
    private User user;

    @JsonView(Views.Public.class)
    @Column(nullable = false)
    private Timestamp timestamp;

    @JsonView(Views.Public.class)
    @Column(nullable = false)
    private Integer heartBeat;

    @JsonView(Views.Public.class)
    @Column(nullable = false)
    private Integer bloodOxygenLevel;

    @JsonView(Views.Public.class)
    @Column(nullable = false)
    private Integer pressureMin;

    @JsonView(Views.Public.class)
    @Column(nullable = false)
    private Integer pressureMax;

    public static HealthData newHealthData(Long id, Timestamp timestamp, User user, Integer heartbeat,
                                           Integer pressureMin, Integer pressureMax, Integer bloodOxygenLevel){
        HealthData healthData = new HealthData();
        healthData.setId(id);
        healthData.setTimestamp(timestamp);
        healthData.setUser(user);
        healthData.setHeartBeat(heartbeat);
        healthData.setPressureMin(pressureMin);
        healthData.setPressureMax(pressureMax);
        healthData.setBloodOxygenLevel(bloodOxygenLevel);
        return healthData;
    }


}
