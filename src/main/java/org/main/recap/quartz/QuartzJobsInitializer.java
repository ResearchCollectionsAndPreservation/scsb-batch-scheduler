package org.main.recap.quartz;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.main.recap.RecapConstants;
import org.main.recap.jpa.JobDetailsRepository;
import org.main.recap.model.jpa.JobEntity;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.quartz.CronExpression.isValidExpression;

/**
 * Created by rajeshbabuk on 28/3/17.
 */
@Component
public class QuartzJobsInitializer {

    private static final Logger logger = LoggerFactory.getLogger(QuartzJobsInitializer.class);

    private JobLauncher jobLauncher;
    private JobLocator jobLocator;
    private JobDetailsRepository jobDetailsRepository;
    private Scheduler scheduler;

    @Autowired
    public QuartzJobsInitializer(JobLauncher jobLauncher, JobLocator jobLocator, JobDetailsRepository jobDetailsRepository, Scheduler scheduler) {
        this.jobLauncher = jobLauncher;
        this.jobLocator = jobLocator;
        this.jobDetailsRepository = jobDetailsRepository;
        this.scheduler = scheduler;
        initializeJobs();
    }

    /**
     * This method reads the jobs from database and initializes them with the quartz scheduler.
     */
    private void initializeJobs() {
        logger.info("Initializing jobs");
        try {
            List<JobEntity> jobEntities = jobDetailsRepository.findAll();
            if (CollectionUtils.isNotEmpty(jobEntities)) {
                for (JobEntity jobEntity : jobEntities) {
                    String jobName = jobEntity.getJobName();
                    String cronExpression = jobEntity.getCronExpression();
                    try {
                        JobDetailImpl jobDetailImpl = new JobDetailImpl();
                        jobDetailImpl.setName(jobName);
                        jobDetailImpl.setJobClass(QuartzJobLauncher.class);
                        JobDataMap jobDataMap = new JobDataMap();
                        jobDataMap.put(RecapConstants.JOB_NAME, jobName);
                        jobDataMap.put(RecapConstants.JOB_LAUNCHER, jobLauncher);
                        jobDataMap.put(RecapConstants.JOB_LOCATOR, jobLocator);
                        jobDetailImpl.setJobDataMap(jobDataMap);
                        if (StringUtils.isNotBlank(cronExpression) && isValidExpression(cronExpression)) {
                            JobKey jobKey = new JobKey(jobName);
                            jobDetailImpl.setKey(jobKey);
                            CronTriggerImpl trigger = new CronTriggerImpl();
                            trigger.setName(jobName + RecapConstants.TRIGGER_SUFFIX);
                            trigger.setJobKey(jobKey);
                            trigger.setCronExpression(cronExpression);
                            scheduler.scheduleJob(jobDetailImpl, trigger);
                            logger.info("Job {} is initialized.", jobName);
                        } else {
                            logger.info("Job {} has invalid cron expression.", jobName);
                            JobKey jobKey = new JobKey(jobName);
                            jobDetailImpl.setKey(jobKey);
                            jobDetailImpl.setDurability(true);
                            scheduler.addJob(jobDetailImpl, true);
                        }
                    } catch(Exception ex) {
                        logger.error("Initializing job " + jobName + " Failed.");
                        logger.error(RecapConstants.LOG_ERROR, ex);
                    }
                }
            }
        } catch (Exception ex) {
            logger.error(RecapConstants.LOG_ERROR, ex);
        }
    }
}
