package com.manning.mss.ch03.sample04.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

@Component
public class OAuthFilter extends AbstractGatewayFilterFactory<OAuthFilter.Config> {

    @Autowired
    private RouteValidator validator;
    @Autowired
    private RestTemplate restTemplate;

    public OAuthFilter() {
        super(Config.class);
    }
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {
                   //REST call to AUTH service
                    String plainCreds = "client:secret";
                    byte[] plainCredsBytes = plainCreds.getBytes();
                    String base64Creds = Base64.getEncoder().encodeToString(plainCredsBytes);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    headers.add("token", authHeader);
                    headers.add("Authorization", "Basic " + base64Creds);

                    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
                    map.add("token",authHeader);

                    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

                    restTemplate.exchange("http://localhost:8080/oauth2/introspect", HttpMethod.POST, entity, Map.class);

                } catch (Exception e) {
                    System.out.println("invalid access...!");
                    throw new RuntimeException("un authorized access to application");
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}
