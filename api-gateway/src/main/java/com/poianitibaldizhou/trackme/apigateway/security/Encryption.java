package com.poianitibaldizhou.trackme.apigateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration of the beans used for the encryption operations
 */
@Configuration
public class Encryption {

    /**
     * Password encoder bean that allows to store encrypted password in the database
     *
     * @return BCrypt encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
