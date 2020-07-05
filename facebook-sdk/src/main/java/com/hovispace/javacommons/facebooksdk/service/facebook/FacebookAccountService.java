package com.hovispace.javacommons.facebooksdk.service.facebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.*;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
public class FacebookAccountService {

//    private final Connection<Facebook> _facebookConnection;
//
//    @Autowired
//    public FacebookAccountService(ConnectionRepository connectionRepository) {
//        _facebookConnection = connectionRepository.findPrimaryConnection(Facebook.class);
//    }
//
//    public void getListFriends(String userId) {
//        checkNotNull(_facebookConnection, "Facebook connection is null");
//        Facebook facebook = _facebookConnection.getApi();
//        FriendOperations friendOperations = facebook.friendOperations();
//        UserOperations userOperations = facebook.userOperations();
//
//        List<String> friendsId = friendOperations.getFriendIds();
//        List<User> friends = friendOperations.getFriendProfiles();
//
//        friends.stream().map(User::toString).forEach(System.out::println);
//    }
//
//    public void getListPages(String userId) {
//        checkNotNull(_facebookConnection, "Facebook connection is null");
//        Facebook facebook = _facebookConnection.getApi();
//        PageOperations pageOperations = facebook.pageOperations();
//        UserOperations userOperations = facebook.userOperations();
//
//        List<Page> pages = pageOperations.search("SELECT ");
//
//
//    }

}
