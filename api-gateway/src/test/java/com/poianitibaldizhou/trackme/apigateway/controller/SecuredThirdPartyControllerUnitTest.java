package com.poianitibaldizhou.trackme.apigateway.controller;

import com.poianitibaldizhou.trackme.apigateway.assembler.ThirdPartyCompanyAssembler;
import com.poianitibaldizhou.trackme.apigateway.assembler.ThirdPartyPrivateAssembler;
import com.poianitibaldizhou.trackme.apigateway.entity.CompanyDetail;
import com.poianitibaldizhou.trackme.apigateway.entity.PrivateThirdPartyDetail;
import com.poianitibaldizhou.trackme.apigateway.entity.ThirdPartyCustomer;
import com.poianitibaldizhou.trackme.apigateway.exception.ThirdPartyCustomerNotFoundException;
import com.poianitibaldizhou.trackme.apigateway.filter.pre.AccessControlFilter;
import com.poianitibaldizhou.trackme.apigateway.filter.route.TranslationFilter;
import com.poianitibaldizhou.trackme.apigateway.service.ThirdPartyAuthenticationService;
import com.poianitibaldizhou.trackme.apigateway.service.ThirdPartyAccountManagerServiceImpl;
import com.poianitibaldizhou.trackme.apigateway.util.Constants;
import com.poianitibaldizhou.trackme.apigateway.util.ThirdPartyCompanyWrapper;
import com.poianitibaldizhou.trackme.apigateway.util.ThirdPartyPrivateWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit test of the controller of the secured part of the controller of the third parties.
 * In this test class the authentication is not considered: indeed, only the pieces of functionality of the
 * controller methods are verified.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = SecuredThirdPartyController.class, secure = false)
@Import({ThirdPartyCompanyAssembler.class, ThirdPartyPrivateAssembler.class})
public class SecuredThirdPartyControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ThirdPartyAccountManagerServiceImpl service;

    @MockBean
    private ThirdPartyAuthenticationService authenticationService;

    @MockBean
    private AccessControlFilter accessControlFilter;

    @MockBean
    private TranslationFilter translationFilter;


    /**
     * Test the get of a third party by means of an email when the third party is related with company details
     *
     * @throws Exception due to mock mvc get method
     */
    @Test
    public void testGetThirdPartyCustomerWhenCompany() throws Exception {
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

        given(service.getThirdPartyCompanyByEmail(null)).willReturn(java.util.Optional.of(thirdPartyCompanyWrapper));

        mvc.perform(get(Constants.SECURED_TP_API+Constants.GET_TP_INFO_API).accept(MediaTypes.HAL_JSON_VALUE))
                //.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("thirdPartyCustomer.email", is(customer.getEmail())))
                .andExpect(jsonPath("companyDetail.thirdPartyCustomer.email", is(customer.getEmail())))
                .andExpect(jsonPath("companyDetail.companyName", is(companyDetail.getCompanyName())))
                .andExpect(jsonPath("companyDetail.address", is(companyDetail.getAddress())))
                .andExpect(jsonPath("companyDetail.dunsNumber", is(companyDetail.getDunsNumber())))
                .andExpect(jsonPath("_links.self.href",
                        is("http://localhost"+Constants.SECURED_TP_API + Constants.GET_TP_INFO_API)));
    }

    /**
     * Test the get of the third party customer when no customer with the specified email is registered into the system
     *
     * @throws Exception due to mock mvc get method
     */
    @Test
    public void testGetThirdPartyCustomerWhenNotRegistered() throws Exception {
        given(service.getThirdPartyPrivateByEmail(null)).willThrow(new ThirdPartyCustomerNotFoundException("notPresentMail@yahoo.it"));

        mvc.perform(get(Constants.SECURED_TP_API + Constants.GET_TP_INFO_API).accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test the get of a third party by means of an email when the third party is not related with company details,
     * but private details are available
     *
     * @throws Exception due to mock mvc get method
     */
    @Test
    public void testGetThirdPartyCustomerWhenPrivate() throws Exception {
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

        given(service.getThirdPartyCompanyByEmail(null)).willReturn(Optional.empty());
        given(service.getThirdPartyPrivateByEmail(null)).willReturn(Optional.of(thirdPartyPrivateWrapper));

        mvc.perform(get(Constants.SECURED_TP_API + Constants.GET_TP_INFO_API).accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("thirdPartyCustomer.email", is(customer.getEmail())))
                .andExpect(jsonPath("privateThirdPartyDetail.thirdPartyCustomer.email", is(customer.getEmail())))
                .andExpect(jsonPath("privateThirdPartyDetail.ssn", is(privateThirdPartyDetail.getSsn())))
                .andExpect(jsonPath("privateThirdPartyDetail.name", is(privateThirdPartyDetail.getName())))
                .andExpect(jsonPath("privateThirdPartyDetail.surname", is(privateThirdPartyDetail.getSurname())))
                .andExpect(jsonPath("privateThirdPartyDetail.birthDate", is(privateThirdPartyDetail.getBirthDate().toString())))
                .andExpect(jsonPath("privateThirdPartyDetail.birthCity", is(privateThirdPartyDetail.getBirthCity())))
                .andExpect(jsonPath("_links.self.href", is("http://localhost" + Constants.SECURED_TP_API + Constants.GET_TP_INFO_API)));
    }
}
