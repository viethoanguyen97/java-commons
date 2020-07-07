package com.hovispace.javacommons.facebooksdk.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final Environment _environment;
    private final UserDetailsService _userDetailsService;

    @Autowired
    public SecurityConfig(Environment environment, @Qualifier("customUserDetailsService") UserDetailsService userDetailsService) {
        _environment = environment;
        _userDetailsService = userDetailsService;
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

}

