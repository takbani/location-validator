package com.location.validator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ApiConnectorService {

    @Autowired
    WebClient.Builder webClientBuilder;

    public <T> T getResponseFromApiForGetRequest(String url, Class<T> returnType){
        return  webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(returnType)
                .block();

    }
}
