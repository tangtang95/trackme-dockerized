package com.poianitibaldizhou.trackme.individualrequestservice.service;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.User;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.RequestNotFoundException;
import com.poianitibaldizhou.trackme.individualrequestservice.message.publisher.IndividualRequestPublisher;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.IndividualRequestRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestStatus;
import com.poianitibaldizhou.trackme.individualrequestservice.util.ResponseType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("usage-message-broker")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("classpath:sql/testEventListener.sql")
public class UploadResponseServiceImplIntegrationTest {

    @Autowired
    private UploadResponseService uploadResponseService;

    @Autowired
    private IndividualRequestRepository individualRequestRepository;

    @Autowired
    @SpyBean
    private IndividualRequestPublisher individualRequestPublisher;

    /**
     * Test addResponse when the response type is accept
     *
     * @throws Exception no exception expected
     */
    @Test
    public void addResponseWhenResponseTypeIsAccept() throws Exception {
        IndividualRequest individualRequest = individualRequestRepository.findById(1L).orElseThrow(() -> new RequestNotFoundException(1L));
        individualRequest.setStatus(IndividualRequestStatus.ACCEPTED);
        uploadResponseService.addResponse(individualRequest.getId(), ResponseType.ACCEPT, new User("user1"));

        verify(individualRequestPublisher, times(1)).publishIndividualRequest(individualRequest);
    }

    /**
     * Test addResponse when the response type is refuse
     *
     * @throws Exception no exception expected
     */
    @Test
    public void addResponseWhenResponseTypeIsRefuse() throws Exception {
        IndividualRequest individualRequest = individualRequestRepository.findById(1L).orElseThrow(() -> new RequestNotFoundException(1L));
        individualRequest.setStatus(IndividualRequestStatus.REFUSED);
        uploadResponseService.addResponse(individualRequest.getId(), ResponseType.REFUSE, new User("user1"));

        verify(individualRequestPublisher, times(0)).publishIndividualRequest(any());
    }

}