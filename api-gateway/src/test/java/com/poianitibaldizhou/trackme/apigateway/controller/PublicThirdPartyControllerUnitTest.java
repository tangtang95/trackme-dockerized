package com.poianitibaldizhou.trackme.apigateway.controller;

import com.poianitibaldizhou.trackme.apigateway.assembler.ThirdPartyCompanyAssembler;
import com.poianitibaldizhou.trackme.apigateway.assembler.ThirdPartyPrivateAssembler;
import com.poianitibaldizhou.trackme.apigateway.entity.CompanyDetail;
import com.poianitibaldizhou.trackme.apigateway.entity.PrivateThirdPartyDetail;
import com.poianitibaldizhou.trackme.apigateway.entity.ThirdPartyCustomer;
import com.poianitibaldizhou.trackme.apigateway.entity.User;
import com.poianitibaldizhou.trackme.apigateway.exception.AlreadyPresentEmailException;
import com.poianitibaldizhou.trackme.apigateway.security.TokenAuthenticationProvider;
import com.poianitibaldizhou.trackme.apigateway.service.ThirdPartyAuthenticationService;
import com.poianitibaldizhou.trackme.apigateway.service.UserAuthenticationService;
import com.poianitibaldizhou.trackme.apigateway.service.ThirdPartyAccountManagerService;
import com.poianitibaldizhou.trackme.apigateway.util.ApiUtils;
import com.poianitibaldizhou.trackme.apigateway.util.Constants;
import com.poianitibaldizhou.trackme.apigateway.util.ThirdPartyCompanyWrapper;
import com.poianitibaldizhou.trackme.apigateway.util.ThirdPartyPrivateWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit test of the public controller of the third party customers
 */
@RunWith(SpringRunner.class)
@WebMvcTest(PublicThirdPartyController.class)
@Import({ThirdPartyCompanyAssembler.class, ThirdPartyPrivateAssembler.class})
public class PublicThirdPartyControllerUnitTest {

    @Value(Constants.SERVER_ADDRESS)
    private String serverAddress;

    @Value(Constants.PORT)
    private Integer port;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ThirdPartyAccountManagerService service;

    @MockBean
    private ThirdPartyAuthenticationService authenticationService;

    @MockBean
    private UserAuthenticationService userAuthenticationService;

    @MockBean
    private ApiUtils apiUtils;

    @MockBean
    private TokenAuthenticationProvider tokenAuthenticationProvider;

    /**
     * Test the registration of a third party customer that is related with a company
     *
     * @throws Exception due to mock mvc get method
     */
    @Test
    public void testRegisterCompanyThirdParty() throws Exception {
        ThirdPartyCustomer customer = new ThirdPartyCustomer();
        customer.setId(1L);
        customer.setEmail("tp@provider.com");
        customer.setPassword("password");

        CompanyDetail companyDetail = new CompanyDetail();
        companyDetail.setId(1L);
        companyDetail.setCompanyName("company name");
        companyDetail.setAddress("address");
        companyDetail.setThirdPartyCustomer(customer);
        companyDetail.setDunsNumber("dunsNumber");

        ThirdPartyCompanyWrapper thirdPartyCompanyWrapper = new ThirdPartyCompanyWrapper();
        thirdPartyCompanyWrapper.setThirdPartyCustomer(customer);
        thirdPartyCompanyWrapper.setCompanyDetail(companyDetail);

        given(service.registerThirdPartyCompany(any(ThirdPartyCompanyWrapper.class))).willReturn(thirdPartyCompanyWrapper);

        String json = "{\n" +
                "   \"thirdPartyCustomer\":{\n" +
                "      \"email\":\"tp@provider.com\",\n" +
                "      \"password\":\"password\"\n" +
                "   },\n" +
                "   \"companyDetail\":{\n" +
                "      \"thirdPartyCustomer\":{\n" +
                "         \"email\":\"tp@provider.com\",\n" +
                "         \"password\":\"password\"\n" +
                "      },\n" +
                "      \"companyName\":\"company name\",\n" +
                "      \"address\":\"address\",\n" +
                "      \"dunsNumber\":\"dunsNumber\"\n" +
                "   }\n" +
                "}";

        mvc.perform(post(Constants.PUBLIC_TP_API + Constants.REGISTER_COMPANY_TP_API).
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("thirdPartyCustomer.email", is(customer.getEmail())))
                .andExpect(jsonPath("companyDetail.thirdPartyCustomer.email", is(customer.getEmail())))
                .andExpect(jsonPath("companyDetail.companyName", is(companyDetail.getCompanyName())))
                .andExpect(jsonPath("companyDetail.address", is(companyDetail.getAddress())))
                .andExpect(jsonPath("companyDetail.dunsNumber", is(companyDetail.getDunsNumber())))
                .andExpect(jsonPath("_links.self.href",
                        is("http://localhost" + Constants.SECURED_TP_API + Constants.GET_TP_INFO_API)));
    }

