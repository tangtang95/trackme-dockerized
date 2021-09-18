package com.poianitibaldizhou.trackme.sharedataservice.entity;

import com.poianitibaldizhou.trackme.sharedataservice.util.AggregatorOperator;
import com.poianitibaldizhou.trackme.sharedataservice.util.RequestType;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * JPA entity object regarding the group request
 */
@Data
@Entity
public class GroupRequest {

    @Id
    private Long id;

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

}
