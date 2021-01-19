package org.recap.batch.service;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IncrementalExportNyplServiceUT extends BaseTestCase {

    @Value("${scsb.etl.url}")
    private String scsbEtlUrl;

    @Mock
    RecordsExportService recordsExportService;

    Date createdDate = new Date(System.currentTimeMillis());
    String exportStringDate= "2020-07-07";

    @Test
    public void testIncrementalExportNypl() throws Exception {
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl,RecapConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,RecapCommonConstants.NYPL)).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl,RecapConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,RecapCommonConstants.NYPL)).thenCallRealMethod();
        String status = recordsExportService.exportRecords(scsbEtlUrl,RecapConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,RecapCommonConstants.NYPL);

    }
    @Test
    public void testIncrementalExportCul() throws Exception {
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl,RecapConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,RecapCommonConstants.COLUMBIA)).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl,RecapConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,RecapCommonConstants.COLUMBIA)).thenCallRealMethod();
        String status = recordsExportService.exportRecords(scsbEtlUrl,RecapConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,RecapCommonConstants.COLUMBIA);

    }
    @Test
    public void testIncrementalExportPul() throws Exception {
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl,RecapConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,RecapCommonConstants.PRINCETON)).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl,RecapConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,RecapCommonConstants.PRINCETON)).thenCallRealMethod();
        String status = recordsExportService.exportRecords(scsbEtlUrl,RecapConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,RecapCommonConstants.PRINCETON);

    }
}
