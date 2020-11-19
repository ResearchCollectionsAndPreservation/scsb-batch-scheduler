package org.recap.batch.service;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
import org.recap.model.batch.SolrIndexRequest;
import org.recap.model.jpa.JobEntity;
import org.recap.util.JobDataParameterUtil;
import org.recap.util.PropertyUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Anitha V on 12/7/20.
 */

public class CommonServiceUT extends BaseTestCase {

    @Mock
    CommonService commonService;

    @Value("${scsb.solr.doc.url}")
    String solrClientUrl;

    @Value("${scsb.circ.url}")
    String scsbCircUrl;

    @Mock
    RestTemplate restTemplate;

    @Mock
    JobDataParameterUtil jobDataParameterUtil;

    @Test
    public void testExecuteService() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RecapCommonConstants.API_KEY, RecapCommonConstants.RECAP);
        HttpEntity<Date> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(RecapConstants.SUCCESS, HttpStatus.OK);
        Mockito.when(commonService.getHttpEntity()).thenCallRealMethod();
        Mockito.when(commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(commonService.getRestTemplate().exchange(solrClientUrl + RecapConstants.ACCESSION_URL, HttpMethod.GET, httpEntity, String.class)).thenReturn(responseEntity);
        Mockito.when(commonService.executeService(solrClientUrl , RecapConstants.ACCESSION_URL, HttpMethod.GET)).thenCallRealMethod();
        String status = commonService.executeService(solrClientUrl , RecapConstants.ACCESSION_URL, HttpMethod.GET);
        assertNotNull(status);
        assertEquals(RecapConstants.SUCCESS, status);
    }
    @Test
    public void testexecutePurge() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RecapCommonConstants.API_KEY, RecapCommonConstants.RECAP);
        HttpEntity<Date> httpEntity = new HttpEntity<>(headers);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put(RecapCommonConstants.STATUS, RecapConstants.SUCCESS);
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        Mockito.when(commonService.getHttpEntity()).thenCallRealMethod();
        Mockito.when(commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(commonService.getRestTemplate().exchange(scsbCircUrl + RecapConstants.PURGE_ACCESSION_REQUEST_URL, HttpMethod.GET, httpEntity, Map.class)).thenReturn(responseEntity);
        Mockito.when(commonService.executePurge(scsbCircUrl , RecapConstants.PURGE_ACCESSION_REQUEST_URL)).thenCallRealMethod();
        resultMap = commonService.executePurge(scsbCircUrl , RecapConstants.PURGE_ACCESSION_REQUEST_URL);
        assertNotNull(resultMap);
        assertEquals(RecapConstants.SUCCESS, resultMap.get(RecapCommonConstants.STATUS));
    }
    @Test
    public void testgetResponse() throws Exception {
        SolrIndexRequest solrIndexRequest = new SolrIndexRequest();
        solrIndexRequest.setProcessType(RecapCommonConstants.ONGOING_MATCHING_ALGORITHM_JOB);
        Date createdDate = new Date();
        solrIndexRequest.setCreatedDate(createdDate);
        HttpHeaders headers = new HttpHeaders();
        headers.set(RecapCommonConstants.API_KEY, RecapCommonConstants.RECAP);
        HttpEntity<SolrIndexRequest> httpEntity = new HttpEntity<>(solrIndexRequest, headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(RecapConstants.SUCCESS, HttpStatus.OK);
        Mockito.when(commonService.getHttpHeaders()).thenCallRealMethod();
        Mockito.when(commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(commonService.getRestTemplate().exchange(solrClientUrl + RecapConstants.MATCHING_ALGORITHM_URL, HttpMethod.POST, httpEntity, String.class)).thenReturn(responseEntity);
        Mockito.when(commonService.getResponse(solrIndexRequest , solrClientUrl, RecapConstants.MATCHING_ALGORITHM_URL)).thenCallRealMethod();
        String status = commonService.getResponse(solrIndexRequest , solrClientUrl, RecapConstants.MATCHING_ALGORITHM_URL);
        assertNotNull(status);
        assertEquals(RecapConstants.SUCCESS, status);
    }
    @Ignore
    public void testpendingRequest() throws Exception {
        JobEntity jobEntity=new JobEntity();
        jobEntity.setJobDescription(RecapConstants.CHECK_PENDING_REQUEST_IN_DB);
        jobEntity.setStatus((RecapConstants.SUCCESS));
        jobEntity.setId(1);
        HttpHeaders headers = new HttpHeaders();
        headers.set(RecapCommonConstants.API_KEY, RecapCommonConstants.RECAP);
        HttpEntity<JobEntity> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(RecapConstants.SUCCESS, HttpStatus.OK);
        Mockito.when(commonService.getHttpHeaders()).thenCallRealMethod();
        Mockito.when(commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(commonService.getRestTemplate().exchange(scsbCircUrl + RecapConstants.CHECK_PENDING_REQUEST_IN_DB, HttpMethod.GET, httpEntity, String.class)).thenReturn(responseEntity);
        Mockito.when(commonService.pendingRequest(scsbCircUrl, RecapConstants.CHECK_PENDING_REQUEST_IN_DB)).thenCallRealMethod();
        String status = commonService.pendingRequest(scsbCircUrl, RecapConstants.CHECK_PENDING_REQUEST_IN_DB);
        assertNotNull(status);
        assertEquals(RecapConstants.SUCCESS, status);
    }

    @Test
    public void testgetRestTemplate()  {
        Mockito.when(commonService.getRestTemplate()).thenCallRealMethod();
        assertNotEquals(commonService.getRestTemplate(),restTemplate);

    }

}
