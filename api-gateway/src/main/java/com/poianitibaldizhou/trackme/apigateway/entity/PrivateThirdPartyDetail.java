package com.poianitibaldizhou.trackme.apigateway.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.sql.Date;

/**
 * JPA entity object regarding the company details of a third party customer that is private person, and
 * not a company
 */
@Data
@Entity
public class PrivateThirdPartyDetail {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @MapsId
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "thirdPartyCustomer")
    private ThirdPartyCustomer thirdPartyCustomer;

    @Column(nullable = false, unique = true, length = 16)
    private String ssn;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private Date birthDate;

    @Column(nullable = false)
    private String birthCity;

}
