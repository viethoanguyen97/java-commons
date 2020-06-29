package com.hovispace.javacommons.springgraphql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class SpringGraphQLApplication implements ApplicationListener<ApplicationReadyEvent> {

    private final ApplicationContext _applicationContext;

    @Autowired
    public SpringGraphQLApplication(ApplicationContext applicationContext) {
        _applicationContext = applicationContext;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringGraphQLApplication.class);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            int port = _applicationContext.getBean(Environment.class).getProperty("server.port", Integer.class, 8080);
            System.out.printf("%s:%d", ip, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
