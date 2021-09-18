package com.poianitibaldizhou.trackme.apigateway.service;

import com.poianitibaldizhou.trackme.apigateway.entity.ThirdPartyCustomer;
import com.poianitibaldizhou.trackme.apigateway.entity.User;
import com.poianitibaldizhou.trackme.apigateway.util.Constants;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the authentication service based on UUID tokens
 */
@Service
public class UUIDAuthenticationService implements UserAuthenticationService, ThirdPartyAuthenticationService {

    private final PasswordEncoder passwordEncoder;

    private final UserAccountManagerService userAccountManagerService;
    private final HashMap<String, User> mapUserByToken = new HashMap<>();
    private final HashMap<String, String> mapTokenByUsername = new HashMap<>();

    private final ThirdPartyAccountManagerService thirdPartyAccountManagerService;
    private final HashMap<String, ThirdPartyCustomer> mapThirdPartyByToken = new HashMap<>();
    private final HashMap<String, String> mapTokenByEmail = new HashMap<>();

    /**
     * Creates a new authentication service based on UUID tokens
     *
     * @param userAccountManagerService account manager service for accessing persistent data regarding the accounts
     * @param thirdPartyAccountManagerService account manager service for accessing persisted data regarding the accounts
     * @param passwordEncoder password encoder for encoding password when authentication
     */
    public UUIDAuthenticationService(UserAccountManagerService userAccountManagerService,
                                     ThirdPartyAccountManagerService thirdPartyAccountManagerService,
                                     PasswordEncoder passwordEncoder) {
        this.userAccountManagerService = userAccountManagerService;
        this.thirdPartyAccountManagerService = thirdPartyAccountManagerService;
        this.passwordEncoder = passwordEncoder;
    }

    // USER AUTHENTICATION METHODS

    @Transactional
    @Override
    public Optional<String> userLogin(String username, String password) {
        // Generates the token
        final String uuid = getUUIDToken();

        // Check credentials and if the user is already logged
        User user = userAccountManagerService.getUserByUserName(username);

        if(passwordEncoder.matches(password, user.getPassword()) && !mapUserByToken.values().contains(user)) {
            mapUserByToken.put(uuid, user);
            mapTokenByUsername.put(username, uuid);
        } else if(passwordEncoder.matches(password, user.getPassword()) && mapUserByToken.values().contains(user)) {
            mapUserByToken.remove(mapTokenByUsername.remove(username));
            mapTokenByUsername.put(username, uuid);
            mapUserByToken.put(uuid, user);
        } else {
            return Optional.empty();
        }

        return Optional.of(uuid);
    }

    @Transactional
    @Override
    public Optional<User> findUserByToken(final String token) {
        return Optional.ofNullable(mapUserByToken.get(token));
    }

    @Transactional
    @Override
    public void userLogout(final User user) {
        String token = mapTokenByUsername.get(user.getUsername());
        if(token == null)
            throw new IllegalStateException();

        mapTokenByUsername.remove(user.getUsername());
        mapUserByToken.remove(token);
    }


    // THIRD PARTY AUTHENTICATION METHODS

    @Transactional
    @Override
    public Optional<String> thirdPartyLogin(String email, String password) {
        // Generates the token
        final String uuid = getUUIDToken();

        // Check credentials and if the third party customer is already logged
        ThirdPartyCustomer thirdPartyCustomer = thirdPartyAccountManagerService.getThirdPartyByEmail(email);

        if(!mapThirdPartyByToken.values().contains(thirdPartyCustomer) && passwordEncoder.matches(password, thirdPartyCustomer.getPassword())) {
            mapThirdPartyByToken.put(uuid, thirdPartyCustomer);
            mapTokenByEmail.put(email, uuid);
        } else if(mapThirdPartyByToken.values().contains(thirdPartyCustomer) &&
                passwordEncoder.matches(password, thirdPartyCustomer.getPassword())) {
            mapThirdPartyByToken.remove(mapTokenByEmail.remove(email));
            mapThirdPartyByToken.put(uuid, thirdPartyCustomer);
            mapTokenByEmail.put(email, uuid);
        } else {
            return Optional.empty();
        }

        return Optional.of(uuid);
    }

    @Transactional
    @Override
    public Optional<ThirdPartyCustomer> findThirdPartyByToken(String token) {
        return Optional.ofNullable(mapThirdPartyByToken.get(token));
    }

    @Transactional
    @Override
    public void thirdPartyLogout(ThirdPartyCustomer thirdPartyCustomer) {
        String token = mapTokenByEmail.get(thirdPartyCustomer.getEmail());
        if(token == null)
            throw new IllegalStateException();

        mapTokenByEmail.remove(thirdPartyCustomer.getEmail());
        mapThirdPartyByToken.remove(token);
    }

    @Override
    public UserDetails findUserDetailsByToken(String token) {
        Optional<User> user = findUserByToken(token);
        Optional<ThirdPartyCustomer> thirdPartyCustomer = findThirdPartyByToken(token);

        if(user.isPresent()) {
            return user.orElseThrow(()-> new UsernameNotFoundException(Constants.LOGGED_USER_NOT_FOUND + token));
        } else if(thirdPartyCustomer.isPresent()) {
            return thirdPartyCustomer.orElseThrow(() -> new UsernameNotFoundException(Constants.LOGGED_USER_NOT_FOUND + token));
        }

        throw new UsernameNotFoundException(Constants.LOGGED_USER_NOT_FOUND + token);
    }

    /**
     * Assure that a new token is being generated and not one that is already present
     *
     * @return token that is not assigned to any client
     */
    private String getUUIDToken() {
        String token;

        do {
            token = UUID.randomUUID().toString();
        } while(mapUserByToken.containsKey(token) || mapThirdPartyByToken.containsKey(token));

        return token;
    }
}
