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

/**
 * Created by Anitha V on 14/7/20.
 */

public class IncrementalExportPulTaskletUT extends BaseTestCase {
    private static final Logger logger = LoggerFactory.getLogger(IncrementalExportPulTaskletUT.class);

    @Mock
    IncrementalExportPulTasklet incrementalExportPulTasklet;

    @Mock
    IncrementalExportNyplTasklet incrementalExportNyplTasklet;

    @Mock
    IncrementalExportCulTasklet incrementalExportCulTasklet;

    @Test
    public void testexecute_pul() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(123L, "job"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        Mockito.when(incrementalExportPulTasklet.executeIncrementalExport(context, logger, "PUL")).thenCallRealMethod();
        Mockito.when(incrementalExportPulTasklet.execute(contribution,context)).thenCallRealMethod();
        RepeatStatus status = incrementalExportPulTasklet.execute(contribution,context);

    }

    @Test
    public void testexecute_nypl() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(123L, "job"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        Mockito.when(incrementalExportCulTasklet.executeIncrementalExport(context, logger, "NYPL")).thenCallRealMethod();
        Mockito.when(incrementalExportCulTasklet.execute(contribution,context)).thenCallRealMethod();
        RepeatStatus status = incrementalExportCulTasklet.execute(contribution,context);

    }

    @Test
    public void testexecute_cul() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(123L, "job"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        Mockito.when(incrementalExportNyplTasklet.executeIncrementalExport(context, logger, "CUL")).thenCallRealMethod();
        Mockito.when(incrementalExportNyplTasklet.execute(contribution,context)).thenCallRealMethod();
        RepeatStatus status = incrementalExportNyplTasklet.execute(contribution,context);

    }

}
