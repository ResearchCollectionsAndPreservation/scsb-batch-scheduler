package org.recap.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.recap.ScsbConstants;
import org.recap.batch.service.RequestInitialLoadService;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by hemalathas on 16/6/17.
 */
@Slf4j
public class RequestInitialLoadTasklet extends JobCommonTasklet implements Tasklet {

    @Autowired
    private RequestInitialLoadService requestInitialLoadService;

    /**
     * This method starts the execution of Request Initial Loas.
     *
     * @param contribution StepContribution
     * @param chunkContext ChunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Executing Request Initial Load..........");
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        try {
            updateJob(jobExecution, "RequestInitialLoadTasklet", Boolean.FALSE);
            String resultStatus = requestInitialLoadService.requestInitialLoad(scsbCircUrl);
            log.info("Request Initial Load status : {}", resultStatus);
            setExecutionContext(executionContext, stepExecution, resultStatus);
        } catch (Exception ex) {
            updateExecutionExceptionStatus(stepExecution, executionContext, ex, ScsbConstants.REQUEST_INITIAL_LOAD_STATUS_NAME);
        }
        return RepeatStatus.FINISHED;
    }

}
