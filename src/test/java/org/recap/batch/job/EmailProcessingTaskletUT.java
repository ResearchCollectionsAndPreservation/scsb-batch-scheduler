package org.recap.batch.job;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
import org.recap.batch.service.EmailService;
import org.recap.model.EmailPayLoad;
import org.recap.model.jpa.JobEntity;
import org.recap.repository.jpa.JobDetailsRepository;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Anitha V on 14/7/20.
 */

public class EmailProcessingTaskletUT extends BaseTestCase {

    @Mock
    JobEntity jobEntity;

    @Mock
    JobDetailsRepository jobDetailsRepository;

    @Mock
    EmailService emailService;

    @Value("${scsb.solr.doc.url}")
    String solrClientUrl;

    @Mock
    EmailProcessingTasklet emailProcessingTasklet;

    @Test
    public void testexecute_Exception() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(123L, "job"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        String jobName = jobExecution.getJobInstance().getJobName();
        Date createdDate = jobExecution.getCreateTime();
        String jobStatus = (String) executionContext.get(RecapConstants.JOB_STATUS);
        String jobStatusMessage = (String) executionContext.get(RecapConstants.JOB_STATUS_MESSAGE);
        EmailPayLoad emailPayLoad = new EmailPayLoad();
        emailPayLoad.setJobName(jobName);
        emailPayLoad.setJobDescription(jobEntity.getJobDescription());
        emailPayLoad.setJobAction(RecapConstants.RAN);
        emailPayLoad.setStartDate(createdDate);
        emailPayLoad.setStatus(jobStatus);
        emailPayLoad.setMessage(jobStatusMessage);
        Mockito.when(emailService.sendEmail(solrClientUrl, emailPayLoad)).thenReturn(String.valueOf(emailPayLoad));
        Mockito.when(jobDetailsRepository.findByJobName(jobName)).thenReturn(jobEntity);
        Mockito.when(emailProcessingTasklet.execute(contribution,context)).thenCallRealMethod();
        RepeatStatus status = emailProcessingTasklet.execute(contribution,context);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }

    @Test
    public void testexecute() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(25L, "PeriodicLASItemStatusReconciliation"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        String jobName = jobExecution.getJobInstance().getJobName();
        Date createdDate = jobExecution.getCreateTime();
        String jobStatus = (String) executionContext.get(RecapConstants.JOB_STATUS);
        String jobStatusMessage = (String) executionContext.get(RecapConstants.JOB_STATUS_MESSAGE);
        Map<String, String> resultMap= new HashMap<>();
        resultMap.put(RecapCommonConstants.STATUS, RecapConstants.SUCCESS);
        resultMap.put(RecapCommonConstants.MESSAGE, RecapConstants.SUCCESS);
        EmailPayLoad emailPayLoad = new EmailPayLoad();
        emailPayLoad.setJobName(jobName);
        emailPayLoad.setJobDescription("test");
        emailPayLoad.setJobAction(RecapConstants.RAN);
        emailPayLoad.setStartDate(createdDate);
        emailPayLoad.setStatus(RecapConstants.JOB_STATUS);
        emailPayLoad.setMessage("test");
        assertNotNull(emailPayLoad.getMessage());
        assertNotNull(emailPayLoad.getJobDescription());
        assertNotNull(emailPayLoad.getJobAction());
        assertNotNull(emailPayLoad.getStartDate());
        assertNotNull(emailPayLoad.getStatus());
        assertNotNull(emailPayLoad.getJobName());
        ReflectionTestUtils.setField(emailProcessingTasklet,"jobDetailsRepository",jobDetailsRepository);
        ReflectionTestUtils.setField(emailProcessingTasklet,"emailService",emailService);
        Mockito.when(jobDetailsRepository.findByJobName(jobName)).thenReturn(jobEntity);
        Mockito.when(emailService.sendEmail(solrClientUrl, emailPayLoad)).thenReturn(String.valueOf(emailPayLoad));
        Mockito.when(emailProcessingTasklet.execute(contribution,context)).thenCallRealMethod();
        RepeatStatus status = emailProcessingTasklet.execute(contribution,context);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }


}
