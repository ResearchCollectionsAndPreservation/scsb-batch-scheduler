package org.recap.batch.service;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
import org.recap.util.JobDataParameterUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RecordsExportServiceUT  extends BaseTestCase {

    @Mock
    RecordsExportService recordsExportService;

    @Value("${scsb.etl.url}")
    private String scsbEtlUrl;

    @Mock
    CommonService commonService;

    @Mock
    JobDataParameterUtil jobDataParameterUtil;

    @Mock
    RestTemplate restTemplate;

    @Ignore
    public void testexportRecords() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RecapCommonConstants.API_KEY, RecapCommonConstants.RECAP);
        HttpEntity<Date> httpEntity = new HttpEntity<>(headers);
        Date createdDate = new Date(System.currentTimeMillis());
        String exportStringDate= "2020-07-07";
        Map<String, String> requestParameterMap= new HashMap<>();
        requestParameterMap.put(RecapConstants.DATE, String.valueOf(createdDate));
        ResponseEntity<String> responseEntity = new ResponseEntity<>(RecapConstants.SUCCESS, HttpStatus.OK);
        ReflectionTestUtils.setField(recordsExportService,"commonService",commonService);
        ReflectionTestUtils.setField(recordsExportService,"jobDataParameterUtil",jobDataParameterUtil);
        Mockito.when(commonService.getHttpEntity()).thenCallRealMethod();
        Mockito.when(jobDataParameterUtil.buildJobRequestParameterMap(RecapConstants.DELETED_RECORDS_EXPORT_PUL)).thenReturn(requestParameterMap);
        commonService.setRequestParameterMap(requestParameterMap, exportStringDate, jobDataParameterUtil, createdDate);
        Mockito.when(commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(restTemplate.exchange(scsbEtlUrl + RecapConstants.DELETED_RECORDS_EXPORT_PUL, HttpMethod.GET, httpEntity, String.class)).thenReturn(responseEntity);

        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, RecapConstants.DELETED_RECORDS_EXPORT_PUL,createdDate,exportStringDate,"PUL")).thenCallRealMethod();
        String status=recordsExportService.exportRecords(scsbEtlUrl, RecapConstants.DELETED_RECORDS_EXPORT_PUL,createdDate,exportStringDate,"PUL");

    }
}
