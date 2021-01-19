package org.recap.batch.job;

import org.recap.RecapCommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * Created by rajeshbabuk on 29/6/17.
 */
public class DeletedRecordsExportNyplTasklet extends DeletedRecordsExportTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(DeletedRecordsExportNyplTasklet.class);

    /**
     * This method starts the execution of deleted records export job for New York.
     *
     * @param contribution StepContribution
     * @param chunkContext ChunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Executing - DeletedRecordsExportNyplTasklet");
        return executeDeletedRecordsExport(chunkContext, logger, RecapCommonConstants.NYPL, Boolean.TRUE);
    }
}