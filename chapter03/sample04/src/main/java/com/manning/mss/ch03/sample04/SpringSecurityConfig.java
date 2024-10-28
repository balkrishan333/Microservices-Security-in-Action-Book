package com.manning.mss.ch03.sample04;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AppUtils {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
