package com.poianitibaldizhou.trackme.sharedataservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

/**
 * JPA entity object regarding the user
 */
@Data
@Entity
public class User {

    @Id
    @Column(length = 16)
    private String ssn;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false)
    private Date birthDate;

    @Column(nullable = false, length = 150)
    private String birthCity;

    @Column(nullable = false, length = 100)
    private String birthNation;


}
