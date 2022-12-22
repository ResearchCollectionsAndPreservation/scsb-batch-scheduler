package org.recap.batch.service;

import org.apache.commons.lang.StringUtils;
import org.recap.PropertyKeyConstants;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.model.batch.SolrIndexRequest;
import org.recap.util.JobDataParameterUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;

@Service
public class CommonService {

    @Value("${" + PropertyKeyConstants.SCSB_SWAGGER_API_KEY + "}")
    private String apiKey;

    /**
     * Gets rest template.
     *
     * @return the rest template
     */
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    public HttpEntity getHttpEntity() {
        return new HttpEntity<>(getHttpHeaders());
    }

    public HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ScsbCommonConstants.API_KEY, apiKey);
        return headers;
    }

    public String executeService(String url, String jobUrl, HttpMethod httpMethod) {
        HttpEntity httpEntity = getHttpEntity();
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(url + jobUrl, httpMethod, httpEntity, String.class);
        return responseEntity.getBody();
    }

    public  Map<String, String> executePurge(String scsbCircUrl, String url) {
        HttpEntity httpEntity = getHttpEntity();
        ResponseEntity<Map> responseEntity = getRestTemplate().exchange(scsbCircUrl + url, HttpMethod.GET, httpEntity, Map.class);
        return responseEntity.getBody();
    }

    public String pendingRequest(String url, String jobUrl) {
        HttpEntity httpEntity = getHttpEntity();
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(url + jobUrl, HttpMethod.GET, httpEntity, String.class);
        return responseEntity.getBody();
    }

    public String getResponse(SolrIndexRequest solrIndexRequest, String solrClientUrl, String url, HttpMethod httpMethod) {
        HttpHeaders headers = getHttpHeaders();
        HttpEntity<SolrIndexRequest> httpEntity = new HttpEntity<>(solrIndexRequest, headers);
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(solrClientUrl + url, httpMethod, httpEntity, String.class);
        return responseEntity.getBody();
    }

    protected void setRequestParameterMap(Map<String, String>  requestParameterMap, String exportStringDate,JobDataParameterUtil jobDataParameterUtil,  Date createdDate) {
        if (StringUtils.isBlank(exportStringDate)) {
            requestParameterMap.put(ScsbConstants.DATE, jobDataParameterUtil.getDateFormatStringForExport(createdDate));
        } else {
            requestParameterMap.put(ScsbConstants.DATE, exportStringDate);
        }
    }

    public String requestLog(String url, String jobUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(url + jobUrl, HttpMethod.GET, httpEntity, String.class);
        return responseEntity.getBody();
    }
}
