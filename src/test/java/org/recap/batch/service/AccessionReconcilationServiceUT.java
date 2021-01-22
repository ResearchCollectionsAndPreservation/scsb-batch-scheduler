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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by akulak on 25/5/17.
 */
public class AccessionReconcilationServiceUT extends BaseTestCase{

    @Value("${scsb.circ.url}")
    String scsbCircUrl;

    @Mock
    RestTemplate restTemplate;

    @Mock
    CommonService commonService;

    @Mock
    AccessionReconcilationService accessionReconcilationService;

    @Test
    public void testAccessionReconcilationService() throws Exception{
        HttpHeaders headers = new HttpHeaders();
        headers.set(RecapCommonConstants.API_KEY, RecapCommonConstants.RECAP);
        HttpEntity httpEntity = new HttpEntity<>(headers);
        ReflectionTestUtils.setField(accessionReconcilationService,"commonService",commonService);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(RecapConstants.SUCCESS, HttpStatus.OK);
        Mockito.when(accessionReconcilationService.commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(accessionReconcilationService.commonService.getRestTemplate().exchange(scsbCircUrl + RecapConstants.ACCESSION_RECOCILIATION_URL, HttpMethod.POST, httpEntity, String.class)).thenReturn(responseEntity);
        Mockito.when(accessionReconcilationService.commonService.executeService(scsbCircUrl,RecapConstants.ACCESSION_RECOCILIATION_URL, HttpMethod.POST)).thenReturn(RecapConstants.SUCCESS);

        Mockito.when(accessionReconcilationService.accessionReconcilation(scsbCircUrl)).thenCallRealMethod();
        String status = accessionReconcilationService.accessionReconcilation(scsbCircUrl);
        assertNotNull(status);
        assertEquals(RecapConstants.SUCCESS, status);
    }
}
