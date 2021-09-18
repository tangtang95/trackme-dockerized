package com.poianitibaldizhou.trackme.grouprequestservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poianitibaldizhou.trackme.grouprequestservice.GroupRequestServiceApplication;
import com.poianitibaldizhou.trackme.grouprequestservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.grouprequestservice.entity.GroupRequest;
import com.poianitibaldizhou.trackme.grouprequestservice.exception.BadOperatorRequestTypeException;
import com.poianitibaldizhou.trackme.grouprequestservice.exception.GroupRequestNotFoundException;
import com.poianitibaldizhou.trackme.grouprequestservice.exception.ImpossibleAccessException;
import com.poianitibaldizhou.trackme.grouprequestservice.repository.FilterStatementRepository;
import com.poianitibaldizhou.trackme.grouprequestservice.repository.GroupRequestRepository;
import com.poianitibaldizhou.trackme.grouprequestservice.util.*;
import org.apache.tomcat.util.bcel.Const;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

/**
 * Integration test for the group request controller
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GroupRequestServiceApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"classpath:integrationControllerTestDB.sql"})
@Transactional
public class GroupRequestControllerIntegrationTest {

	@LocalServerPort
	private int port;

	@Autowired
	private GroupRequestRepository groupRequestRepository;

	@Autowired
	private FilterStatementRepository filterStatementRepository;

	private HttpHeaders httpHeaders;

	private TestRestTemplate restTemplate = new TestRestTemplate();

	@Before
	public void setUp() {
		httpHeaders = new HttpHeaders();
	}

	@After
	public void tearDown() {
		httpHeaders = null;
	}

	// TEST GET SINGLE REQUEST

	/**
	 * Test the get of a single group request (accepted one) from the API
	 *
	 * @throws Exception due to json comparison
	 */
	@Test
	public void testGetSingleRequestAccepted() throws Exception{
		httpHeaders.set(Constants.HEADER_THIRD_PARTY_ID, "1");
		HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(Constants.GROUP_REQUEST_API + "/id/1"),
				HttpMethod.GET, entity, String.class);

		String expectedBody =  "\n" +
                "{\n" +
                "   \"groupRequest\":{\n" +
                "      \"creationTimestamp\":\"2011-06-06T00:00:00.000+0000\",\n" +
                "      \"aggregatorOperator\":\"COUNT\",\n" +
                "      \"requestType\":\"ALL\",\n" +
                "      \"status\":\"ACCEPTED\"\n" +
                "   },\n" +
                "   \"filterStatementList\":[\n" +
                "      {\n" +
                "         \"column\":\"HEART_BEAT\",\n" +
                "         \"value\":\"200\",\n" +
                "         \"comparisonSymbol\":\"GREATER\",\n" +
                "         \"groupRequest\":{\n" +
                "            \"creationTimestamp\":\"2011-06-06T00:00:00.000+0000\",\n" +
                "            \"aggregatorOperator\":\"COUNT\",\n" +
                "            \"requestType\":\"ALL\",\n" +
                "            \"status\":\"ACCEPTED\"\n" +
                "         }\n" +
                "      },\n" +
                "      {\n" +
                "         \"column\":\"BLOOD_OXYGEN_LEVEL\",\n" +
                "         \"value\":\"90\",\n" +
                "         \"comparisonSymbol\":\"GREATER\",\n" +
                "         \"groupRequest\":{\n" +
                "            \"creationTimestamp\":\"2011-06-06T00:00:00.000+0000\",\n" +
                "            \"aggregatorOperator\":\"COUNT\",\n" +
                "            \"requestType\":\"ALL\",\n" +
                "            \"status\":\"ACCEPTED\"\n" +
                "         }\n" +
                "      }\n" +
                "   ],\n" +
                "   \"_links\":{\n" +
                "      \"self\":{\n" +
                "         \"href\":\"http://localhost:"+port+"/grouprequests/id/1\"\n" +
                "      },\n" +
                "      \"accessData\":{\n" +
                "         \"href\":\"" + Constants.FAKE_URL + "/sharedataservice/dataretrieval/grouprequests/1\"\n" +
                "      }\n" +
                "   }\n" +
                "}";

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expectedBody, response.getBody(), false);
	}

	/**
	 * Test the get of a single group request (pending one) from the API
	 *
	 * @throws Exception due to json comparison
	 */
	@Test
	public void testGetSingleRequestPending() throws Exception{
		httpHeaders.set(Constants.HEADER_THIRD_PARTY_ID, "2");
		HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(Constants.GROUP_REQUEST_API + "/id/2"),
				HttpMethod.GET, entity, String.class);

		String expectedBody =  "\n" +
				"{\n" +
				"   \"groupRequest\":{\n" +
				"      \"creationTimestamp\":\"2005-11-03T00:00:00.000+0000\",\n" +
				"      \"aggregatorOperator\":\"MAX\",\n" +
				"      \"requestType\":\"HEART_BEAT\",\n" +
				"      \"status\":\"UNDER_ANALYSIS\"\n" +
				"   },\n" +
				"   \"filterStatementList\":[\n" +
				"      {\n" +
				"         \"column\":\"HEART_BEAT\",\n" +
				"         \"value\":\"80\",\n" +
				"         \"comparisonSymbol\":\"LESS\",\n" +
				"         \"groupRequest\":{\n" +
				"            \"creationTimestamp\":\"2005-11-03T00:00:00.000+0000\",\n" +
                "            \"aggregatorOperator\":\"MAX\",\n" +
                "            \"requestType\":\"HEART_BEAT\",\n" +
                "            \"status\":\"UNDER_ANALYSIS\"\n" +
				"         }\n" +
				"      }\n" +
				"   ],\n" +
				"   \"_links\":{\n" +
				"      \"self\":{\n" +
				"         \"href\":\"http://localhost:"+port+"/grouprequests/id/2\"\n" +
				"      }\n" +
				"   }\n" +
				"}";

        assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expectedBody, response.getBody(), false);
	}

	/**
	 * Test the get of single group request when that request is not present
	 */
	@Test
	public void testGetNonExistingGroupRequest() throws IOException {
		httpHeaders.set(Constants.HEADER_THIRD_PARTY_ID, "1000");
		HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
		ResponseEntity<String> responseEntity = restTemplate.exchange(createURLWithPort(
				Constants.GROUP_REQUEST_API + "/id/1000"),
				HttpMethod.GET, entity, String.class);

		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        ObjectMapper mapper = new ObjectMapper();
        ExceptionResponseBody exceptionResponseBody = mapper.readValue(responseEntity.getBody(), ExceptionResponseBody.class);

        assertEquals(HttpStatus.NOT_FOUND.value(), exceptionResponseBody.getStatus());
        assertEquals(HttpStatus.NOT_FOUND.toString(), exceptionResponseBody.getError());
        assertEquals(new GroupRequestNotFoundException(1000L).getMessage(), exceptionResponseBody.getMessage());
	}


	/**
	 * Test the get of a group request when the header specifies an impossible access
	 */
	@Test
	public void testGetRequestWhenWrongHeader() throws IOException {
		httpHeaders.set(Constants.HEADER_THIRD_PARTY_ID, "1000");
		HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
		ResponseEntity<String> responseEntity = restTemplate.exchange(createURLWithPort(
				Constants.GROUP_REQUEST_API + "/id/1"),
				HttpMethod.GET, entity, String.class);
		assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		ExceptionResponseBody exceptionResponseBody = mapper.readValue(responseEntity.getBody(), ExceptionResponseBody.class);
		assertEquals(HttpStatus.UNAUTHORIZED.value(), exceptionResponseBody.getStatus());
		assertEquals(HttpStatus.UNAUTHORIZED.toString(), exceptionResponseBody.getError());
		assertEquals(new ImpossibleAccessException().getMessage(), exceptionResponseBody.getMessage());
	}

	// TEST GET REQUEST BY THIRD PARTY ID

	/**
	 * Test the get of requests of a specific third party customer
	 *
	 * @throws JSONException due to json comparison
	 */
	@Test
	public void testGetRequestByThirdPartyId() throws JSONException {
		httpHeaders.set(Constants.HEADER_THIRD_PARTY_ID, "5");
		HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(
				Constants.GROUP_REQUEST_API + Constants.GROUP_REQUEST_BY_THIRD_PARTY_API),
				HttpMethod.GET, entity, String.class);

		String expectedBody =  "\n" +
				"{\n" +
				"   \"_embedded\":{\n" +
				"      \"groupRequestWrapperList\":[\n" +
				"         {\n" +
				"            \"groupRequest\":{\n" +
				"               \"creationTimestamp\":\"2005-11-03T00:00:00.000+0000\",\n" +
				"               \"aggregatorOperator\":\"DISTINCT_COUNT\",\n" +
				"               \"requestType\":\"ALL\",\n" +
				"               \"status\":\"REFUSED\"\n" +
				"            },\n" +
				"            \"filterStatementList\":[\n" +
				"\n" +
				"            ],\n" +
				"            \"_links\":{\n" +
				"               \"self\":{\n" +
				"                  \"href\":\"http://localhost:"+port+Constants.GROUP_REQUEST_API+"/id/4\"\n" +
				"               }\n" +
				"            }\n" +
				"         }\n" +
				"      ]\n" +
				"   },\n" +
				"   \"_links\":{\n" +
				"      \"self\":{\n" +
				"         \"href\":\"http://localhost:"+port+Constants.GROUP_REQUEST_API+Constants.GROUP_REQUEST_BY_THIRD_PARTY_API+"\"\n" +
				"      }\n" +
				"   }\n" +
				"}";
		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expectedBody, response.getBody(), false);
	}

	/**
	 * Test the get of group request performed by a third party customer, when the specified third party customer
	 * have not performed any request yet
	 *
	 * @throws JSONException due to json comparison
	 */
	@Test
	public void testGetRequestByThirdPartyIdWhenNoRequest() throws JSONException {
		httpHeaders.set(Constants.HEADER_THIRD_PARTY_ID, "1000");
		HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(Constants.GROUP_REQUEST_API+Constants.GROUP_REQUEST_BY_THIRD_PARTY_API),
				HttpMethod.GET, entity, String.class);

		String expectedBody =  "{\n" +
				"   \"_links\":{\n" +
				"      \"self\":{\n" +
				"         \"href\":\"http://localhost:"+port+Constants.GROUP_REQUEST_API+Constants.GROUP_REQUEST_BY_THIRD_PARTY_API+"\"\n" +
				"      }\n" +
				"   }\n" +
				"}";

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expectedBody, response.getBody(), false);
	}

	// TEST ADD NEW REQUEST

	/**
	 * Test the add of a new group request with two filter statements
	 *
	 * @throws Exception if entity were not correctly inserted into the repository
	 */
	@Test
	public void testAddOfNewRequestTwoFilterStatements() throws Exception {
		httpHeaders.set(Constants.HEADER_THIRD_PARTY_ID, "1");

		// Set up the request
		GroupRequest groupRequest = new GroupRequest();
		groupRequest.setRequestType(RequestType.ALL);
		groupRequest.setAggregatorOperator(AggregatorOperator.DISTINCT_COUNT);

		FilterStatement filterStatement1 = new FilterStatement();
		filterStatement1.setComparisonSymbol(ComparisonSymbol.NOT_EQUALS);
		filterStatement1.setValue("100");
		filterStatement1.setColumn(FieldType.LONGITUDE);

		FilterStatement filterStatement2 = new FilterStatement();
		filterStatement2.setComparisonSymbol(ComparisonSymbol.EQUALS);
		filterStatement2.setValue("200");
		filterStatement2.setColumn(FieldType.HEART_BEAT);

		List<FilterStatement> filterStatements = new ArrayList<>();
		filterStatements.add(filterStatement1);
		filterStatements.add(filterStatement2);

		GroupRequestWrapper groupRequestWrapper = new GroupRequestWrapper();
		groupRequestWrapper.setGroupRequest(groupRequest);
		groupRequestWrapper.setFilterStatementList(filterStatements);

		// Access the API

		HttpEntity<GroupRequestWrapper> entity = new HttpEntity<>(groupRequestWrapper, httpHeaders);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort(Constants.GROUP_REQUEST_API+Constants.NEW_GROUP_REQUEST_API),
				HttpMethod.POST, entity, String.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);

		Pattern p = Pattern.compile("[0-9]+$");
		Matcher m = p.matcher(actual);
		String requestId = "";
		if(m.find()) {
			requestId = m.group();
		}

		GroupRequest insertedGroupRequest = groupRequestRepository.findById(Long.parseLong(requestId)).orElseThrow(Exception::new);
		List<FilterStatement> insertedFilterStatement = filterStatementRepository.findAllByGroupRequest_Id(Long.parseLong(requestId));

		// ASSERT ON GROUP REQUEST
		assertEquals(RequestStatus.UNDER_ANALYSIS, insertedGroupRequest.getStatus());
		assertEquals(groupRequest.getAggregatorOperator(), insertedGroupRequest.getAggregatorOperator());
		assertEquals(groupRequest.getRequestType(), insertedGroupRequest.getRequestType());
		assertEquals(Long.parseLong(requestId), (long)insertedGroupRequest.getId());
		assertEquals(new Long(1), insertedGroupRequest.getThirdPartyId());

		// ASSERT ON FILTER STATEMENTS
		assertEquals(2, insertedFilterStatement.size());
		for(FilterStatement f : insertedFilterStatement)
			assertEquals(Long.parseLong(requestId), (long)f.getGroupRequest().getId());

	}

	/**
	 * Test the add of a new group request with one filter statements
	 *
	 * @throws Exception if entity were not correctly inserted into the repository
	 */
	@Test
	public void testAddOfNewRequestOneFilterStatements() throws Exception {
		httpHeaders.set(Constants.HEADER_THIRD_PARTY_ID, "2");

		// Set up the request
		GroupRequest groupRequest = new GroupRequest();
		groupRequest.setRequestType(RequestType.PRESSURE_MIN);
		groupRequest.setAggregatorOperator(AggregatorOperator.AVG);

		FilterStatement filterStatement = new FilterStatement();
		filterStatement.setComparisonSymbol(ComparisonSymbol.NOT_EQUALS);
		filterStatement.setValue("100");
		filterStatement.setColumn(FieldType.BLOOD_OXYGEN_LEVEL);

		GroupRequestWrapper groupRequestWrapper = new GroupRequestWrapper();
		groupRequestWrapper.setGroupRequest(groupRequest);
		groupRequestWrapper.setFilterStatementList(Collections.singletonList(filterStatement));

		// Access the API

		HttpEntity<GroupRequestWrapper> entity = new HttpEntity<>(groupRequestWrapper, httpHeaders);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort(Constants.GROUP_REQUEST_API+Constants.NEW_GROUP_REQUEST_API),
				HttpMethod.POST, entity, String.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);

		Pattern p = Pattern.compile("[0-9]+$");
		Matcher m = p.matcher(actual);
		String requestId = "";
		if(m.find()) {
			requestId = m.group();
		}

		GroupRequest insertedGroupRequest = groupRequestRepository.findById(Long.parseLong(requestId)).orElseThrow(Exception::new);
		List<FilterStatement> insertedFilterStatement = filterStatementRepository.findAllByGroupRequest_Id(Long.parseLong(requestId));

		// ASSERT ON GROUP REQUEST
		assertEquals(RequestStatus.UNDER_ANALYSIS, insertedGroupRequest.getStatus());
		assertEquals(groupRequest.getAggregatorOperator(), insertedGroupRequest.getAggregatorOperator());
		assertEquals(groupRequest.getRequestType(), insertedGroupRequest.getRequestType());
		assertEquals(Long.parseLong(requestId), (long)insertedGroupRequest.getId());
		assertEquals(new Long(2), insertedGroupRequest.getThirdPartyId());

		// ASSERT ON FILTER STATEMENTS
		assertEquals(1, insertedFilterStatement.size());
		assertEquals(Long.parseLong(requestId), (long)insertedFilterStatement.get(0).getGroupRequest().getId());
		assertEquals(filterStatement.getColumn(), insertedFilterStatement.get(0).getColumn());
		assertEquals(filterStatement.getValue(), insertedFilterStatement.get(0).getValue());
		assertEquals(filterStatement.getComparisonSymbol(), insertedFilterStatement.get(0).getComparisonSymbol());
	}

	/**
	 * Test the add of a new request when the aggregated operator can't be matched with the specified request type
	 *
	 */
	@Test
	public void testAddRequestWithNonMatchingOperatorAndRequestType() throws IOException {
		httpHeaders.set(Constants.HEADER_THIRD_PARTY_ID, "2");

		// Set up the request
		GroupRequest groupRequest = new GroupRequest();
		groupRequest.setRequestType(RequestType.BIRTH_CITY);
		groupRequest.setAggregatorOperator(AggregatorOperator.AVG);

		GroupRequestWrapper groupRequestWrapper = new GroupRequestWrapper();
		groupRequestWrapper.setGroupRequest(groupRequest);
		groupRequestWrapper.setFilterStatementList(new ArrayList<>());

		HttpEntity<GroupRequestWrapper> entity = new HttpEntity<>(groupRequestWrapper, httpHeaders);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort(Constants.GROUP_REQUEST_API+Constants.NEW_GROUP_REQUEST_API),
				HttpMethod.POST, entity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ObjectMapper mapper = new ObjectMapper();
        ExceptionResponseBody exceptionResponseBody = mapper.readValue(response.getBody(), ExceptionResponseBody.class);

        assertEquals(HttpStatus.BAD_REQUEST.value(), exceptionResponseBody.getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.toString(), exceptionResponseBody.getError());
        assertEquals(new BadOperatorRequestTypeException(AggregatorOperator.AVG, RequestType.BIRTH_CITY).getMessage(), exceptionResponseBody.getMessage());
	}

    /**
     * Test the add of a new group requests when some fields are missing
     */
	@Test
    public void testAddRequestWhenMissingFields() throws IOException {
		httpHeaders.set(Constants.HEADER_THIRD_PARTY_ID, "2");

        // Set up the request
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setRequestType(RequestType.PRESSURE_MIN);

        FilterStatement filterStatement = new FilterStatement();
        filterStatement.setComparisonSymbol(ComparisonSymbol.NOT_EQUALS);
        filterStatement.setValue("100");

        GroupRequestWrapper groupRequestWrapper = new GroupRequestWrapper();
        groupRequestWrapper.setGroupRequest(groupRequest);
        groupRequestWrapper.setFilterStatementList(Collections.singletonList(filterStatement));

        // Access the API

        HttpEntity<GroupRequestWrapper> entity = new HttpEntity<>(groupRequestWrapper, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(Constants.GROUP_REQUEST_API+Constants.NEW_GROUP_REQUEST_API),
                HttpMethod.POST, entity, String.class);

        ObjectMapper mapper = new ObjectMapper();
        ExceptionResponseBody exceptionResponseBody = mapper.readValue(response.getBody(), ExceptionResponseBody.class);

        assertEquals(HttpStatus.BAD_REQUEST.value(), exceptionResponseBody.getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.toString(), exceptionResponseBody.getError());
        assertEquals(Constants.INVALID_OPERATION, exceptionResponseBody.getMessage());
    }

    @Test
    public void testAddRequest() throws IOException {
	    httpHeaders.set(Constants.HEADER_THIRD_PARTY_ID, "2");
        HttpEntity<GroupRequestWrapper> entity = new HttpEntity<>(new GroupRequestWrapper(), httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(Constants.GROUP_REQUEST_API+Constants.NEW_GROUP_REQUEST_API),
                HttpMethod.POST, entity, String.class);

        ObjectMapper mapper = new ObjectMapper();
        ExceptionResponseBody exceptionResponseBody = mapper.readValue(response.getBody(), ExceptionResponseBody.class);

        assertEquals(HttpStatus.BAD_REQUEST.value(), exceptionResponseBody.getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.toString(), exceptionResponseBody.getError());
        assertEquals(Constants.INVALID_OPERATION, exceptionResponseBody.getMessage());

    }

	// UTILITY METHOD

	/**
	 * Utility method to form the url with the injected port for a certain uri
	 * @param uri uri that will access a certain resource of the application
	 * @return url for accesing the resource identified by the uri
	 */
	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
}
