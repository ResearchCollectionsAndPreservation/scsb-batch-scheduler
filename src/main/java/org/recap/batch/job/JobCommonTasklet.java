package org.recap.batch.job;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.PollingConsumer;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.quartz.JobDataMap;
import org.quartz.impl.JobDetailImpl;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
import org.recap.batch.service.UpdateJobDetailsService;
import org.recap.quartz.QuartzJobLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JobCommonTasklet {

    private static final Logger logger = LoggerFactory.getLogger(JobCommonTasklet.class);

    @Value("${scsb.solr.doc.url}")
    protected String solrClientUrl;

    @Value("${scsb.circ.url}")
    protected String scsbCircUrl;

    @Value("${scsb.core.url}")
    protected String scsbCoreUrl;

    @Value("${scsb.etl.url}")
    protected String scsbEtlUrl;

    @Autowired
    protected CamelContext camelContext;

    @Autowired
    protected ProducerTemplate producerTemplate;

    @Autowired
    protected UpdateJobDetailsService updateJobDetailsService;

    public String getResultStatus(JobExecution jobExecution, StepExecution stepExecution, Logger logger, ExecutionContext executionContext, String initialQueueName, String completeQueueName, String statusName) throws IOException {
        String resultStatus = null;
        PollingConsumer consumer = null;
        try {
            producerTemplate.sendBody(initialQueueName, String.valueOf(jobExecution.getId()));
            Endpoint endpoint = camelContext.getEndpoint(completeQueueName);
            consumer = endpoint.createPollingConsumer();
            Exchange exchange = consumer.receive();
            resultStatus = (String) exchange.getIn().getBody();
            if (StringUtils.isNotBlank(resultStatus)) {
                String[] resultSplitMessage = resultStatus.split("\\|");
                if (!resultSplitMessage[0].equalsIgnoreCase(RecapCommonConstants.JOB_ID + ":" + jobExecution.getId())) {
                    producerTemplate.sendBody(completeQueueName, resultStatus);
                    resultStatus = RecapConstants.FAILURE + " - " + RecapConstants.FAILURE_QUEUE_MESSAGE;
                } else {
                    resultStatus = resultSplitMessage[1];
                }
            }
        }
        catch (Exception ex) {
            logger.error("{} {} ",RecapCommonConstants.LOG_ERROR, ExceptionUtils.getMessage(ex));
            executionContext.put(RecapConstants.JOB_STATUS, RecapConstants.FAILURE);
            executionContext.put(RecapConstants.JOB_STATUS_MESSAGE, statusName + " " + ExceptionUtils.getMessage(ex));
            stepExecution.setExitStatus(new ExitStatus(RecapConstants.FAILURE, ExceptionUtils.getFullStackTrace(ex)));
        }
        finally {
            if (consumer != null) {
                consumer.close();
            }
        }
        return resultStatus;
    }
    public void updateJob(JobExecution jobExecution, String taskletName, Boolean check) throws Exception {
        long jobInstanceId = jobExecution.getJobInstance().getInstanceId();
        String jobName = jobExecution.getJobInstance().getJobName();
        Date createdDate = jobExecution.getCreateTime();
        if(Boolean.TRUE.equals(check)) {
            String jobNameParam = (String) jobExecution.getExecutionContext().get(RecapConstants.JOB_NAME);
            logger.info("Job Parameter in {} : {}" , taskletName , jobNameParam);
            if (!jobName.equalsIgnoreCase(jobNameParam)) {
                updateJobDetailsService.updateJob(solrClientUrl, jobName, createdDate, jobInstanceId);
            }
        }
        else {
            updateJobDetailsService.updateJob(solrClientUrl, jobName, createdDate, jobInstanceId);
        }
    }

    public ExecutionContext setExecutionContext(ExecutionContext executionContext, StepExecution stepExecution, String resultStatus) {
        if (!StringUtils.containsIgnoreCase(resultStatus, RecapConstants.SUCCESS) || StringUtils.containsIgnoreCase(resultStatus, RecapCommonConstants.FAIL)) {
            executionContext.put(RecapConstants.JOB_STATUS, RecapConstants.FAILURE);
            executionContext.put(RecapConstants.JOB_STATUS_MESSAGE, resultStatus);
            stepExecution.setExitStatus(new ExitStatus(RecapConstants.FAILURE, resultStatus));
        } else {
            executionContext.put(RecapConstants.JOB_STATUS, RecapConstants.SUCCESS);
            executionContext.put(RecapConstants.JOB_STATUS_MESSAGE, resultStatus);
            stepExecution.setExitStatus(new ExitStatus(RecapConstants.SUCCESS, resultStatus));
        }
        return executionContext;
    }
    public void setJobDetailImpl(JobDetailImpl jobDetailImpl, String jobName, JobLauncher jobLauncher, JobLocator jobLocator) {
        jobDetailImpl.setName(jobName);
        jobDetailImpl.setJobClass(QuartzJobLauncher.class);
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(RecapConstants.JOB_NAME, jobName);
        jobDataMap.put(RecapConstants.JOB_LAUNCHER, jobLauncher);
        jobDataMap.put(RecapConstants.JOB_LOCATOR, jobLocator);
        jobDetailImpl.setJobDataMap(jobDataMap);
    }

    protected Date getCreatedDate(JobExecution jobExecution) {
        Date createdDate = null;
        try {
            String fromDate = jobExecution.getJobParameters().getString(RecapConstants.FROM_DATE);

            if (StringUtils.isNotBlank(fromDate)) {
                SimpleDateFormat dateFormatter = new SimpleDateFormat(RecapConstants.FROM_DATE_FORMAT);
                createdDate = dateFormatter.parse(fromDate);
            } else {
                createdDate = jobExecution.getCreateTime();
            }
        } catch (ParseException e) {
            logger.error("{} {}", RecapCommonConstants.LOG_ERROR, ExceptionUtils.getMessage(e));

        }
        return createdDate;
    }
    
}
