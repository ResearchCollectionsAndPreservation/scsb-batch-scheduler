package org.recap.batch.service;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
import org.recap.model.batch.SolrIndexRequest;
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
public class MatchingAlgorithmServiceUT extends BaseTestCase {

    @Value("${scsb.solr.doc.url}")
    String solrClientUrl;

    @Mock
    RestTemplate restTemplate;

    @Mock
    MatchingAlgorithmService matchingAlgorithmService;

    @Mock
    CommonService commonService;

    @Test
    public void testMatchingAlgorithmService() {
        SolrIndexRequest solrIndexRequest = new SolrIndexRequest();
        solrIndexRequest.setProcessType(RecapCommonConstants.ONGOING_MATCHING_ALGORITHM_JOB);
        Date createdDate = new Date();
        solrIndexRequest.setCreatedDate(createdDate);

        HttpHeaders headers = new HttpHeaders();
        headers.set(RecapCommonConstants.API_KEY, RecapCommonConstants.RECAP);
        HttpEntity<SolrIndexRequest> httpEntity = new HttpEntity<>(solrIndexRequest, headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(RecapConstants.SUCCESS, HttpStatus.OK);
        ReflectionTestUtils.setField(matchingAlgorithmService,"commonService",commonService);
        Mockito.when(matchingAlgorithmService.commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(matchingAlgorithmService.getSolrIndexRequest(createdDate)).thenReturn(solrIndexRequest);
        Mockito.when(matchingAlgorithmService.commonService.getRestTemplate().exchange(solrClientUrl + RecapConstants.MATCHING_ALGORITHM_URL, HttpMethod.POST, httpEntity, String.class)).thenReturn(responseEntity);
        Mockito.when(matchingAlgorithmService.commonService.getResponse(Mockito.any(),Mockito.anyString(),Mockito.anyString())).thenReturn(responseEntity.getBody());
        Mockito.when(matchingAlgorithmService.getSolrIndexRequest(createdDate)).thenCallRealMethod();

        Mockito.when(matchingAlgorithmService.initiateMatchingAlgorithm(solrClientUrl, createdDate)).thenCallRealMethod();
        String status = matchingAlgorithmService.initiateMatchingAlgorithm(solrClientUrl, createdDate);
        assertNotNull(status);
        assertNotNull(solrIndexRequest.getCreatedDate());
        assertNotNull(solrIndexRequest.getProcessType());
        assertEquals(RecapConstants.SUCCESS, status);
    }
}
