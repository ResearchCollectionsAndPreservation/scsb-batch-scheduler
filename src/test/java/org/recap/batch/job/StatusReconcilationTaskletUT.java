package org.recap.batch.job;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.RecapConstants;
import org.recap.batch.service.StatusReconciliationService;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Anitha V on 14/7/20.
 */

public class StatusReconcilationTaskletUT extends BaseTestCase {

    @Mock
    StatusReconciliationService statusReconciliationService;

    @Value("${scsb.core.url}")
    String scsbCoreUrl;

    @Mock
    StatusReconcilationTasklet statusReconcilationTasklet;

    @Test
    public void testexecute_Exception() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(123L, "job"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        Mockito.when(statusReconciliationService.statusReconciliation(scsbCoreUrl)).thenThrow(new NullPointerException());
        Mockito.when(statusReconcilationTasklet.execute(contribution,context)).thenCallRealMethod();
        RepeatStatus status = statusReconcilationTasklet.execute(contribution,context);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }

    @Test
    public void testexecute() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(25L, "PeriodicLASItemStatusReconciliation"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        ReflectionTestUtils.setField(statusReconcilationTasklet,"statusReconciliationService",statusReconciliationService);
        Mockito.when(statusReconciliationService.statusReconciliation(scsbCoreUrl)).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(statusReconcilationTasklet.execute(contribution,context)).thenCallRealMethod();
        RepeatStatus status = statusReconcilationTasklet.execute(contribution,context);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }


}
