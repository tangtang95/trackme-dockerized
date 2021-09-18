package com.poianitibaldizhou.trackme.individualrequestservice.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Information useful about the third party: private or company type.
 */
@Entity
@Data
public class ThirdParty {

    @Id
    private Long id;

    @Column(nullable = false)
    private String identifierName;

    /**
     * Empty constructor used by hibernate
     */
    public ThirdParty() {

    }

    /**
     * Constructor.
     * Create a new third party with specific id
     * @param id the id of the third party
     */
    public ThirdParty(Long id) {
        this.id = id;
    }

    /**
     * Constructor.
     * Create a new third party with specific id and identifier name
     * @param id the id of the third party
     * @param identifierName the identifier name of the third party
     */
    public ThirdParty(Long id, String identifierName){
        this.id = id;
        this.identifierName = identifierName;
    }

}
