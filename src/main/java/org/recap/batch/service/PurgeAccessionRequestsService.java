package org.recap.batch.service;

import org.recap.RecapConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by rajeshbabuk on 22/5/17.
 */
@Service
public class PurgeAccessionRequestsService {

    @Autowired
    protected CommonService commonService;

    /**
     * This method makes a rest call to scsb circ microservice to initiate the process of purging accession requests.
     *
     * @param scsbCoreUrl    the scsb core url
     * @return status of purging accession requests process
     */
    public Map<String, String> purgeAccessionRequests(String scsbCoreUrl) {
        return commonService.executePurge(scsbCoreUrl, RecapConstants.PURGE_ACCESSION_REQUEST_URL);
    }
}
