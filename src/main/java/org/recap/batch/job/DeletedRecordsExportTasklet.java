package org.recap.batch.job;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
import org.recap.batch.service.RecordsExportService;
import org.slf4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class DeletedRecordsExportTasklet extends JobCommonTasklet {

    @Autowired
    private RecordsExportService recordsExportService;

    /**
     * This method starts the execution of deleted records export job for any passed institution.
     *
     * @param chunkContext chunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    public RepeatStatus executeDeletedRecordsExport(ChunkContext chunkContext, Logger logger, String exportInstitution, Boolean check) throws Exception {
        logger.info("Executing DeletedRecordsExport for {}", exportInstitution);
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        try {
            String exportStringDate = jobExecution.getJobParameters().getString(RecapConstants.FROM_DATE);
            Date createdDate = jobExecution.getCreateTime();
            updateJob(jobExecution, "DeletedRecordsExport" + StringUtils.capitalize(exportInstitution.toLowerCase()) + "Tasklet", check);
            String resultStatus = recordsExportService.exportRecords(scsbEtlUrl, RecapConstants.DELETED_RECORDS_EXPORT + StringUtils.capitalize(exportInstitution.toLowerCase()), createdDate, exportStringDate, exportInstitution);
            logger.info("Deleted Records Export {} status : {}", exportInstitution, resultStatus);
            setExecutionContext(executionContext, stepExecution, resultStatus);
        } catch (Exception ex) {
            logger.error("{} {}", RecapCommonConstants.LOG_ERROR, ExceptionUtils.getMessage(ex));
            executionContext.put(RecapConstants.JOB_STATUS, RecapConstants.FAILURE);
            executionContext.put(RecapConstants.JOB_STATUS_MESSAGE, ExceptionUtils.getMessage(ex));
            stepExecution.setExitStatus(new ExitStatus(RecapConstants.FAILURE, ExceptionUtils.getFullStackTrace(ex)));
        }
        return RepeatStatus.FINISHED;
    }
}