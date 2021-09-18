package com.poianitibaldizhou.trackme.sharedataservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * JPA entity object regarding the individual request accepted
 */
@Data
@Entity
public class IndividualRequest {

    @Id
    private Long id;

    @Column(nullable = false)
    private Timestamp creationTimestamp;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "user_ssn", nullable = false)
    private User user;

    @Column(nullable = false)
    private Long thirdPartyId;

}
