package org.recap.batch.job;

import org.junit.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
import org.recap.batch.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Anitha V on 14/7/20.
 */

public class DeletedRecordsExportTaskletUT extends BaseTestCase {
    private static final Logger logger = LoggerFactory.getLogger(IncrementalExportTaskletUT.class);

    @Value("${scsb.etl.url}")
    String scsbEtlUrl;

    @Mock
    DeletedRecordsExportTasklet deletedRecordsExportTasklet;
    
    @Mock
    RecordsExportService recordsExportService;

    @Test
    public void testExecute_cul() throws Exception {
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        String exportStringDate = jobExecution.getJobParameters().getString(RecapConstants.FROM_DATE);
        Date createdDate = jobExecution.getCreateTime();
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, RecapConstants.DELETED_RECORDS_EXPORT_CUL, createdDate, exportStringDate, RecapCommonConstants.COLUMBIA)).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(deletedRecordsExportTasklet.executeDeletedRecordsExport(context,logger,RecapCommonConstants.COLUMBIA,true)).thenCallRealMethod();
        RepeatStatus status = deletedRecordsExportTasklet.executeDeletedRecordsExport(context,logger,RecapCommonConstants.COLUMBIA,true);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }

    @Test
    public void testExecute_pul() throws Exception {
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        String exportStringDate = jobExecution.getJobParameters().getString(RecapConstants.FROM_DATE);
        Date createdDate = jobExecution.getCreateTime();
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl,RecapConstants.DELETED_RECORDS_EXPORT_PUL,createdDate, exportStringDate, RecapCommonConstants.PRINCETON)).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(deletedRecordsExportTasklet.executeDeletedRecordsExport(context,logger,RecapCommonConstants.PRINCETON,true)).thenCallRealMethod();
        RepeatStatus status = deletedRecordsExportTasklet.executeDeletedRecordsExport(context,logger,RecapCommonConstants.PRINCETON,true);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }

    @Test
    public void testExecute_nypl() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(25L, "PeriodicLASItemStatusReconciliation"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        String exportStringDate = jobExecution.getJobParameters().getString(RecapConstants.FROM_DATE);
        Date createdDate = jobExecution.getCreateTime();
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl,RecapConstants.DELETED_RECORDS_EXPORT_NYPL,createdDate, exportStringDate, RecapCommonConstants.NYPL)).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(deletedRecordsExportTasklet.executeDeletedRecordsExport(context,logger,RecapCommonConstants.NYPL,true)).thenCallRealMethod();
        RepeatStatus status = deletedRecordsExportTasklet.executeDeletedRecordsExport(context,logger,RecapCommonConstants.NYPL,true);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }

    @Test
    public void testExecute_exception() throws Exception {
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        String exportStringDate = jobExecution.getJobParameters().getString(RecapConstants.FROM_DATE);
        Date createdDate = jobExecution.getCreateTime();
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl,RecapConstants.DELETED_RECORDS_EXPORT_NYPL,createdDate, exportStringDate, RecapCommonConstants.PRINCETON)).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(deletedRecordsExportTasklet.executeDeletedRecordsExport(context,logger,RecapCommonConstants.PRINCETON,true)).thenCallRealMethod();
        RepeatStatus status = deletedRecordsExportTasklet.executeDeletedRecordsExport(context,logger,RecapCommonConstants.PRINCETON,true);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }


}
