package com.poianitibaldizhou.trackme.apigateway.service;

import com.poianitibaldizhou.trackme.apigateway.entity.User;
import com.poianitibaldizhou.trackme.apigateway.exception.AlreadyPresentSsnException;
import com.poianitibaldizhou.trackme.apigateway.exception.AlreadyPresentUsernameException;
import com.poianitibaldizhou.trackme.apigateway.exception.SsnNotFoundException;
import com.poianitibaldizhou.trackme.apigateway.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Implementation of the services regarding the management user accounts
 */
@Slf4j
@Service
public class UserAccountManagerServiceImpl implements UserAccountManagerService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private InternalCommunicationService internalCommunicationService;

    /**
     * Creates the manager of the services regarding the account of the users.
     * It needs a repository in order to make some operations on data (e.g. register a user)
     *
     * @param userRepository repository regarding the users
     * @param passwordEncoder password encoder for encoding password between inserting them in the database
     */
    public UserAccountManagerServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void setInternalCommunicationService(InternalCommunicationService internalCommunicationService){
        this.internalCommunicationService = internalCommunicationService;
    }

    @Transactional
    @Override
    public User getUserBySsn(String ssn) {
        return userRepository.findById(ssn).orElseThrow(() -> new SsnNotFoundException(ssn));
    }

    @Override
    public User getUserByUserName(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Transactional
    @Override
    public User registerUser(User user) {
        if(userRepository.findById(user.getSsn()).isPresent()) {
            throw new AlreadyPresentSsnException(user.getSsn());
        }
        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new AlreadyPresentUsernameException(user.getUsername());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.saveAndFlush(user);

        if(!Objects.isNull(internalCommunicationService)){
            internalCommunicationService.broadcastUserMessage(savedUser);
        } else{
            log.error("FATAL ERROR: InternalCommunicationService null, maybe due to the settings of active profiles");
        }

        return savedUser;
    }

}
