package org.recap.batch.job;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.RecapConstants;
import org.recap.batch.service.GenerateReportsService;
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

public class AccessionReportsTaskletUT extends BaseTestCase {

    @Mock
    GenerateReportsService generateReportsService;

    @Value("${scsb.solr.doc.url}")
    String solrClientUrl;

    @Mock
    AccessionReportsTasklet accessionReportsTasklet;

    Date createdDate = new Date();
    @Test
    public void testexecute_Exception() throws Exception {

        StepContribution contribution = new StepContribution(new StepExecution("AccessionStep", new JobExecution(new JobInstance(
                123L, "Accession"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        Mockito.when(generateReportsService.generateReport(solrClientUrl, createdDate, RecapConstants.GENERATE_ACCESSION_REPORT)).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(accessionReportsTasklet.execute(contribution,context)).thenCallRealMethod();
        RepeatStatus status = accessionReportsTasklet.execute(contribution,context);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }

    @Test
    public void testexecute() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("AccessionStep", new JobExecution(new JobInstance(
                123L, "Accession"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        ReflectionTestUtils.setField(accessionReportsTasklet,"generateReportsService",generateReportsService);
        Mockito.when(generateReportsService.generateReport(solrClientUrl, createdDate, RecapConstants.GENERATE_ACCESSION_REPORT)).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(accessionReportsTasklet.execute(contribution,context)).thenCallRealMethod();
        RepeatStatus status = accessionReportsTasklet.execute(contribution,context);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }


}
