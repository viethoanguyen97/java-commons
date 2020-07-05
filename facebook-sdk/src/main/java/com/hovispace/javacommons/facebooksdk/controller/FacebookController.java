package com.hovispace.javacommons.facebooksdk.controller;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import static java.util.Objects.isNull;

@Controller
@RequestMapping("/")
public class FacebookController {

//    private Facebook _facebook;
//    private ConnectionRepository _connectionRepository;
//
//    public FacebookController(Facebook facebook, ConnectionRepository connectionRepository) {
//        _facebook = facebook;
//        _connectionRepository = connectionRepository;
//    }
//
//    @RequestMapping(value = "feed", method = RequestMethod.GET)
//    public String feed(Model model) {
//        if (isNull(_connectionRepository.findPrimaryConnection(Facebook.class))) {
//            return "redirect:/connect/facebook";
//        }
//
//        Facebook facebook = _connectionRepository.findPrimaryConnection(Facebook.class).getApi();
//        User userProfile = facebook.userOperations().getUserProfile();
//        model.addAttribute("userProfile", userProfile);
//        PagedList<Post> userFeed = facebook.feedOperations().getFeed();
//        model.addAttribute("userFeed", userFeed);
//
//        return "feed";
//    }
//
//    @RequestMapping(value = "friends", method = RequestMethod.GET)
//    public String friends(Model model) {
//        if (_connectionRepository.findPrimaryConnection(Facebook.class) == null) {
//            return "redirect:/connect/facebook";
//        }
//
//        Facebook facebook = _connectionRepository.findPrimaryConnection(Facebook.class).getApi();
//        User userProfile = facebook.userOperations().getUserProfile();
//        model.addAttribute("userProfile", userProfile);
//        List<User> friends = facebook.friendOperations().getFriendProfiles();
//        model.addAttribute("friends", friends);
//
//        return "friends";
//    }
}
