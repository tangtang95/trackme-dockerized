package com.poianitibaldizhou.trackme.sharedataservice.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.poianitibaldizhou.trackme.sharedataservice.util.Views;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * JPA entity object regarding the position data of a specific user
 */
@Data
@Entity
public class PositionData {

    @JsonView(Views.Internal.class)
    @Id
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
    private Double longitude;

    @JsonView(Views.Public.class)
    @Column(nullable = false)
    private Double latitude;

    public static PositionData newPositionData(Long id, Timestamp timestamp, User user, Double latitude, Double longitude){
        PositionData positionData = new PositionData();
        positionData.setId(id);
        positionData.setTimestamp(timestamp);
        positionData.setUser(user);
        positionData.setLatitude(latitude);
        positionData.setLongitude(longitude);
        return positionData;
    }

}
