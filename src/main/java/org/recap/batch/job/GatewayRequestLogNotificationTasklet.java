package org.recap.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.recap.ScsbConstants;
import org.recap.batch.service.CommonService;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Chittoor Charan Raj
 */
@Slf4j
public class GatewayRequestLogNotificationTasklet extends JobCommonTasklet implements Tasklet {

    @Autowired
    private CommonService commonService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info(ScsbConstants.GATEWAY_REQUEST_LOG_NOTIFICATION);
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        try {
            updateJob(jobExecution,"GatewayRequestLogNotificationTasklet", Boolean.FALSE);
            commonService.requestLog(scsbEtlUrl, ScsbConstants.GATEWAY_REQUEST_LOG_FOR_EMAIL_NOTIFICATION);
        } catch (Exception ex) {
            updateExecutionExceptionStatus(stepExecution, executionContext, ex, ScsbConstants.FAILURE);
        }
        return RepeatStatus.FINISHED;
    }
}
