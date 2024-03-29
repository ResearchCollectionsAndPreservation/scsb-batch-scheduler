package org.recap.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.batch.service.PurgeExceptionRequestsService;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by rajeshbabuk on 23/3/17.
 */
@Slf4j
public class PurgeExceptionRequestTasklet extends JobCommonTasklet implements Tasklet {

    @Autowired
    private PurgeExceptionRequestsService purgeExceptionRequestsService;

    /**
     * This method starts the execution of purging exception requests job.
     *
     * @param contribution StepContribution
     * @param chunkContext ChunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Executing PurgeExceptionRequestTasklet");
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        try {
            updateJob(jobExecution, "PurgeExceptionRequestTasklet", Boolean.FALSE);
            Map<String, String> resultMap = purgeExceptionRequestsService.purgeExceptionRequests(scsbCoreUrl);
            String status = resultMap.get(ScsbCommonConstants.STATUS);
            String message = resultMap.get(ScsbCommonConstants.MESSAGE);
            log.info("Purge Exception Requests status : {}", status);
            log.info("Purge Exception Requests status message : {}", message);
            executionContext.put(ScsbConstants.JOB_STATUS, status);
            executionContext.put(ScsbConstants.JOB_STATUS_MESSAGE, message);
            stepExecution.setExitStatus(new ExitStatus(status, message));
        } catch (Exception ex) {
            updateExecutionExceptionStatus(stepExecution, executionContext, ex, ScsbConstants.PURGE_EXCEPTION_REQUEST_STATUS_NAME);
        }
        return RepeatStatus.FINISHED;
    }
}
