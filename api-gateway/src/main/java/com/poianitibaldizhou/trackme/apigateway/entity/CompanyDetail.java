package com.poianitibaldizhou.trackme.apigateway.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

/**
 * JPA entity object regarding the company details of a third party customer that is a company
 */
@Data
@Entity
public class CompanyDetail {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @MapsId
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "thirdPartyCustomer")
    private ThirdPartyCustomer thirdPartyCustomer;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String dunsNumber;

}
