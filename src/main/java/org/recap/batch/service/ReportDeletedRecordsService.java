package org.recap.batch.service;

import org.recap.RecapConstants;
import org.recap.repository.jpa.JobDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by rajeshbabuk on 12/4/17.
 */
@Service
public class ReportDeletedRecordsService {

    private static final Logger logger = LoggerFactory.getLogger(ReportDeletedRecordsService.class);

    @Autowired
    private JobDetailsRepository jobDetailsRepository;

    @Autowired
    private CommonService commonService;


    /**
     * Gets job details repository.
     *
     * @return the job details repository
     */
    public JobDetailsRepository getJobDetailsRepository() {
        return jobDetailsRepository;
    }

    /**
     * This method makes a rest call to scsb-circ microservice to report about the deleted records, in important transaction tables in SCSB.
     *
     * @param scsbCoreUrl
     * @return
     */
    public String reportDeletedRecords(String scsbCoreUrl){
        HttpEntity httpEntity = commonService.getHttpEntity();
        logger.info("{},{}", scsbCoreUrl , RecapConstants.REPORT_DELETED_RECORDS_URL);
        ResponseEntity<String> responseEntity = commonService.getRestTemplate().exchange(scsbCoreUrl + RecapConstants.REPORT_DELETED_RECORDS_URL, HttpMethod.GET, httpEntity, String.class);
        return responseEntity.getBody();
    }
}
