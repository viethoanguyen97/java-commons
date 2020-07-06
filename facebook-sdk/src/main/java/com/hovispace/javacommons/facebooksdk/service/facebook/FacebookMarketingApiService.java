package com.hovispace.javacommons.facebooksdk.service.facebook;

import com.facebook.ads.sdk.*;
import com.facebook.ads.sdk.Lead.APIRequestGet;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;

@Component
public class FacebookMarketingApiService {

    private final String _appSecret;
    private final String _appId;

    private final Environment _environment;
    private final FacebookAccountService _facebookAccountService;

    public FacebookMarketingApiService(Environment environment, FacebookAccountService facebookAccountService) {
        _environment = environment;
        _facebookAccountService = facebookAccountService;

        _appId = _environment.getProperty("facebook.appId");
        _appSecret = _environment.getProperty("facebook.appSecret");
    }

//
//    public void createTestAd(String accessToken, String pageId, String adAccountId) {
//        APIContext context = new APIContext(accessToken).enableDebug(true);
//
//        new AdAccount(adAccountId, context)
//            .createAdCreative()
//            .setObjectStorySpec(new AdCreativeObjectStorySpec().setFieldPageId(pageId).setFieldVideoData(new AdCreativeVideoData()
//                            .setFieldCallToAction(
//                                new AdCreativeLinkDataCallToAction()
//                                    .setFieldType(AdCreativeLinkDataCallToAction.EnumType.VALUE_SIGN_UP)
//                                    .setFieldValue(
//                                        new AdCreativeLinkDataCallToActionValue().setFieldLeadGenFormId("<formID>") .setFieldLink(\"http://fb.me/\"))).setFieldImageUrl(\"<imageURL>\").setFieldLinkDescription(\"try it out\").setFieldVideoId(\"<videoID>\"))) .execute();
//    }

    public List<LeadgenForm> getLeadGenForms(String pageId, String accessToken) throws APIException {
        checkNotNull(_appId);
        checkNotNull(_appSecret);
        APIContext apiContext = new APIContext(accessToken).enableDebug(true);
        Page page = new Page(pageId, apiContext);

        APINodeList<LeadgenForm> leadgenForms = page.getLeadGenForms().execute();
        List<LeadgenForm> result = new ArrayList<>();
        while (!leadgenForms.isEmpty()) {
            result.addAll(leadgenForms);
        }

        return result;
    }

    public List<Lead> getLeadInfos(List<String> leadIds, String accessToken) throws APIException {
        checkNotNull(_appId);
        checkNotNull(_appSecret);
        APIContext apiContext = new APIContext(accessToken).enableDebug(true);
        APINodeList<Lead> leads = Lead.fetchByIds(leadIds, asList(APIRequestGet.FIELDS), apiContext);
        List<Lead> result = new ArrayList<>();
        while (!leads.isEmpty()) {
            result.addAll(leads);
        }

        return result;
    }

    public Lead getLeadInfo(String leadId, String accessToken) throws APIException {
        checkNotNull(_appId);
        checkNotNull(_appSecret);

        APIContext apiContext = new APIContext(accessToken).enableDebug(true);
        Lead lead = new Lead(leadId, apiContext).get().execute();
        //Lead lead = Lead.fetchById(leadId, apiContext);

        return lead;
    }

}
