package com.hovispace.javacommons.facebooksdk.service.facebook;

import com.facebook.ads.sdk.*;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * With the Pages API your app can get and update page information, and change page settings.
 * Reference: https://developers.facebook.com/docs/pages/managing
 */
@Component
public class FacebookPagesApiService {

    private final String _appSecret;
    private final String _appId;
    private final Boolean _enableDebug;

    public FacebookPagesApiService(Environment environment) {
        _appId = environment.getProperty("facebook.appId");
        _appSecret = environment.getProperty("facebook.appSecret");
        _enableDebug = Boolean.valueOf(environment.getProperty("facebook.enableDebug"));
    }

    /**
     * get list of pages, tasks and tokens
     * required:
     *  - pages_show_list permission
     *  - a User access token to list all Pages on which you can perform the MODERATE tasks on the Page
     *
     * @param userId should be a long-lived token
     * @param userAccessToken
     */
    public List<Page> getListPagesOfUser(String userId, String userAccessToken) throws APIException {
        APIContext apiContext = new APIContext(userAccessToken, _appSecret, _appId).enableDebug(_enableDebug);
        User user = new User(userId, apiContext);

        APINodeList<Page> pages = user.getAccounts().execute();
        List<Page> result = new ArrayList<>();
        while (isNotEmpty(pages)) {
            result.addAll(pages);
            pages.nextPage();
        }

        return result;
    }

    /**
     * get list of managed pages with custom fields
     * required:
     *  - pages_show_list permission
     *  - a User access token to list all Pages on which you can perform the MODERATE tasks on the Page
     *
     * reference ad-hoc apirequest
     *  - https://github.com/facebook/facebook-java-business-sdk#ad-hoc-apirequest
     *
     * @param userId
     * @param userAccessToken
     * @param fields
     * @throws APIException
     */
    public List<Page> getListPagesOfUser(String userId, String userAccessToken, List<String> fields) throws APIException {
        APIContext apiContext = new APIContext(userAccessToken, _appSecret, _appId).enableDebug(_enableDebug);
        APIRequest<Page> request = new APIRequest<>(apiContext, userId, "/accounts", "GET", fields, Page.getParser());
        APINodeList<Page> pages = (APINodeList<Page>) request.execute();

        List<Page> result = new ArrayList<>();
        while (isNotEmpty(pages)) {
            result.addAll(pages);
            pages.nextPage();
        }

        return result;
    }

}
