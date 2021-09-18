package com.poianitibaldizhou.trackme.apigateway.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.poianitibaldizhou.trackme.apigateway.assembler.ThirdPartyCompanyAssembler;
import com.poianitibaldizhou.trackme.apigateway.assembler.ThirdPartyPrivateAssembler;
import com.poianitibaldizhou.trackme.apigateway.entity.ThirdPartyCustomer;
import com.poianitibaldizhou.trackme.apigateway.service.ThirdPartyAuthenticationService;
import com.poianitibaldizhou.trackme.apigateway.service.ThirdPartyAccountManagerService;
import com.poianitibaldizhou.trackme.apigateway.util.Constants;
import com.poianitibaldizhou.trackme.apigateway.util.ThirdPartyCompanyWrapper;
import com.poianitibaldizhou.trackme.apigateway.util.ThirdPartyPrivateWrapper;
import com.poianitibaldizhou.trackme.apigateway.util.Views;
import org.springframework.hateoas.Resource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Entry point for accessing the service that regards the account of third parties that are secured.
 * Indeed, authentication is needed in order to access the methods provided here.
 */
@RestController
@RequestMapping(path = Constants.SECURED_TP_API)
public class SecuredThirdPartyController {

    private final ThirdPartyAccountManagerService service;

    private final ThirdPartyCompanyAssembler thirdPartyCompanyAssembler;

    private final ThirdPartyPrivateAssembler thirdPartyPrivateAssembler;

    private final ThirdPartyAuthenticationService thirdPartyAuthenticationService;

    /**
     * Creates a new entry point for accessing the services that regard the accounts
     * of third party customers
     *
     * @param service             service that manages the third party customers accounts: needed
     *                            in order to access the business function of the service
     * @param thirdPartyCompanyAssembler assembler for third party customer related with companies, that adds hal content
     * @param thirdPartyPrivateWrapper assembler for third party customer non-related with companies, that adds hal content
     * @param thirdPartyAuthenticationService service that manages the authentication of third parties
     */
    SecuredThirdPartyController(ThirdPartyAccountManagerService service,
                                ThirdPartyCompanyAssembler thirdPartyCompanyAssembler,
                                ThirdPartyPrivateAssembler thirdPartyPrivateWrapper,
                                ThirdPartyAuthenticationService thirdPartyAuthenticationService) {
        this.service = service;
        this.thirdPartyCompanyAssembler = thirdPartyCompanyAssembler;
        this.thirdPartyPrivateAssembler = thirdPartyPrivateWrapper;
        this.thirdPartyAuthenticationService = thirdPartyAuthenticationService;
    }

    /**
     * This method will return information regarding the third party customer specified
     *
     * @param thirdPartyCustomer information of third party customer will be retrieved
     * @return contains information regarding the third party customer and, either its company details or
     * its private detail (note that both is impossible)
     */
    @JsonView(Views.Public.class)
    @GetMapping(path = Constants.GET_TP_INFO_API)
    public @ResponseBody
    Resource<Object> getThirdParty(@AuthenticationPrincipal final ThirdPartyCustomer thirdPartyCustomer) {
        String email = thirdPartyCustomer.getEmail();
        Optional<ThirdPartyCompanyWrapper> thirdPartyCompanyWrapper = service.getThirdPartyCompanyByEmail(email);
        if(thirdPartyCompanyWrapper.isPresent()) {
            return new Resource<>(thirdPartyCompanyAssembler.toResource(thirdPartyCompanyWrapper.get()));
        }

        Optional<ThirdPartyPrivateWrapper> thirdPartyPrivateWrapper = service.getThirdPartyPrivateByEmail(email);
        if(thirdPartyPrivateWrapper.isPresent()) {
            return new Resource<>(thirdPartyPrivateAssembler.toResource(thirdPartyPrivateWrapper.get()));
        }

        throw new IllegalStateException();
    }

    /**
     * Logout the third party that has called the method
     *
     * @param thirdPartyCustomer customer to logout
     * @return true
     */
    @PostMapping(Constants.LOGOUT_TP_API)
    public @ResponseBody boolean logout(@NotNull @AuthenticationPrincipal final ThirdPartyCustomer thirdPartyCustomer) {
        thirdPartyAuthenticationService.thirdPartyLogout(thirdPartyCustomer);
        return true;
    }
}

