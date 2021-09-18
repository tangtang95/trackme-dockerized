package com.poianitibaldizhou.trackme.apigateway.service;

import com.poianitibaldizhou.trackme.apigateway.entity.CompanyDetail;
import com.poianitibaldizhou.trackme.apigateway.entity.PrivateThirdPartyDetail;
import com.poianitibaldizhou.trackme.apigateway.entity.ThirdPartyCustomer;
import com.poianitibaldizhou.trackme.apigateway.util.ThirdPartyCompanyWrapper;
import com.poianitibaldizhou.trackme.apigateway.util.ThirdPartyPrivateWrapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("usage-message-broker")
public class ThirdPartyAccountManagerServiceImplTest {
    @Autowired
    private ThirdPartyAccountManagerService thirdPartyAccountManagerService;

    @Autowired
    @SpyBean
    private InternalCommunicationService internalCommunicationService;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void registerThirdPartyCompany() throws Exception {
        ThirdPartyCustomer thirdPartyCustomer = new ThirdPartyCustomer();
        thirdPartyCustomer.setEmail("tp@provider.com");
        thirdPartyCustomer.setPassword("password");

        CompanyDetail companyDetail = new CompanyDetail();
        companyDetail.setAddress("address");
        companyDetail.setDunsNumber("121242");
        companyDetail.setCompanyName("companyName");

        ThirdPartyCompanyWrapper companyWrapper = new ThirdPartyCompanyWrapper();
        companyWrapper.setCompanyDetail(companyDetail);
        companyWrapper.setThirdPartyCustomer(thirdPartyCustomer);
        thirdPartyAccountManagerService.registerThirdPartyCompany(companyWrapper);

        verify(internalCommunicationService, times(1)).broadcastCompanyThirdPartyMessage(companyDetail);
    }

    @Test
    public void registerThirdPartyPrivate() throws Exception {
        ThirdPartyCustomer thirdPartyCustomer = new ThirdPartyCustomer();
        thirdPartyCustomer.setEmail("tp@provider.com");
        thirdPartyCustomer.setPassword("password");

        PrivateThirdPartyDetail privateDetail = new PrivateThirdPartyDetail();
        privateDetail.setSsn("ssn");
        privateDetail.setName("name");
        privateDetail.setSurname("surname");
        privateDetail.setBirthCity("birthCity");
        privateDetail.setBirthDate(new Date(0));

        ThirdPartyPrivateWrapper privateWrapper = new ThirdPartyPrivateWrapper();
        privateWrapper.setPrivateThirdPartyDetail(privateDetail);
        privateWrapper.setThirdPartyCustomer(thirdPartyCustomer);
        thirdPartyAccountManagerService.registerThirdPartyPrivate(privateWrapper);

        verify(internalCommunicationService, times(1)).broadcastPrivateThirdPartyMessage(privateDetail);
    }

}