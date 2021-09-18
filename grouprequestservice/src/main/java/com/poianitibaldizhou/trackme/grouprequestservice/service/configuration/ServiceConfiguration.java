package com.poianitibaldizhou.trackme.grouprequestservice.service.configuration;

import com.poianitibaldizhou.trackme.grouprequestservice.service.InternalCommunicationServiceImpl;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.validation.constraints.Positive;

@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "trackme.group-request")
@EnableConfigurationProperties
@Getter
@Setter
public class ServiceConfiguration {

    @Positive
    private int minimumNumberOfUserInvolved;

    /**
     * Declare a command line runner which sets the minimum number of user involved for internal communication
     * service to the value of a property inside application.properties: trackme.group-request.minimum-number-of-user-involved
     *
     * @param internalCommunicationService the internal communication service bean
     * @return the command line runenr which sets the minimum number of user involved
     */
    @Bean
    public CommandLineRunner setMinNumberOfUserInvolved(InternalCommunicationServiceImpl internalCommunicationService){
        return args -> internalCommunicationService.setMinNumberOfUserInvolved(minimumNumberOfUserInvolved);
    }


}
