package org.recap.batch.service;

import org.recap.RecapConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by rajeshbabuk on 18/4/17.
 */

@Service
public class PurgeEmailAddressService {

    @Autowired
    protected CommonService commonService;


    /**
     * This method makes a rest call to scsb circ microservice to initiate the process of purging email addresses.
     *
     * @param scsbCoreUrl    the scsb core url
     * @return status of purging email addresses process
     */
    public Map<String, String> purgeEmailAddress(String scsbCoreUrl) {
        return commonService.executePurge(scsbCoreUrl, RecapConstants.PURGE_EMAIL_URL);
    }
}
