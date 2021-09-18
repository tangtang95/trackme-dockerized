package com.poianitibaldizhou.trackme.apigateway.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.poianitibaldizhou.trackme.apigateway.assembler.ThirdPartyCompanyAssembler;
import com.poianitibaldizhou.trackme.apigateway.assembler.ThirdPartyPrivateAssembler;
import com.poianitibaldizhou.trackme.apigateway.service.ThirdPartyAuthenticationService;
import com.poianitibaldizhou.trackme.apigateway.service.ThirdPartyAccountManagerService;
import com.poianitibaldizhou.trackme.apigateway.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Public controller regarding the third parties: methods in there can be accessed without authentication
 */
@RestController
@RequestMapping(Constants.PUBLIC_TP_API)
public class PublicThirdPartyController {

    @Value(Constants.SERVER_ADDRESS)
    private String serverAddress;

    @Value(Constants.PORT)
    private Integer port;

    private ThirdPartyCompanyAssembler thirdPartyCompanyAssembler;
    private ThirdPartyPrivateAssembler thirdPartyPrivateAssembler;
    private ThirdPartyAccountManagerService service;
    private ThirdPartyAuthenticationService thirdPartyAuthenticationService;

    /**
     * Creates a new public rest controller for the third party accounts
     *
     * @param thirdPartyAccountManagerService account manager service that exposes the business functionality and
     *                                  can access persistent data on third parties
     * @param thirdPartyPrivateAssembler  third party assembler useful to assemble information regarding private
     *                                    third parties
     * @param thirdPartyCompanyAssembler third party assembler useful to assemble information regarding third parties
     *                                   that are related with companies
     * @param thirdPartyAuthenticationService authentication service that performs operations regarding the authentication
     *                              of new third parties
     */
    public PublicThirdPartyController(ThirdPartyAccountManagerService thirdPartyAccountManagerService,
                                      ThirdPartyPrivateAssembler thirdPartyPrivateAssembler,
                                      ThirdPartyCompanyAssembler thirdPartyCompanyAssembler,
                                      ThirdPartyAuthenticationService thirdPartyAuthenticationService) {
        this.thirdPartyCompanyAssembler = thirdPartyCompanyAssembler;
        this.thirdPartyPrivateAssembler = thirdPartyPrivateAssembler;
        this.service = thirdPartyAccountManagerService;
        this.thirdPartyAuthenticationService = thirdPartyAuthenticationService;
    }

    /**
     * Register a third party customer as a company
     *
     * @param thirdPartyCompanyWrapper information regarding the customer and its company
     * @return an http 201 created message that contains the newly formed link
     * @throws URISyntaxException due to the creation of a new URI resource
     */
    @JsonView(Views.Public.class)
    @PostMapping(Constants.REGISTER_COMPANY_TP_API)
    public @ResponseBody
    ResponseEntity<?> registerCompanyThirdParty(@JsonView (Views.Secured.class) @RequestBody ThirdPartyCompanyWrapper thirdPartyCompanyWrapper)
            throws URISyntaxException {
        Resource<ThirdPartyCompanyWrapper> resource = thirdPartyCompanyAssembler.
                toResource(service.registerThirdPartyCompany(thirdPartyCompanyWrapper));

        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }

    /**
     * Register a third party customer as a non-company
     *
     * @param thirdPartyPrivateWrapper information regarding the customer and its private detail
     * @return an http 201 created message that contains the newly formed link
     * @throws URISyntaxException due to the creation of a new URI resource
     */
    @JsonView(Views.Public.class)
    @PostMapping(Constants.REGISTER_PRIVATE_TP_API)
    public @ResponseBody
    ResponseEntity<?> registerPrivateThirdParty(
            @JsonView(Views.Secured.class) @RequestBody ThirdPartyPrivateWrapper thirdPartyPrivateWrapper)
            throws URISyntaxException {
        Resource<ThirdPartyPrivateWrapper> resource = thirdPartyPrivateAssembler.
                toResource(service.registerThirdPartyPrivate(thirdPartyPrivateWrapper));

        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }

    /**
     * Log in a third party into the system
     *
     * @param email email of the customer
     * @param password password of the customer
     * @return token associated with the customer and a list of links to the possible actions that a logged
     * third party customer can perform
     */
    @PostMapping(Constants.LOGIN_TP_API)
    @ResponseBody
    public Resource<Object> login(
            @RequestParam(Constants.LOGIN_TP_EMAIL_API_PARAM) final String email,
            @RequestParam(Constants.LOGIN_TP_PW_API_PARAM) final String password) {
        TokenWrapper tokenWrapper = new TokenWrapper();
        tokenWrapper.setToken(thirdPartyAuthenticationService.thirdPartyLogin(email, password)
                .orElseThrow(() -> new BadCredentialsException(Constants.THIRD_PARTY_BAD_CREDENTIAL)));

        return new Resource<>(tokenWrapper, SetUpLinks.getLoggedThirdPartyLinks(serverAddress, port));
    }
}
