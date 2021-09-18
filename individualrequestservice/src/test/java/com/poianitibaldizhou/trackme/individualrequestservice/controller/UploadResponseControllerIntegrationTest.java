package com.poianitibaldizhou.trackme.individualrequestservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poianitibaldizhou.trackme.individualrequestservice.IndividualRequestServiceApplication;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.BlockedThirdPartyKey;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.ThirdParty;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.User;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.*;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.BlockedThirdPartyRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.IndividualRequestRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.ResponseRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.util.Constants;
import com.poianitibaldizhou.trackme.individualrequestservice.util.ExceptionResponseBody;
import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestStatus;
import com.poianitibaldizhou.trackme.individualrequestservice.util.ResponseType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Integration test for the upload request service
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IndividualRequestServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@ActiveProfiles("test")
@Sql("classpath:ControllerIntegrationTest.sql")
public class UploadResponseControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private IndividualRequestRepository requestRepository;

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private BlockedThirdPartyRepository blockedThirdPartyRepository;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    private HttpHeaders httpHeaders;

    @Before
    public void setUp() {
        httpHeaders = new HttpHeaders();
    }

    @After
    public void tearDown() {
        httpHeaders = null;
    }

    // TEST ADD RESPONSE METHOD

    /**
     * Test the add of a response when the header throws an impossble access
     *
     * @throws Exception due to json mapping
     */
    @Test
    public void testAddResponseWhenWrongHeader() throws Exception {
        httpHeaders.set(Constants.HEADER_USER_SSN, "user2");
        String responseType = ResponseType.ACCEPT.toString();

        HttpEntity<String> entity = new HttpEntity<>(responseType, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(Constants.RESPONSE_API+ "/requests/1"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        ObjectMapper mapper = new ObjectMapper();
        ExceptionResponseBody exceptionResponseBody = mapper.readValue(response.getBody(), ExceptionResponseBody.class);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), exceptionResponseBody.getStatus());
        assertEquals(HttpStatus.UNAUTHORIZED.toString(), exceptionResponseBody.getError());
        assertEquals(new ImpossibleAccessException().getMessage(), exceptionResponseBody.getMessage());

    }

    /**
     * Test the add of an acceptance response to an individual request
     * @throws Exception unsuccessful insertion of the new response
     */
    @Test
    public void testAddAcceptResponse() throws Exception {
        httpHeaders.set(Constants.HEADER_USER_SSN, "user1");
        String responseType = ResponseType.ACCEPT.toString();

        HttpEntity<String> entity = new HttpEntity<>(responseType, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(Constants.RESPONSE_API+ "/requests/1"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(responseRepository.findById(1L).isPresent());
        assertTrue(responseRepository.findById(1L).orElseThrow(Exception::new).getResponse().equals(ResponseType.ACCEPT));
        assertTrue(requestRepository.findById(1L).orElseThrow(Exception::new).getStatus().equals(IndividualRequestStatus.ACCEPTED));
    }

    /**
     * Test the add of a refusing response to an individual request
     * @throws Exception unsuccessful insertion of the new response
     */
    @Test
    public void testAddRefuseResponse() throws Exception {
        httpHeaders.set(Constants.HEADER_USER_SSN, "user1");
        String responseType = ResponseType.REFUSE.toString();

        HttpEntity<String> entity = new HttpEntity<>(responseType, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(Constants.RESPONSE_API+"/requests/1"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(responseRepository.findById(1L).isPresent());
        assertTrue(responseRepository.findById(1L).orElseThrow(Exception::new).getResponse().equals(ResponseType.REFUSE));
        assertTrue(requestRepository.findById(1L).orElseThrow(Exception::new).getStatus().equals(IndividualRequestStatus.REFUSED));
    }

    /**
     * Test the add of a response to a non existing individual request
     */
    @Test
    public void testAddResponseOfNonExistingRequest() throws IOException {
        httpHeaders.set(Constants.HEADER_USER_SSN, "user1");
        String responseType = ResponseType.ACCEPT.toString();

        HttpEntity<String> entity = new HttpEntity<>(responseType, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(Constants.RESPONSE_API+"/requests/100"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ObjectMapper mapper = new ObjectMapper();
        ExceptionResponseBody exceptionResponseBody = mapper.readValue(response.getBody(), ExceptionResponseBody.class);

        assertEquals(HttpStatus.NOT_FOUND.value(), exceptionResponseBody.getStatus());
        assertEquals(HttpStatus.NOT_FOUND.toString(), exceptionResponseBody.getError());
        assertEquals(new RequestNotFoundException(100L).getMessage(), exceptionResponseBody.getMessage());
    }


    // TEST BLOCK THIRD PARTY METHOD

    /**
     * Test the add of a block by a certain user for a certain third party customer, when the user is not
     * registered
     */
    @Test
    public void testAddBlockWhenUserNotFound() throws IOException {
        httpHeaders.set(Constants.HEADER_USER_SSN,"nonRegisteredUser");
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(Constants.RESPONSE_API+"/blockedThirdParty/thirdparties/100"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ObjectMapper mapper = new ObjectMapper();
        ExceptionResponseBody exceptionResponseBody = mapper.readValue(response.getBody(), ExceptionResponseBody.class);

        assertEquals(HttpStatus.NOT_FOUND.value(), exceptionResponseBody.getStatus());
        assertEquals(HttpStatus.NOT_FOUND.toString(), exceptionResponseBody.getError());
        assertEquals(new UserNotFoundException(new User("nonRegisteredUser")).getMessage(), exceptionResponseBody.getMessage());
    }

    /**
     * Test the add of a block by a certain user for a certain third party customer, when the third party customer
     * never sent a request toward that user
     */
    @Test
    public void testAddBlockWhenNoRequest() throws IOException {
        httpHeaders.set(Constants.HEADER_USER_SSN, "user1");
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(Constants.RESPONSE_API+"/blockedThirdParty/thirdparties/10"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ObjectMapper mapper = new ObjectMapper();
        ExceptionResponseBody exceptionResponseBody = mapper.readValue(response.getBody(), ExceptionResponseBody.class);

        assertEquals(HttpStatus.NOT_FOUND.value(), exceptionResponseBody.getStatus());
        assertEquals(HttpStatus.NOT_FOUND.toString(), exceptionResponseBody.getError());
        assertEquals(new ThirdPartyNotFoundException(10L).getMessage(), exceptionResponseBody.getMessage());
    }

    /**
     * Test the add of a block by a certain user for a certain third party customer, when that user has already
     * blocked the specified third party customer
     */
    @Test
    public void testAddBlockWhenBlockAlreadyPresent() throws IOException {
        httpHeaders.set(Constants.HEADER_USER_SSN,"user5");
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(Constants.RESPONSE_API+"/blockedThirdParty/thirdparties/4"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ObjectMapper mapper = new ObjectMapper();
        ExceptionResponseBody exceptionResponseBody = mapper.readValue(response.getBody(), ExceptionResponseBody.class);

        assertEquals(HttpStatus.BAD_REQUEST.value(), exceptionResponseBody.getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.toString(), exceptionResponseBody.getError());
        assertEquals(new BlockAlreadyPerformedException(4L).getMessage(), exceptionResponseBody.getMessage());
    }

    /**
     * Test the add of a block by a certain user for a certain third party customer when no refused
     * request of that user performed by the customer is present
     */
    @Test
    public void testAddBlockWhenNoRefusedRequestPresent() throws IOException {
        httpHeaders.set(Constants.HEADER_USER_SSN, "user1");
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(Constants.RESPONSE_API+"/blockedThirdParty/thirdparties/1"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ObjectMapper mapper = new ObjectMapper();
        ExceptionResponseBody exceptionResponseBody = mapper.readValue(response.getBody(), ExceptionResponseBody.class);

        assertEquals(HttpStatus.NOT_FOUND.value(), exceptionResponseBody.getStatus());
        assertEquals(HttpStatus.NOT_FOUND.toString(), exceptionResponseBody.getError());
        assertEquals(new ThirdPartyRefusedRequestNotFoundException(1L).getMessage(), exceptionResponseBody.getMessage());

    }

    /**
     * Test the add of a block
     */
    @Test
    public void testAddBlock() {
        httpHeaders.set(Constants.HEADER_USER_SSN, "user17");
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(Constants.RESPONSE_API+"/blockedThirdParty/thirdparties/17"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        BlockedThirdPartyKey newItemKey = new BlockedThirdPartyKey(new ThirdParty(17L, "thirdParty17"), new User("user17"));
        assertTrue(blockedThirdPartyRepository.findById(newItemKey).isPresent());

        requestRepository.flush();
        requestRepository.findAllByThirdParty_Id(17L).forEach(individualRequest -> assertEquals(IndividualRequestStatus.REFUSED,
                individualRequest.getStatus()));
    }


    // UTIL METHOD

    /**
     * Utility method to form the url with the injected port for a certain uri
     * @param uri uri that will access a certain resource of the application
     * @return url for accesing the resource identified by the uri
     */
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
