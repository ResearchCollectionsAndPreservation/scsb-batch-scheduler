package org.recap.batch.job;

import org.recap.RecapConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * Created by rajeshbabuk on 14/9/17.
 */
public class StartOrStopPollingLongRunningJobsTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(StartOrStopPollingLongRunningJobsTasklet.class);

    /**
     * This method starts or stops the execution of polling log running jobs.
     *
     * @param contribution StepContribution
     * @param chunkContext ChunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Executing StartOrStopPollingLongRunningJobsTasklet");
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        String action = jobExecution.getJobParameters().getString(RecapConstants.POLLING_ACTION);
        if (RecapConstants.START.equalsIgnoreCase(action)) {
            RecapConstants.POLL_LONG_RUNNING_JOBS = true;
            logger.info("Started polling long running jobs");
            stepExecution.setExitStatus(new ExitStatus(RecapConstants.SUCCESS,  "Started polling long running jobs"));
        } else if (RecapConstants.STOP.equalsIgnoreCase(action)) {
            RecapConstants.POLL_LONG_RUNNING_JOBS = false;
            logger.info("Stopped polling long running jobs");
            stepExecution.setExitStatus(new ExitStatus(RecapConstants.SUCCESS,  "Stopped polling long running jobs"));
        }
        return RepeatStatus.FINISHED;
    }
}
