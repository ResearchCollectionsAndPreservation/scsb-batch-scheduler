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
public class IncrementalExportPulTasklet extends IncrementalExportTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(IncrementalExportPulTasklet.class);

    /**
     * This method starts the execution of incremental export job for Princeton.
     *
     * @param contribution StepContribution
     * @param chunkContext ChunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Executing - IncrementalExportPulTasklet");
        return executeIncrementalExport(chunkContext, logger, RecapCommonConstants.PRINCETON);
    }
}

