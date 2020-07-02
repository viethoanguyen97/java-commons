package com.hovispace.javacommons.springsocial.configuration;

import com.hovispace.javacommons.springsocial.service.facebook.FacebookConnectionSignup;
import com.hovispace.javacommons.springsocial.service.facebook.FacebookSignInAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.social.facebook.appId}")
    private String _appId;

    @Value("${spring.social.facebook.appSecret}")
    private String _appSecret;

    private final UserDetailsService _userDetailsService;
    private final FacebookConnectionSignup _facebookConnectionsSignup;

    @Autowired
    public SecurityConfig(@Qualifier("customUserDetailsService") UserDetailsService userDetailsService, FacebookConnectionSignup facebookConnectionsSignup) {
        _userDetailsService = userDetailsService;
        _facebookConnectionsSignup = facebookConnectionsSignup;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(_userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * I will not spend too much time on this config, a spring-security module will be created soon.
         */
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/login*", "/signin/**", "/signup/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().loginPage("/login").permitAll()
            .and()
            .logout();
    }

    @Bean
    public ConnectionFactoryLocator connectionFactoryLocator() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        registry.addConnectionFactory(new FacebookConnectionFactory(_appId, _appSecret));
        return registry;
    }

    /**
     * we are using a ProviderSignInController to enable the Facebook authentication, which needs 2 things
     * - ConnectionFactoryLocator registered as a FacebookConnectionFactory with the Facebook properties defined earlier.
     * - InMemoryUsersConnectionRepository
     *
     * by sending a POST to "/signin/facebook" - this controller will initate a user sign-in using the Facebook service provider
     * we're setting up a SignInAdapter to handle the login logic in our application and
     * we also setting up a ConnectionSignUp to handle the signin-up users implicitly when they first authenticate with Facebook
     */
    @Bean
    public ProviderSignInController providerSignInController() {
        ConnectionFactoryLocator connectionFactoryLocator = connectionFactoryLocator();
        InMemoryUsersConnectionRepository usersConnectionRepository = new InMemoryUsersConnectionRepository(connectionFactoryLocator);
        usersConnectionRepository.setConnectionSignUp(_facebookConnectionsSignup);
        return new ProviderSignInController(connectionFactoryLocator, usersConnectionRepository, new FacebookSignInAdapter());
    }
}
