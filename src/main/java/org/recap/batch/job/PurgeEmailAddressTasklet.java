package org.recap.batch.job;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
import org.recap.batch.service.PurgeEmailAddressService;
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

import java.util.Map;

/**
 * Created by rajeshbabuk on 28/3/17.
 */
public class PurgeEmailAddressTasklet extends JobCommonTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(PurgeEmailAddressTasklet.class);

    @Autowired
    private PurgeEmailAddressService purgeEmailAddressService;

    /**
     * This method starts the execution of purging email addresses job.
     *
     * @param contribution StepContribution
     * @param chunkContext ChunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Executing PurgeEmailAddressTasklet");
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        try {
            updateJob(jobExecution, "PurgeEmailAddressTasklet", Boolean.FALSE);
            Map<String, String> resultMap = purgeEmailAddressService.purgeEmailAddress(scsbCoreUrl);
            String status = resultMap.get(RecapCommonConstants.STATUS);
            String message = RecapCommonConstants.PURGE_EDD_REQUEST + ":" + resultMap.get(RecapCommonConstants.PURGE_EDD_REQUEST)
                    + ", " + RecapCommonConstants.PURGE_PHYSICAL_REQUEST + ":" + resultMap.get(RecapCommonConstants.PURGE_PHYSICAL_REQUEST);
            logger.info("Purge Email Addresses status : {}", status);
            logger.info("Purge Email Addresses status message : {}", message);
            executionContext.put(RecapConstants.JOB_STATUS, status);
            executionContext.put(RecapConstants.JOB_STATUS_MESSAGE, message);
            stepExecution.setExitStatus(new ExitStatus(status, message));
        } catch (Exception ex) {
            logger.error("{} {}", RecapCommonConstants.LOG_ERROR, ExceptionUtils.getMessage(ex));
            executionContext.put(RecapConstants.JOB_STATUS, RecapConstants.FAILURE);
            executionContext.put(RecapConstants.JOB_STATUS_MESSAGE, ExceptionUtils.getMessage(ex));
            stepExecution.setExitStatus(new ExitStatus(RecapConstants.FAILURE, ExceptionUtils.getFullStackTrace(ex)));
        }
        return RepeatStatus.FINISHED;
    }
}
