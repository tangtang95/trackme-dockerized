package com.poianitibaldizhou.trackme.individualrequestservice.entity;

import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestStatus;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Information regarding the individual request
 */
@Data
@Entity
public class IndividualRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IndividualRequestStatus status;

    @Column(nullable = false)
    private Timestamp timestamp;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "ssn", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "third_partyid", nullable = false)
    private ThirdParty thirdParty;

    @Column(nullable = false)
    private String motivation;

    /**
     * Empty constructor
     */
    public IndividualRequest() {

    }

    /**
     * Creates an individual request with pending status
     *
     * @param timestamp timestamp of the request
     * @param startDate the request will allow to access data starting from this date
     * @param endDate the request will allow to access data up to this date
     * @param user the request will allow to acces data regarding the user identified by this ssn
     * @param thirdParty the request is performed by a third party customer identified by this number
     */
    public IndividualRequest(Timestamp timestamp, Date startDate, Date endDate, User user, ThirdParty thirdParty) {
        this.status = IndividualRequestStatus.PENDING;
        this.timestamp = timestamp;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
        this.thirdParty = thirdParty;
        this.motivation = "";
    }
}
