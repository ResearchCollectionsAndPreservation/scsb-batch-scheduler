package org.recap.batch.job;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
import org.recap.batch.service.IdentifyPendingRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

public class IdentifyAndNotifyPendingRequestTasklet extends JobCommonTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(IdentifyAndNotifyPendingRequestTasklet.class);

    @Autowired
    private IdentifyPendingRequestService identifyPendingRequestService;

    /**
     * This method starts the execution for checking the requests if they are in "Pending" status and notify them through sending email.
     *
     * @param contribution StepContribution
     * @param chunkContext ChunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Identifying requests that are in Pending status ...");
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        try {
            updateJob(jobExecution,"IdentifyAndNotifyPendingRequestTasklet", Boolean.FALSE);
            identifyPendingRequestService.identifyPendingRequestService(scsbCircUrl);
        } catch (Exception ex) {
            logger.error("{} {}", RecapCommonConstants.LOG_ERROR, ExceptionUtils.getMessage(ex));
            executionContext.put(RecapConstants.JOB_STATUS, RecapConstants.FAILURE);
            executionContext.put(RecapConstants.JOB_STATUS_MESSAGE, ExceptionUtils.getMessage(ex));
            stepExecution.setExitStatus(new ExitStatus(RecapConstants.FAILURE, ExceptionUtils.getFullStackTrace(ex)));
        }
        return RepeatStatus.FINISHED;
    }
}
