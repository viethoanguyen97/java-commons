package com.hovispace.javacommons.facebooksdk.controller;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import static java.util.Objects.isNull;

@Controller
@RequestMapping("/")
public class FacebookController {

    private final ConnectionRepository _connectionRepository;

    public FacebookController( ConnectionRepository connectionRepository) {
        _connectionRepository = connectionRepository;
    }

    @RequestMapping(value = "feed", method = RequestMethod.GET)
    public String feed(Model model) {
        Connection<Facebook> facebookConnection = _connectionRepository.findPrimaryConnection(Facebook.class);
        if (isNull(facebookConnection)) {
            return "redirect:/connect/facebook";
        }

        Facebook facebook = facebookConnection.getApi();
        User userProfile = facebook.userOperations().getUserProfile();
        model.addAttribute("userProfile", userProfile);
        PagedList<Post> userFeed = facebook.feedOperations().getFeed();
        model.addAttribute("userFeed", userFeed);

        return "feed";
    }

    @RequestMapping(value = "friends", method = RequestMethod.GET)
    public String friends(Model model) {
        Connection<Facebook> facebookConnection = _connectionRepository.findPrimaryConnection(Facebook.class);
        if (isNull(facebookConnection)) {
            return "redirect:/connect/facebook";
        }

        Facebook facebook = facebookConnection.getApi();
        User userProfile = facebook.userOperations().getUserProfile();
        model.addAttribute("userProfile", userProfile);
        List<User> friends = facebook.friendOperations().getFriendProfiles();
        model.addAttribute("friends", friends);

        return "friends";
    }

    @RequestMapping(value = "pages", method = RequestMethod.GET)
    public String pages(Model model) {
        Connection<Facebook> facebookConnection = _connectionRepository.findPrimaryConnection(Facebook.class);
        if (isNull(facebookConnection)) {
            return "redirect:/connect/facebook";
        }
        Facebook facebook = facebookConnection.getApi();
        PageOperations pageOperations = facebook.pageOperations();
        PagedList<Account> accounts = pageOperations.getAccounts();
        model.addAttribute("pages", accounts);
//        System.out.println(accounts);
//        User userProfile = facebook.userOperations().getUserProfile();
//        model.addAttribute("userProfile", userProfile);
//        List<User> friends = facebook.friendOperations().getFriendProfiles();
//        model.addAttribute("friends", friends);

        return "pages";
    }
}
