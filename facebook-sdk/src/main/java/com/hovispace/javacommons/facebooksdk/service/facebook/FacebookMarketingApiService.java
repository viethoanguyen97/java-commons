package com.hovispace.javacommons.facebooksdk.service.facebook;

import com.facebook.ads.sdk.*;
import com.facebook.ads.sdk.Lead.APIRequestGet;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * To read ad specific fields, such as ad_id, campaign_id, you will need:
 *     A Page or User access token requested by a person who can advertise on the ad account and on the Page
 *          - The ads_management permission
 *          - The pages_read_engagement permission
 *          - The pages_manage_metadata permission - if using webhooks
 *     To read all lead data and ad level data, you will need:
 *          - A Page or User access token requested by a person who can advertise on the ad account and on the Page
 *          - The ads_management permission
 *          - The leads_retrieval permission
 *          - The pages_read_engagement permission
 *
 *  For more information, please refer the official document: https://developers.facebook.com/docs/marketing-api/guides/lead-ads/retrieving
 */
@Component
public class FacebookMarketingApiService {

    private final String _appSecret;
    private final String _appId;
    private final Boolean _enableDebug;

    public FacebookMarketingApiService(Environment environment) {
        _appId = environment.getProperty("facebook.appId");
        _appSecret = environment.getProperty("facebook.appSecret");
        _enableDebug = Boolean.valueOf(environment.getProperty("facebook.enableDebug"));
    }

    /**
     * get all lead gen of page
     * @param pageId
     * @param accessToken long-lived page access token
     * @return
     * @throws APIException
     */
    public List<LeadgenForm> getLeadGenForms(String pageId, String accessToken) throws APIException {
        checkNotNull(_appId);
        checkNotNull(_appSecret);
        APIContext apiContext = new APIContext(accessToken, _appSecret, _appId).enableDebug(_enableDebug);
        Page page = new Page(pageId, apiContext);

        APINodeList<LeadgenForm> leadgenForms = page.getLeadGenForms().execute();
        List<LeadgenForm> result = new ArrayList<>();
        while (isNotEmpty(leadgenForms)) {
            result.addAll(leadgenForms);
            leadgenForms = leadgenForms.nextPage();
        }

        return result;
    }

    /**
     * get bulk lead
     * @param leadIds
     * @param accessToken long-lived page access token
     * @return
     * @throws APIException
     */
    public List<Lead> getLeadInfos(List<String> leadIds, String accessToken) throws APIException {
        checkNotNull(_appId);
        checkNotNull(_appSecret);

        APIContext apiContext = new APIContext(accessToken, _appSecret, _appId).enableDebug(_enableDebug);
        APINodeList<Lead> leads = Lead.fetchByIds(leadIds, asList(APIRequestGet.FIELDS), apiContext);
        List<Lead> result = new ArrayList<>();
        while (isNotEmpty(leads)) {
            result.addAll(leads);
            leads = leads.nextPage();
        }

        return result;
    }

    /**
     * get lead info from a lead
     * @param leadId
     * @param accessToken long-lived page access token
     * @return
     * @throws APIException
     */
    public Lead getLeadInfo(String leadId, String accessToken) throws APIException {
        checkNotNull(_appId);
        checkNotNull(_appSecret);

        APIContext apiContext = new APIContext(accessToken, _appSecret, _appId).enableDebug(_enableDebug);
        Lead lead = new Lead(leadId, apiContext).get().execute();
        //Lead lead = Lead.fetchById(leadId, apiContext);

        return lead;
    }

}
