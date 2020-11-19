package org.recap.batch.service;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
import org.recap.model.jpa.JobEntity;
import org.recap.repository.jpa.JobDetailsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rajeshbabuk on 19/4/17.
 */
public class UpdateJobDetailsServiceUT extends BaseTestCase {

    @Value("${scsb.solr.doc.url}")
    String solrClientUrl;

    @Mock
    RestTemplate restTemplate;

    @Mock
    JobDetailsRepository jobDetailsRepository;

    @Mock
    JobEntity jobEntity;

    @Mock
    UpdateJobDetailsService updateJobDetailsService;

    @Mock
    CommonService commonService;

    @Test
    public void testUpdateJobDetailsService() throws Exception {
        String jobName  = RecapCommonConstants.PURGE_EXCEPTION_REQUESTS;
        Long jobInstanceId = Long.valueOf(1);
        Date lastExecutedTime = new Date();
        jobEntity.setJobName(jobName);
        jobEntity.setCronExpression("0/10 * * * * ? *");

        HttpHeaders headers = new HttpHeaders();
        headers.set(RecapCommonConstants.API_KEY, RecapCommonConstants.RECAP);
        HttpEntity<JobEntity> httpEntity = new HttpEntity<>(jobEntity, headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(RecapConstants.SUCCESS, HttpStatus.OK);
        ReflectionTestUtils.setField(updateJobDetailsService,"commonService",commonService);
        ReflectionTestUtils.setField(updateJobDetailsService,"jobDetailsRepository",jobDetailsRepository);

        Mockito.when(updateJobDetailsService.commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(updateJobDetailsService.getJobDetailsRepository()).thenReturn(jobDetailsRepository);
        Mockito.when(updateJobDetailsService.getJobDetailsRepository().findByJobName(jobName)).thenReturn(jobEntity);
        Mockito.when(restTemplate.exchange(solrClientUrl + RecapConstants.UPDATE_JOB_URL, HttpMethod.POST, httpEntity, String.class)).thenReturn(responseEntity);
        //Mockito.when(updateJobDetailsService.updateJob(solrClientUrl, jobName, lastExecutedTime, jobInstanceId)).thenCallRealMethod();
        String status = updateJobDetailsService.updateJob(solrClientUrl, jobName, lastExecutedTime, jobInstanceId);
       // assertNotNull(status);
        //assertEquals(RecapConstants.SUCCESS, status);
    }
}
