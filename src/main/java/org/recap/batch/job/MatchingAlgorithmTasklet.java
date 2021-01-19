package org.recap.batch.job;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.PollingConsumer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rajeshbabuk on 3/4/17.
 */
public class MatchingAlgorithmTasklet extends  JobCommonTasklet implements Tasklet{

    private static final Logger logger = LoggerFactory.getLogger(MatchingAlgorithmTasklet.class);

    /**
     * This method starts the execution of the matching algorithm job.
     *
     * @param contribution StepContribution
     * @param chunkContext ChunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Executing MatchingAlgorithmTasklet");
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        PollingConsumer consumer = null;
        try {
            Date createdDate = getCreatedDate(jobExecution);
            updateJob(jobExecution, "Matching Algorithm Tasklet", Boolean.TRUE);
            
            Map<String, String> requestMap = new HashMap<>();
            requestMap.put(RecapCommonConstants.JOB_ID, String.valueOf(jobExecution.getId()));
            requestMap.put(RecapCommonConstants.PROCESS_TYPE, RecapCommonConstants.ONGOING_MATCHING_ALGORITHM_JOB);
            requestMap.put(RecapCommonConstants.CREATED_DATE, createdDate.toString());
            producerTemplate.sendBody(RecapCommonConstants.MATCHING_ALGORITHM_JOB_INITIATE_QUEUE, requestMap);
            Endpoint endpoint = camelContext.getEndpoint(RecapCommonConstants.MATCHING_ALGORITHM_JOB_COMPLETION_OUTGOING_QUEUE);
            consumer = endpoint.createPollingConsumer();
            Exchange exchange = consumer.receive();
            String resultStatus = (String) exchange.getIn().getBody();
            if (StringUtils.isNotBlank(resultStatus)) {
                String[] resultSplitMessage = resultStatus.split("\\|");
                if (!resultSplitMessage[0].equalsIgnoreCase(RecapCommonConstants.JOB_ID + ":" + jobExecution.getId())) {
                    producerTemplate.sendBody(RecapCommonConstants.MATCHING_ALGORITHM_JOB_COMPLETION_OUTGOING_QUEUE, resultStatus);
                    resultStatus = RecapConstants.FAILURE + " - " + RecapConstants.FAILURE_QUEUE_MESSAGE;
                } else {
                    resultStatus = resultSplitMessage[1];
                }
            }
            logger.info("Job Id : {} Matching Algorithm Job Result Status : {}", jobExecution.getId(), resultStatus);
            setExecutionContext(executionContext, stepExecution, RecapConstants.MATCHING_ALGORITHM_STATUS_NAME + " " + resultStatus);
        } catch (Exception ex) {
            logger.error("{} {}", RecapCommonConstants.LOG_ERROR, ExceptionUtils.getMessage(ex));
            executionContext.put(RecapConstants.JOB_STATUS, RecapConstants.FAILURE);
            executionContext.put(RecapConstants.JOB_STATUS_MESSAGE, RecapConstants.MATCHING_ALGORITHM_STATUS_NAME + " " + ExceptionUtils.getMessage(ex));
            stepExecution.setExitStatus(new ExitStatus(RecapConstants.FAILURE, ExceptionUtils.getFullStackTrace(ex)));
        }
        finally {
            if(consumer != null) {
                consumer.close();
            }
        }
        return RepeatStatus.FINISHED;
    }
}
