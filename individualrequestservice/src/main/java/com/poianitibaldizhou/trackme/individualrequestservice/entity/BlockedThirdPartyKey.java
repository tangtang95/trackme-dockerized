package com.poianitibaldizhou.trackme.individualrequestservice.entity;

import lombok.EqualsAndHashCode;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Key for the BlockedThirdParty entity
 */
@Embeddable
@EqualsAndHashCode
public class BlockedThirdPartyKey implements Serializable {

    @ManyToOne
    @JoinColumn(name = "third_party", nullable = false)
    private ThirdParty thirdParty;

    @ManyToOne
    @JoinColumn(name = "ssn", nullable = false)
    private User user;

    /**
     * Creates a key for the BlockedThirdParty entity.
     * A key is formed by two fields: thirdParty and ssn.
     *
     * @param thirdParty third party that will be blocked to access data regarding user specified by the ssn
     * @param user user that blocked the third party identified by thirdParty
     */
    public BlockedThirdPartyKey(ThirdParty thirdParty, User user) {
        this.thirdParty = thirdParty;
        this.user = user;
    }

    /**
     * Empty constructor used by hibernate
     */
    public BlockedThirdPartyKey() {

    }

    public ThirdParty getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(ThirdParty thirdParty) {
        this.thirdParty = thirdParty;
    }

    public User getUser() {
        return user;
    }

    public void setSsn(User user) {
        this.user = user;
    }

}
