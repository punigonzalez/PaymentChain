package com.paymentchain.customer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

public class webClientConfig {
    @Bean
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }
}
