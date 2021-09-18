package com.poianitibaldizhou.trackme.apigateway.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.poianitibaldizhou.trackme.apigateway.util.Views;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

/**
 * JPA entity object regarding the user
 */
@Data
@Entity
public class User implements UserDetails {

    @JsonView(Views.Public.class)
    @Id
    @Column(length = 16)
    private String ssn;

    @JsonView(Views.Secured.class)
    @Column(nullable = false, length = 64)
    private String password;

    @JsonView(Views.Public.class)
    @Column(nullable = false, length = 64, unique = true)
    private String username;

    @JsonView(Views.Public.class)
    @Column(nullable = false, length = 50)
    private String firstName;

    @JsonView(Views.Public.class)
    @Column(nullable = false, length = 50)
    private String lastName;

    @JsonView(Views.Public.class)
    @Column(nullable = false)
    private Date birthDate;

    @JsonView(Views.Public.class)
    @Column(nullable = false, length = 150)
    private String birthCity;

    @JsonView(Views.Public.class)
    @Column(nullable = false, length = 100)
    private String birthNation;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
