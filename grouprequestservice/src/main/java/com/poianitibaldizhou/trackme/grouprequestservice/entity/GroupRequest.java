package com.poianitibaldizhou.trackme.grouprequestservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poianitibaldizhou.trackme.grouprequestservice.util.AggregatorOperator;
import com.poianitibaldizhou.trackme.grouprequestservice.util.RequestStatus;
import com.poianitibaldizhou.trackme.grouprequestservice.util.RequestType;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * JPA entity object regarding the group request
 */
@Data
@Entity
public class GroupRequest {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Column(nullable = false)
    private Long thirdPartyId;

    @Column(nullable = false)
    private Timestamp creationTimestamp;

    @Column(length = 20, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AggregatorOperator aggregatorOperator = AggregatorOperator.COUNT;

    @Column(length = 20, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RequestType requestType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;
}
