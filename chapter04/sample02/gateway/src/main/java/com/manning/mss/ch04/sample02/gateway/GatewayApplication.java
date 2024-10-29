package com.manning.mss.ch04.sample02.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    // @Bean
    // public WebMvcConfigurer corsConfigurer() {
    //     return new WebMvcConfigurer() {
    //         public void addCorsMappings(CorsRegistry registry) {
    //             registry.addMapping("/books/**")
    //                     .allowedOrigins("http://localhost:4200")
    //                     .allowedMethods("GET", "POST");
    //         }
    //     };
    // }

}
