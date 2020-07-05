package com.hovispace.javacommons.facebooksdk.service.facebook;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

import static java.util.Collections.singletonList;

/**
 * The sign-in adapter is a bridge between the controller - driving the Facebook user sign-in flow - and our specific local application
 * The users logged-in using Facebook will have role FACEBOOK_USER
 */
@Component
public class FacebookSignInAdapter implements SignInAdapter {

    @Override
    public String signIn(String s, Connection<?> connection, NativeWebRequest nativeWebRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(connection.getDisplayName(), null, singletonList(new SimpleGrantedAuthority("FACEBOOK_USER")));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return null;
    }
}
