package org.recap.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.recap.ScsbConstants;
import org.recap.batch.service.CheckAndNotifyPendingRequestService;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by angelind on 14/9/17.
 */
@Slf4j
public class CheckAndNotifyPendingRequestTasklet extends JobCommonTasklet implements Tasklet {

    @Autowired
    private CheckAndNotifyPendingRequestService checkAndNotifyPendingRequestService;

    /**
     * This method starts the execution for checking pending request in the lasOutgoingQ and notifying through sending email.
     *
     * @param contribution StepContribution
     * @param chunkContext ChunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Executing CheckAndNotifyPendingRequestTasklet");
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        try {
            updateJob(jobExecution, "CheckAndNotifyPendingRequestTasklet", Boolean.FALSE);
            checkAndNotifyPendingRequestService.checkPendingMessagesInQueue(scsbCircUrl);
        } catch (Exception ex) {
            updateExecutionExceptionStatus(stepExecution, executionContext, ex, ScsbConstants.CHECK_AND_NOTIFY_PENDING_REQUEST_STATUS_NAME);
        }
        return RepeatStatus.FINISHED;
    }
}
