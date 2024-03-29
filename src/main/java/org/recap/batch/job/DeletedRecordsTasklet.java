package org.recap.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.recap.ScsbConstants;
import org.recap.batch.service.ReportDeletedRecordsService;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by rajeshbabuk on 28/3/17.
 */
@Slf4j
public class DeletedRecordsTasklet extends JobCommonTasklet implements Tasklet {

    String serverProtocol;

    @Autowired
    private ReportDeletedRecordsService reportDeletedRecordsService;


    /**
     * This method starts the execution of reporting deleted records.
     *
     * @param contribution StepContribution
     * @param chunkContext ChunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Executing DeletedRecords ");
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        try {
            updateJob(jobExecution,"DeletedRecordsTasklet", Boolean.FALSE);
            String resultStatus = reportDeletedRecordsService.reportDeletedRecords(scsbCoreUrl);
            log.info("Deleted records status : {} " , resultStatus);
            setExecutionContext(executionContext, stepExecution, resultStatus);
        } catch (Exception ex) {
            updateExecutionExceptionStatus(stepExecution, executionContext, ex, ScsbConstants.DELETED_RECORDS_STATUS_NAME);
        }
        return RepeatStatus.FINISHED;
    }
}
