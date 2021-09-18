package com.poianitibaldizhou.trackme.individualrequestservice.repository;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for accessing data regarding the users
 */
public interface UserRepository extends JpaRepository<User, String> {
}