    /**
     * Test the registration of a third party customer that is related with a company when the mail provided
     * is associated with an already registered customer
     *
     * @throws Exception due to mock mvc get method
     */
    @Test
    public void testRegisterThirdPartyCompanyWhenMailAlreadyPresent() throws Exception {
        given(service.registerThirdPartyCompany(any(ThirdPartyCompanyWrapper.class))).willThrow(new AlreadyPresentEmailException("tp@provider.com"));

        String json = "{\n" +
                "   \"thirdPartyCustomer\":{\n" +
                "      \"email\":\"tp@provider.com\",\n" +
                "      \"password\":\"password\"\n" +
                "   },\n" +
                "   \"companyDetail\":{\n" +
                "      \"thirdPartyCustomer\":{\n" +
                "         \"email\":\"tp@provider.com\",\n" +
                "         \"password\":\"password\"\n" +
                "      },\n" +
                "      \"companyName\":\"company name\",\n" +
                "      \"address\":\"address\",\n" +
                "      \"dunsNumber\":\"dunsNumber\"\n" +
                "   }\n" +
                "}";

        mvc.perform(post(Constants.PUBLIC_TP_API + Constants.REGISTER_COMPANY_TP_API).
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").content(json))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test the registration of a third party customer that is not related with a company, but that provides private
     * details
     *
     * @throws Exception due to mock mvc get method
     */
    @Test
    public void testRegisterPrivateThirdParty() throws Exception {
        ThirdPartyCustomer customer = new ThirdPartyCustomer();
        customer.setId(1L);
        customer.setEmail("tp@provider.com");
        customer.setPassword("password");

        PrivateThirdPartyDetail privateThirdPartyDetail = new PrivateThirdPartyDetail();
        privateThirdPartyDetail.setThirdPartyCustomer(customer);
        privateThirdPartyDetail.setId(1L);
        privateThirdPartyDetail.setBirthDate(new Date(0));
        privateThirdPartyDetail.setBirthCity("Verona");
        privateThirdPartyDetail.setSsn("tpssn");
        privateThirdPartyDetail.setName("Luca");
        privateThirdPartyDetail.setSurname("Giuliani");

        ThirdPartyPrivateWrapper thirdPartyPrivateWrapper  = new ThirdPartyPrivateWrapper();
        thirdPartyPrivateWrapper.setThirdPartyCustomer(customer);
        thirdPartyPrivateWrapper.setPrivateThirdPartyDetail(privateThirdPartyDetail);

        given(service.registerThirdPartyPrivate(any(ThirdPartyPrivateWrapper.class))).willReturn(thirdPartyPrivateWrapper);

        String json = "\n" +
                "{\n" +
                "   \"thirdPartyCustomer\":{\n" +
                "      \"email\":\"tp@provider.com\",\n" +
                "      \"password\":\"password\"\n" +
                "   },\n" +
                "   \"privateThirdPartyDetail\":{\n" +
                "      \"thirdPartyCustomer\":{\n" +
                "         \"email\":\"tp@provider.com\",\n" +
                "         \"password\":\"password\"\n" +
                "      },\n" +
                "      \"ssn\":\"tpssn\",\n" +
                "      \"name\":\"Luca\",\n" +
                "      \"surname\":\"Giuliani\",\n" +
                "      \"birthDate\":\"1970-01-01\",\n" +
                "      \"birthCity\":\"Verona\"\n" +
                "   }"+
                "}";

        mvc.perform(post(Constants.PUBLIC_TP_API + Constants.REGISTER_PRIVATE_TP_API).
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("thirdPartyCustomer.email", is(customer.getEmail())))
                .andExpect(jsonPath("privateThirdPartyDetail.thirdPartyCustomer.email", is(customer.getEmail())))
                .andExpect(jsonPath("privateThirdPartyDetail.ssn", is(privateThirdPartyDetail.getSsn())))
                .andExpect(jsonPath("privateThirdPartyDetail.name", is(privateThirdPartyDetail.getName())))
                .andExpect(jsonPath("privateThirdPartyDetail.surname", is(privateThirdPartyDetail.getSurname())))
                .andExpect(jsonPath("privateThirdPartyDetail.birthDate", is(privateThirdPartyDetail.getBirthDate().toString())))
                .andExpect(jsonPath("_links.self.href", is("http://localhost"
                        + Constants.SECURED_TP_API + Constants.GET_TP_INFO_API)));
    }

    /**
     * Test the registration of a third party customer that private private details, when the mail provided
     * is associated with an already registered customer
     *
     * @throws Exception due to mock mvc get method
     */
    @Test
    public void testRegisterThirdPartyPrivateWhenMailAlreadyPresent() throws Exception {
        given(service.registerThirdPartyPrivate(any(ThirdPartyPrivateWrapper.class))).willThrow(new AlreadyPresentEmailException("tp@provider.com"));

        String json = "\n" +
                "{\n" +
                "   \"thirdPartyCustomer\":{\n" +
                "      \"email\":\"tp@provider.com\",\n" +
                "      \"password\":\"password\"\n" +
                "   },\n" +
                "   \"privateThirdPartyDetail\":{\n" +
                "      \"thirdPartyCustomer\":{\n" +
                "         \"email\":\"tp@provider.com\",\n" +
                "         \"password\":\"password\"\n" +
                "      },\n" +
                "      \"ssn\":\"tpssn\",\n" +
                "      \"name\":\"Luca\",\n" +
                "      \"surname\":\"Giuliani\",\n" +
                "      \"birthDate\":\"1970-01-01\",\n" +
                "      \"birthCity\":\"Verona\"\n" +
                "   }"+
                "}";

        mvc.perform(post(Constants.PUBLIC_TP_API + Constants.REGISTER_PRIVATE_TP_API).
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").content(json))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test the login into the system of a third party customer
     * @throws Exception due to mock mvc post method
     */
    @Test
    public void testLogin() throws Exception {
        ThirdPartyCustomer thirdPartyCustomer = new ThirdPartyCustomer();
        thirdPartyCustomer.setPassword("tangpass");
        thirdPartyCustomer.setEmail("newEmail");
        thirdPartyCustomer.setId(1L);

        given(authenticationService.thirdPartyLogin("newEmail", "tangpass")).willReturn(Optional.of("newToken"));

        mvc.perform(post(Constants.PUBLIC_TP_API + Constants.LOGIN_TP_API +"?email=newEmail&password=tangpass"))
                .andExpect(jsonPath("token", is("newToken")))
                .andExpect(jsonPath("_links.logout.href", is("https://" + serverAddress + ":" + port + "/thirdparties/logout")))
                .andExpect(jsonPath("_links.info.href", is("https://" + serverAddress + ":" + port + "/thirdparties/info")))
                .andExpect(jsonPath("_links.groupRequests.href", is("https://" + serverAddress + ":" + port + "/grouprequestservice/grouprequests/thirdparties")))
                .andExpect(jsonPath("_links.newGroupRequest.href", is("https://" + serverAddress + ":" + port + "/grouprequestservice/grouprequests/thirdparties")))
                .andExpect(jsonPath("_links.individualRequests.href", is("https://" + serverAddress + ":" + port + "/individualrequestservice/requests/thirdparties")))
                .andExpect(jsonPath("_links.newIndividualRequest.href", is("https://" + serverAddress + ":" + port + "/individualrequestservice/requests")));
    }
}
