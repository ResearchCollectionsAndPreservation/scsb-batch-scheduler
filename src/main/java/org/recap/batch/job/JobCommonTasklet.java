package org.recap.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.PollingConsumer;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.quartz.JobDataMap;
import org.quartz.impl.JobDetailImpl;
import org.recap.PropertyKeyConstants;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.batch.service.UpdateJobDetailsService;
import org.recap.quartz.QuartzJobLauncher;
import org.recap.util.JobDataParameterUtil;
import org.slf4j.Logger;
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
import java.util.Map;

@Slf4j
public class JobCommonTasklet {

    @Value("${" + PropertyKeyConstants.SCSB_SOLR_DOC_URL + "}")
    protected String solrClientUrl;

    @Value("${" + PropertyKeyConstants.SCSB_CIRC_URL + "}")
    protected String scsbCircUrl;

    @Value("${" + PropertyKeyConstants.SCSB_CORE_URL + "}")
    protected String scsbCoreUrl;

    @Value("${" + PropertyKeyConstants.SCSB_ETL_URL + "}")
    protected String scsbEtlUrl;

    @Value("${" + PropertyKeyConstants.SCSB_ETL_DATADUMP_URL + "}")
    protected String scsbEtlUrlFullExport;

    @Autowired
    protected CamelContext camelContext;

    @Autowired
    protected ProducerTemplate producerTemplate;

    @Autowired
    protected UpdateJobDetailsService updateJobDetailsService;

    @Autowired
    protected JobDataParameterUtil jobDataParameterUtil;

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
                if (!resultSplitMessage[0].equalsIgnoreCase(ScsbCommonConstants.JOB_ID + ":" + jobExecution.getId())) {
                    producerTemplate.sendBody(completeQueueName, resultStatus);
                    resultStatus = ScsbConstants.FAILURE + " - " + ScsbConstants.FAILURE_QUEUE_MESSAGE;
                } else {
                    resultStatus = resultSplitMessage[1];
                }
            }
        }
        catch (Exception ex) {
            logger.error("{} {} ",ScsbCommonConstants.LOG_ERROR, ExceptionUtils.getMessage(ex));
            executionContext.put(ScsbConstants.JOB_STATUS, ScsbConstants.FAILURE);
            executionContext.put(ScsbConstants.JOB_STATUS_MESSAGE, statusName + " " + ExceptionUtils.getMessage(ex));
            stepExecution.setExitStatus(new ExitStatus(ScsbConstants.FAILURE, ExceptionUtils.getFullStackTrace(ex)));
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
            String jobNameParam = (String) jobExecution.getExecutionContext().get(ScsbConstants.JOB_NAME);
            log.info("Job Parameter in {} : {}" , taskletName , jobNameParam);
            if (!jobName.equalsIgnoreCase(jobNameParam)) {
                updateJobDetailsService.updateJob(solrClientUrl, jobName, createdDate, jobInstanceId);
            }
        }
        else {
            updateJobDetailsService.updateJob(solrClientUrl, jobName, createdDate, jobInstanceId);
        }
    }

    public ExecutionContext setExecutionContext(ExecutionContext executionContext, StepExecution stepExecution, String resultStatus) {
        if (!StringUtils.containsIgnoreCase(resultStatus, ScsbConstants.SUCCESS) || StringUtils.containsIgnoreCase(resultStatus, ScsbCommonConstants.FAIL)) {
            executionContext.put(ScsbConstants.JOB_STATUS, ScsbConstants.FAILURE);
            executionContext.put(ScsbConstants.JOB_STATUS_MESSAGE, resultStatus);
            stepExecution.setExitStatus(new ExitStatus(ScsbConstants.FAILURE, resultStatus));
        } else {
            executionContext.put(ScsbConstants.JOB_STATUS, ScsbConstants.SUCCESS);
            executionContext.put(ScsbConstants.JOB_STATUS_MESSAGE, resultStatus);
            stepExecution.setExitStatus(new ExitStatus(ScsbConstants.SUCCESS, resultStatus));
        }
        return executionContext;
    }
    public void setJobDetailImpl(JobDetailImpl jobDetailImpl, String jobName, JobLauncher jobLauncher, JobLocator jobLocator) {
        jobDetailImpl.setName(jobName);
        jobDetailImpl.setJobClass(QuartzJobLauncher.class);
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(ScsbConstants.JOB_NAME, jobName);
        jobDataMap.put(ScsbConstants.JOB_LAUNCHER, jobLauncher);
        jobDataMap.put(ScsbConstants.JOB_LOCATOR, jobLocator);
        jobDetailImpl.setJobDataMap(jobDataMap);
    }

    protected Date getCreatedDate(JobExecution jobExecution) {
        Date createdDate = null;
        try {
            String fromDate = jobExecution.getJobParameters().getString(ScsbConstants.FROM_DATE);

            if (StringUtils.isNotBlank(fromDate)) {
                SimpleDateFormat dateFormatter = new SimpleDateFormat(ScsbConstants.FROM_DATE_FORMAT);
                createdDate = dateFormatter.parse(fromDate);
            } else {
                createdDate = jobExecution.getCreateTime();
            }
        } catch (ParseException e) {
            log.error("{} {}", ScsbCommonConstants.LOG_ERROR, ExceptionUtils.getMessage(e));

        }
        return createdDate;
    }

    public String getExportInstitutionFromParameters(JobExecution jobExecution) {
        String exportInstitution = null;
        try {
            exportInstitution = jobExecution.getJobParameters().getString(ScsbCommonConstants.INSTITUTION);
            if (StringUtils.isBlank(exportInstitution)) {
                Map<String, String> requestParameterMap = jobDataParameterUtil.buildJobRequestParameterMap(jobExecution.getJobInstance().getJobName());
                exportInstitution = requestParameterMap.get(ScsbCommonConstants.INSTITUTION);
            }
        } catch (Exception e) {
            log.error("{} {}", ScsbCommonConstants.LOG_ERROR, ExceptionUtils.getMessage(e));
        }
        return exportInstitution;
    }

    public void updateExecutionExceptionStatus(StepExecution stepExecution, ExecutionContext executionContext, Exception ex, String exceptionCustomMsg) {
        log.error("{} {}", ScsbCommonConstants.LOG_ERROR, ExceptionUtils.getMessage(ex));
        executionContext.put(ScsbConstants.JOB_STATUS, ScsbConstants.FAILURE);
        executionContext.put(ScsbConstants.JOB_STATUS_MESSAGE, exceptionCustomMsg + " " + ExceptionUtils.getMessage(ex));
        stepExecution.setExitStatus(new ExitStatus(ScsbConstants.FAILURE, ExceptionUtils.getFullStackTrace(ex)));
    }
    
}
