package com.poianitibaldizhou.trackme.individualrequestservice.entity;

import com.poianitibaldizhou.trackme.individualrequestservice.util.ResponseType;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Information regarding a response that an user provide w.r.t. a certain request that he has received
 */
@Data
@Entity
public class Response {

    @Id
    @Column(name = "requestID")
    private Long requestID;

    @MapsId
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "requestID")
    private IndividualRequest request;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResponseType response;

    @Column(nullable = false)
    private Timestamp acceptanceTimeStamp;
}