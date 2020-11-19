package org.recap.batch.service;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
import org.recap.model.batch.SolrIndexRequest;
import org.recap.util.JobDataParameterUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Created by angelind on 29/5/17.
 */
public class GenerateReportsServiceUT extends BaseTestCase{

    @Value("${scsb.solr.doc.url}")
    String solrClientUrl;

    @Mock
    JobDataParameterUtil jobDataParameterUtil;

    @Mock
    RestTemplate restTemplate;

    @Mock
     CommonService commonService;

    @Mock
    GenerateReportsService generateReportsService;

    @Test
    public void generateReport() throws Exception {
        SolrIndexRequest solrIndexRequest = new SolrIndexRequest();
        solrIndexRequest.setProcessType(RecapConstants.GENERATE_ACCESSION_REPORT);
        Date createdDate = new Date();
        solrIndexRequest.setCreatedDate(createdDate);
        HttpHeaders headers = getHttpHeaders();
        HttpEntity<SolrIndexRequest> httpEntity = new HttpEntity<>(solrIndexRequest, headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(RecapConstants.SUCCESS, HttpStatus.OK);
        ReflectionTestUtils.setField(generateReportsService,"commonService",commonService);
        ReflectionTestUtils.setField(generateReportsService,"jobDataParameterUtil",jobDataParameterUtil);
        Mockito.when(generateReportsService.commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(generateReportsService.getSolrIndexRequest(createdDate, RecapConstants.GENERATE_ACCESSION_REPORT)).thenReturn(solrIndexRequest);
        Mockito.when(commonService.getRestTemplate().exchange(solrClientUrl + RecapConstants.GENERATE_REPORT_URL, HttpMethod.POST, httpEntity, String.class)).thenReturn(responseEntity);
        Mockito.when(jobDataParameterUtil.getFromDate(createdDate)).thenCallRealMethod();
        Mockito.when(commonService.getResponse(Mockito.any(),Mockito.anyString(),Mockito.anyString())).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(generateReportsService.getSolrIndexRequest(createdDate, RecapConstants.GENERATE_ACCESSION_REPORT)).thenCallRealMethod();
        Mockito.when(generateReportsService.generateReport(solrClientUrl, createdDate, RecapConstants.GENERATE_ACCESSION_REPORT)).thenCallRealMethod();
        String status = generateReportsService.generateReport(solrClientUrl, createdDate, RecapConstants.GENERATE_ACCESSION_REPORT);
        assertNotNull(status);
        assertEquals(RecapConstants.SUCCESS, status);
    }
    public HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RecapCommonConstants.API_KEY, RecapCommonConstants.RECAP);
        return headers;
    }

}