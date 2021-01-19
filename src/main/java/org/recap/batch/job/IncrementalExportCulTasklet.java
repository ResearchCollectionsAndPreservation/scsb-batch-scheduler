package org.recap.batch.job;

import org.recap.RecapCommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * Created by rajeshbabuk on 23/6/17.
 */
public class IncrementalExportCulTasklet extends IncrementalExportTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(IncrementalExportCulTasklet.class);

    /**
     * This method starts the execution of incremental export job for Columbia.
     *
     * @param contribution StepContribution
     * @param chunkContext ChunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Executing - IncrementalExportCulTasklet");
        return executeIncrementalExport(chunkContext, logger, RecapCommonConstants.COLUMBIA);
    }
}
