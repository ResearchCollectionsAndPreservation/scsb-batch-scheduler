package org.recap.batch.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
public class DeletedRecordsExportServiceUT {

    @Mock
    RecordsExportService recordsExportService;

    @Value("${scsb.etl.url}")
    private String scsbEtlUrl;

    Date createdDate = new Date(System.currentTimeMillis());
    String exportStringDate= "2020-07-07";

    @Test
    public void testDeletedRecordsExportPulService() throws Exception {
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, RecapConstants.DELETED_RECORDS_EXPORT_PUL,createdDate,exportStringDate, RecapCommonConstants.PRINCETON)).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, RecapConstants.DELETED_RECORDS_EXPORT_PUL,createdDate,exportStringDate, RecapCommonConstants.PRINCETON)).thenCallRealMethod();
        String status=recordsExportService.exportRecords(scsbEtlUrl, RecapConstants.DELETED_RECORDS_EXPORT_PUL,createdDate,exportStringDate, RecapCommonConstants.PRINCETON);
        assertNotNull(status);
    }

    @Test
    public void testDeletedRecordsExportCulService() throws Exception {
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, RecapConstants.DELETED_RECORDS_EXPORT_CUL,createdDate,exportStringDate,RecapCommonConstants.COLUMBIA)).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, RecapConstants.DELETED_RECORDS_EXPORT_CUL,createdDate,exportStringDate,RecapCommonConstants.COLUMBIA)).thenCallRealMethod();
        String status=recordsExportService.exportRecords(scsbEtlUrl, RecapConstants.DELETED_RECORDS_EXPORT_CUL,createdDate,exportStringDate,RecapCommonConstants.COLUMBIA);
        assertNotNull(status);
    }
    @Test
    public void testDeletedRecordsExportNyplService() throws Exception {
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, RecapConstants.DELETED_RECORDS_EXPORT_NYPL,createdDate,exportStringDate,RecapCommonConstants.NYPL)).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, RecapConstants.DELETED_RECORDS_EXPORT_NYPL,createdDate,exportStringDate,RecapCommonConstants.NYPL)).thenCallRealMethod();
        String status=recordsExportService.exportRecords(scsbEtlUrl, RecapConstants.DELETED_RECORDS_EXPORT_NYPL,createdDate,exportStringDate,RecapCommonConstants.NYPL);
        assertNotNull(status);
    }

}
