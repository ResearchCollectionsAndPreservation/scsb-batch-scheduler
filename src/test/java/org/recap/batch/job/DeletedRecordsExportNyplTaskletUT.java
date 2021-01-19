package org.recap.batch.job;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Anitha V on 14/7/20.
 */

public class DeletedRecordsExportNyplTaskletUT extends BaseTestCase {
    private static final Logger logger = LoggerFactory.getLogger(DeletedRecordsExportNyplTaskletUT.class);

    @Mock
    DeletedRecordsExportPulTasklet deletedRecordsExportPulTasklet;

    @Mock
    DeletedRecordsExportNyplTasklet deletedRecordsExportNyplTasklet;

    @Mock
    DeletedRecordsExportCulTasklet deletedRecordsExportCulTasklet;

    @Test
    public void testexecute_pul() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(123L, "job"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        Mockito.when(deletedRecordsExportPulTasklet.executeDeletedRecordsExport(context, logger, "PUL",true)).thenCallRealMethod();
        Mockito.when(deletedRecordsExportPulTasklet.execute(contribution,context)).thenCallRealMethod();
        RepeatStatus status = deletedRecordsExportPulTasklet.execute(contribution,context);
        assertNull(status);
    }

    @Test
    public void testexecute_cul() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(123L, "job"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        Mockito.when(deletedRecordsExportCulTasklet.executeDeletedRecordsExport(context, logger, "CUL",true)).thenCallRealMethod();
        Mockito.when(deletedRecordsExportCulTasklet.execute(contribution,context)).thenCallRealMethod();
        RepeatStatus status = deletedRecordsExportCulTasklet.execute(contribution,context);
        assertNull(status);
    }

    @Test
    public void testexecute_nypl() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(123L, "job"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        Mockito.when(deletedRecordsExportNyplTasklet.executeDeletedRecordsExport(context, logger, "NYPL",true)).thenCallRealMethod();
        Mockito.when(deletedRecordsExportNyplTasklet.execute(contribution,context)).thenCallRealMethod();
        RepeatStatus status = deletedRecordsExportNyplTasklet.execute(contribution,context);
        assertNull(status);
    }

}
