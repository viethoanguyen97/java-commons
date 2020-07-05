package com.hovispace.javacommons.facebooksdk.service.facebook;

import com.hovispace.javacommons.facebooksdk.model.User;
import com.hovispace.javacommons.facebooksdk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;

@Component
@Qualifier("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository _userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        _userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = _userRepository.findByUsername(username);
        if (isNull(user)) {
            throw new UsernameNotFoundException(username);
        }

        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), true, true, true, true, singletonList(new SimpleGrantedAuthority("FACEBOOK_USER")));
    }
}
