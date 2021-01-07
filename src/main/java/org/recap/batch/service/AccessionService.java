package org.recap.batch.service;

import org.recap.RecapConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

/**
 * Created by angelind on 17/5/17.
 */
@Service
public class AccessionService {

    @Autowired
    protected CommonService commonService;

    /**
     * This method makes a rest call to solr client microservice to initiate the accession process.
     *
     * @param scsbCoreUrl     the scsb core url
     * @return status of the accession process
     */
    public String processAccession(String scsbCoreUrl) {
        return commonService.executeService(scsbCoreUrl,  RecapConstants.ACCESSION_URL, HttpMethod.GET);
    }

}
