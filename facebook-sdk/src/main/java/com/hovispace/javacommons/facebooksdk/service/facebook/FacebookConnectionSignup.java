package com.hovispace.javacommons.facebooksdk.service.facebook;

import com.hovispace.javacommons.facebooksdk.model.User;
import com.hovispace.javacommons.facebooksdk.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

/**
 * When a user authenticates with Facebook for the first time, they have no existing account in our application.
 * This is the point where we need to create that account automatically; we'll use ConnectionSignUp to drive that user creation logic
 */
@Component
public class FacebookConnectionSignup implements ConnectionSignUp {

    private static final Logger LOGGER = LoggerFactory.getLogger(FacebookConnectionSignup.class);
    private final UserRepository _userRepository;

    @Autowired
    public FacebookConnectionSignup(UserRepository userRepository) {
        _userRepository = userRepository;
    }

    @Override
    public String execute(Connection<?> connection) {
        User user = new User();
        user.setUsername(connection.getDisplayName()); //we created an account for the new user – using their DisplayName as username, this is for tutorial purpose only, username should be handled more carefully.
        user.setPassword(RandomStringUtils.randomAlphabetic(8)); //again, this is only a tutorial, so I'll skip the password encoder.
        _userRepository.save(user);

        LOGGER.info("Signup a Facebook user: {}", user.toString());
        return user.getUsername();
    }
}
