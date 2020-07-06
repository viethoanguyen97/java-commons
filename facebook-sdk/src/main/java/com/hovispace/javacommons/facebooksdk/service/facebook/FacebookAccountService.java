package com.hovispace.javacommons.facebooksdk.service.facebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Account;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PageOperations;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
public class FacebookAccountService {

    private final ConnectionRepository _connectionRepository;

    @Autowired
    public FacebookAccountService(ConnectionRepository connectionRepository) {
        _connectionRepository = connectionRepository;
    }

    //get lists pages of user
    public PagedList<Account> getListPages() {
        Connection<Facebook> facebookConnection = _connectionRepository.findPrimaryConnection(Facebook.class);
        checkNotNull(facebookConnection, "Facebook connection is null");

        Facebook facebook = facebookConnection.getApi();
        PageOperations pageOperations = facebook.pageOperations();
        PagedList<Account> accounts = pageOperations.getAccounts();

        return accounts;
    }

}
