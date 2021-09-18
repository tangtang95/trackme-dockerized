package com.poianitibaldizhou.trackme.apigateway.entity;

import com.poianitibaldizhou.trackme.apigateway.util.Privilege;
import lombok.Data;
import org.springframework.http.HttpMethod;

import javax.persistence.*;

/**
 * Entity that represents an available api
 */
@Data
@Entity
public class Api {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private String startingUri;

    @Enumerated(EnumType.STRING)
    @Column
    private Privilege privilege;

    @Enumerated(EnumType.STRING)
    @Column
    private HttpMethod httpMethod;
}
