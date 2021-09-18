package com.poianitibaldizhou.trackme.apigateway.service;

import com.poianitibaldizhou.trackme.apigateway.entity.CompanyDetail;
import com.poianitibaldizhou.trackme.apigateway.entity.PrivateThirdPartyDetail;
import com.poianitibaldizhou.trackme.apigateway.entity.ThirdPartyCustomer;
import com.poianitibaldizhou.trackme.apigateway.exception.AlreadyPresentEmailException;
import com.poianitibaldizhou.trackme.apigateway.exception.ThirdPartyCustomerNotFoundException;
import com.poianitibaldizhou.trackme.apigateway.repository.CompanyDetailRepository;
import com.poianitibaldizhou.trackme.apigateway.repository.PrivateThirdPartyDetailRepository;
import com.poianitibaldizhou.trackme.apigateway.repository.ThirdPartyRepository;
import com.poianitibaldizhou.trackme.apigateway.util.ThirdPartyCompanyWrapper;
import com.poianitibaldizhou.trackme.apigateway.util.ThirdPartyPrivateWrapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for the implementation of the user account manager service
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class ThirdPartyAccountManagerServiceImplUnitTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private ThirdPartyRepository thirdPartyRepository;

    @MockBean
    private CompanyDetailRepository companyDetailRepository;

    @MockBean
    private PrivateThirdPartyDetailRepository privateThirdPartyDetailRepository;

    private ThirdPartyAccountManagerServiceImpl service;

    private ThirdPartyCustomer customer1;
    private ThirdPartyCustomer customer2;
    private ThirdPartyCustomer customer3;
    private ThirdPartyCustomer customer4;

    private CompanyDetail companyDetail1;
    private CompanyDetail companyDetail2;

    private PrivateThirdPartyDetail privateThirdPartyDetail1;
    private PrivateThirdPartyDetail privateThirdPartyDetail2;

    @Before
    public void setUp() {
        setUpThirdPartyRepository();
        setUpCompanyDetailRepository();
        setUpPrivateDetailRepository();

        service = new ThirdPartyAccountManagerServiceImpl(thirdPartyRepository, companyDetailRepository,
                privateThirdPartyDetailRepository, passwordEncoder);
    }

    public void setUpThirdPartyRepository() {
        customer1 = new ThirdPartyCustomer();
        customer1.setId(1L);
        customer1.setEmail("tp1@gmail.com");
        customer1.setPassword("pass");

        customer2 = new ThirdPartyCustomer();
        customer2.setId(2L);
        customer2.setEmail("tp2@gmail.com");
        customer2.setPassword("pass");

        customer3 = new ThirdPartyCustomer();
        customer3.setId(3L);
        customer3.setEmail("tp3@gmail.com");
        customer3.setPassword("pass");

        customer4 = new ThirdPartyCustomer();
        customer4.setId(4L);
        customer4.setEmail("tp4@gmail.com");
        customer4.setPassword("pass");

        Mockito.when(thirdPartyRepository.findById(1L)).thenReturn(java.util.Optional.of(customer1));
        Mockito.when(thirdPartyRepository.findById(2L)).thenReturn(java.util.Optional.of(customer2));
        Mockito.when(thirdPartyRepository.findById(3L)).thenReturn(java.util.Optional.of(customer3));
        Mockito.when(thirdPartyRepository.findById(4L)).thenReturn(java.util.Optional.of(customer4));

        Mockito.when(thirdPartyRepository.findByEmail(customer1.getEmail())).thenReturn(java.util.Optional.of(customer1));
        Mockito.when(thirdPartyRepository.findByEmail(customer2.getEmail())).thenReturn(java.util.Optional.of(customer2));
        Mockito.when(thirdPartyRepository.findByEmail(customer3.getEmail())).thenReturn(java.util.Optional.of(customer3));
        Mockito.when(thirdPartyRepository.findByEmail(customer4.getEmail())).thenReturn(java.util.Optional.of(customer4));
    }

    public void setUpCompanyDetailRepository() {
        companyDetail1 = new CompanyDetail();
        companyDetail1.setThirdPartyCustomer(customer1);
        companyDetail1.setAddress("address1");
        companyDetail1.setCompanyName("company1");
        companyDetail1.setDunsNumber("1");
        companyDetail1.setId(1L);

        companyDetail2 = new CompanyDetail();
        companyDetail2.setThirdPartyCustomer(customer2);
        companyDetail2.setAddress("address2");
        companyDetail2.setCompanyName("company2");
        companyDetail2.setDunsNumber("2");
        companyDetail2.setId(2L);

        Mockito.when(companyDetailRepository.findById(1L)).thenReturn(java.util.Optional.of(companyDetail1));
        Mockito.when(companyDetailRepository.findById(2L)).thenReturn(java.util.Optional.of(companyDetail2));

        Mockito.when(companyDetailRepository.findByThirdPartyCustomer(customer1)).thenReturn(java.util.Optional.of(companyDetail1));
        Mockito.when(companyDetailRepository.findByThirdPartyCustomer(customer2)).thenReturn(java.util.Optional.of(companyDetail2));
    }

    public void setUpPrivateDetailRepository() {
        privateThirdPartyDetail1 = new PrivateThirdPartyDetail();
        privateThirdPartyDetail1.setThirdPartyCustomer(customer3);
        privateThirdPartyDetail1.setBirthCity("city1");
        privateThirdPartyDetail1.setBirthDate(new Date(0));
        privateThirdPartyDetail1.setName("name1");
        privateThirdPartyDetail1.setSurname("surname1");
        privateThirdPartyDetail1.setId(1L);

        privateThirdPartyDetail2 = new PrivateThirdPartyDetail();
        privateThirdPartyDetail2.setThirdPartyCustomer(customer4);
        privateThirdPartyDetail2.setBirthCity("city2");
        privateThirdPartyDetail2.setBirthDate(new Date(0));
        privateThirdPartyDetail2.setName("name2");
        privateThirdPartyDetail2.setSurname("surname2");
        privateThirdPartyDetail2.setId(2L);

        Mockito.when(privateThirdPartyDetailRepository.findById(1L)).thenReturn(java.util.Optional.of(privateThirdPartyDetail1));
        Mockito.when(privateThirdPartyDetailRepository.findById(2L)).thenReturn(java.util.Optional.of(privateThirdPartyDetail2));

        Mockito.when(privateThirdPartyDetailRepository.findByThirdPartyCustomer(customer3)).thenReturn(java.util.Optional.of(privateThirdPartyDetail1));
        Mockito.when(privateThirdPartyDetailRepository.findByThirdPartyCustomer(customer4)).thenReturn(java.util.Optional.of(privateThirdPartyDetail2));
    }

    @After
    public void tearDown() {
        service = null;
    }

    /**
     * Test the get of a third party related with a company, when all the necessary data is present
     *
     * @throws Exception if no third party with that email is present
     */
    @Test
    public void testGetCompanyThirdPartyByEmail() throws Exception {
        ThirdPartyCompanyWrapper thirdPartyCompanyWrapper = service.getThirdPartyCompanyByEmail(customer1.getEmail()).orElseThrow(Exception::new);

        assertEquals(customer1, thirdPartyCompanyWrapper.getThirdPartyCustomer());
        assertEquals(companyDetail1, thirdPartyCompanyWrapper.getCompanyDetail());
    }

    /**
     * Test the get of a third party related with a company, when the third party customer is not even registered
     */
    @Test(expected = ThirdPartyCustomerNotFoundException.class)
    public void testGetCompanyThirdPartyByEmailWhenNotPresent() {
        service.getThirdPartyCompanyByEmail("notPresentEmail@email.com");
    }

    /**
     * Test the get of a third party related with a company, when no company detail for that third party customer
     * is available
     */
    @Test
    public void testGetCompanyThirdPartyByEmailWhenNoDetailAvailable() {
        assertTrue(!service.getThirdPartyCompanyByEmail(customer3.getEmail()).isPresent());
    }

    /**
     * Test the get of a third party non related with any company, when all the necessary data is present
     *
     * @throws Exception if no third party with that email is present
     */
    @Test
    public void testGetPrivateThirdPartyByEmail() throws Exception {
        ThirdPartyPrivateWrapper thirdPartyPrivateWrapper = service.getThirdPartyPrivateByEmail(customer3.getEmail()).orElseThrow(Exception::new);

        assertEquals(customer3, thirdPartyPrivateWrapper.getThirdPartyCustomer());
        assertEquals(privateThirdPartyDetail1, thirdPartyPrivateWrapper.getPrivateThirdPartyDetail());
    }

    /**
     * Test the get of a third party non related with a company, when the third party customer is not even registered
     */
    @Test(expected = ThirdPartyCustomerNotFoundException.class)
    public void testGetPrivateThirdPartyByEmailWhenNotPresent() {
        service.getThirdPartyPrivateByEmail("notPresentEmail@email.com");
    }

    /**
     * Test the get of a third party non related with a company, when no private detail for that third party customer
     * is available
     */
    @Test
    public void testGetPrivateThirdPartyByEmailWhenNoDetailAvailable() {
        assertTrue(!service.getThirdPartyPrivateByEmail(customer1.getEmail()).isPresent());
    }

    /**
     * Test the registration of a new customer with information related with the company
     */
    @Test
    public void testRegisterThirdPartyCompany() {
        ThirdPartyCustomer customer = new ThirdPartyCustomer();
        customer.setId(100L);
        customer.setPassword("pass");
        customer.setEmail("newtp@yahoo.it");

        CompanyDetail companyDetail = new CompanyDetail();
        companyDetail.setThirdPartyCustomer(customer);
        companyDetail.setId(15L);
        companyDetail.setDunsNumber("10");
        companyDetail.setAddress("newAddress");
        companyDetail.setCompanyName("newCompany");

        ThirdPartyCompanyWrapper thirdPartyCompanyWrapper = new ThirdPartyCompanyWrapper();
        thirdPartyCompanyWrapper.setThirdPartyCustomer(customer);
        thirdPartyCompanyWrapper.setCompanyDetail(companyDetail);

        Mockito.when(thirdPartyRepository.saveAndFlush(customer)).thenReturn(customer);
        Mockito.when(companyDetailRepository.saveAndFlush(companyDetail)).thenReturn(companyDetail);

        assertEquals(thirdPartyCompanyWrapper, service.registerThirdPartyCompany(thirdPartyCompanyWrapper));
    }

    /**
     * Test the registration of a new customer that is related with a company, when the email of the customer
     * is already related with a registered third party
     */
    @Test(expected = AlreadyPresentEmailException.class)
    public void testRegisterThirdPartyCompanyWhenMailAlreadyDefined() {
        ThirdPartyCustomer customer = new ThirdPartyCustomer();
        customer.setId(100L);
        customer.setPassword("pass");
        customer.setEmail(customer4.getEmail());

        CompanyDetail companyDetail = new CompanyDetail();
        companyDetail.setThirdPartyCustomer(customer);
        companyDetail.setId(15L);
        companyDetail.setDunsNumber("10");
        companyDetail.setAddress("newAddress");
        companyDetail.setCompanyName("newCompany");

        ThirdPartyCompanyWrapper thirdPartyCompanyWrapper = new ThirdPartyCompanyWrapper();
        thirdPartyCompanyWrapper.setThirdPartyCustomer(customer);
        thirdPartyCompanyWrapper.setCompanyDetail(companyDetail);

        service.registerThirdPartyCompany(thirdPartyCompanyWrapper);
    }

    /**
     * Test the registration of a new customer with information related to its private details
     */
    public void testRegisterThirdPartyPrivate() {
        ThirdPartyCustomer customer = new ThirdPartyCustomer();
        customer.setId(200L);
        customer.setPassword("newpass");
        customer.setEmail("newtp@yahoo.it");

        PrivateThirdPartyDetail privateThirdPartyDetail = new PrivateThirdPartyDetail();
        privateThirdPartyDetail.setId(15L);
        privateThirdPartyDetail.setThirdPartyCustomer(customer);
        privateThirdPartyDetail.setSurname("surname");
        privateThirdPartyDetail.setSsn("newuserssn");
        privateThirdPartyDetail.setName("name");
        privateThirdPartyDetail.setBirthCity("Berlin");
        privateThirdPartyDetail.setBirthDate(new Date(0));

        ThirdPartyPrivateWrapper thirdPartyPrivateWrapper = new ThirdPartyPrivateWrapper();
        thirdPartyPrivateWrapper.setThirdPartyCustomer(customer);
        thirdPartyPrivateWrapper.setPrivateThirdPartyDetail(privateThirdPartyDetail);

        Mockito.when(thirdPartyRepository.saveAndFlush(customer)).thenReturn(customer);
        Mockito.when(privateThirdPartyDetailRepository.saveAndFlush(privateThirdPartyDetail)).thenReturn(privateThirdPartyDetail);

        assertEquals(thirdPartyPrivateWrapper, service.registerThirdPartyPrivate(thirdPartyPrivateWrapper));
    }

    /**
     * Test the registration of a new customer with its private details, when the email of the customer
     * is already related with a registered third party
     */
    @Test(expected = AlreadyPresentEmailException.class)
    public void testRegisterThirdPartyPrivateWhenMailAlreadyDefined() {
        ThirdPartyCustomer customer = new ThirdPartyCustomer();
        customer.setId(200L);
        customer.setPassword("newpass");
        customer.setEmail(customer4.getEmail());

        PrivateThirdPartyDetail privateThirdPartyDetail = new PrivateThirdPartyDetail();
        privateThirdPartyDetail.setId(15L);
        privateThirdPartyDetail.setThirdPartyCustomer(customer);
        privateThirdPartyDetail.setSurname("surname");
        privateThirdPartyDetail.setSsn("newuserssn");
        privateThirdPartyDetail.setName("name");
        privateThirdPartyDetail.setBirthCity("Berlin");
        privateThirdPartyDetail.setBirthDate(new Date(0));

        ThirdPartyPrivateWrapper thirdPartyPrivateWrapper = new ThirdPartyPrivateWrapper();
        thirdPartyPrivateWrapper.setThirdPartyCustomer(customer);
        thirdPartyPrivateWrapper.setPrivateThirdPartyDetail(privateThirdPartyDetail);


        service.registerThirdPartyPrivate(thirdPartyPrivateWrapper);
    }


    @Test(expected = UsernameNotFoundException.class)
    public void testGetTpByEmailWhenNotPresent() {
        service.getThirdPartyByEmail("notPresentMail");
    }
}
