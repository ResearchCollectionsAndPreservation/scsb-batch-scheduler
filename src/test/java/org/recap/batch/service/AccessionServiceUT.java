package org.recap.batch.service;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
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
public class AccessionServiceUT extends BaseTestCase{

    @Value("${scsb.solr.doc.url}")
    String solrClientUrl;

    @Mock
    RestTemplate restTemplate;

    @Mock
    private AccessionService accessionService;

    @Mock
    CommonService commonService;

    @Test
    public void processAccession() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RecapCommonConstants.API_KEY, RecapCommonConstants.RECAP);
        HttpEntity<Date> httpEntity = new HttpEntity<>(headers);
        ReflectionTestUtils.setField(accessionService,"commonService",commonService);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(RecapConstants.SUCCESS, HttpStatus.OK);
        Mockito.when(accessionService.commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(accessionService.commonService.getRestTemplate().exchange(solrClientUrl + RecapConstants.ACCESSION_URL, HttpMethod.GET, httpEntity, String.class)).thenReturn(responseEntity);
        Mockito.when(accessionService.commonService.executeService(solrClientUrl , RecapConstants.ACCESSION_URL, HttpMethod.GET)).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(accessionService.processAccession(solrClientUrl)).thenCallRealMethod();
        String status = accessionService.processAccession(solrClientUrl);
        assertNotNull(status);
        assertEquals(RecapConstants.SUCCESS, status);
    }

}