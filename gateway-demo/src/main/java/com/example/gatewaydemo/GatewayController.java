package com.example.gatewaydemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.cloud.gateway.webflux.ProxyExchange;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class GatewayController {

    @GetMapping("/fallback/id")
    public String getDefaultUuid() {
        return "00000000-0000-0000-0000-000000000000";
    }

    // Inspired by https://github.com/spencergibb/spring-cloud-gateway-recipies-2022
    @GetMapping(path = "/scattergather/uuids")
    Mono<ResponseEntity<List<String>>> scatterGather(ProxyExchange<String> proxy) {
        Mono<ResponseEntity<String>> responseMono1 = proxy.uri("https://httpbin.org/uuid").get();
        Mono<ResponseEntity<String>> responseMono2 = proxy.uri("https://httpbin.org/uuid").get();
        return Mono.zip(responseMono1, responseMono2).map(responses -> {
            ResponseEntity<String> response1 = responses.getT1();
            ResponseEntity<String> response2 = responses.getT2();
            List<String> body = null;
            try {
                var mapper = new ObjectMapper();
                body = List.of(
                        mapper.readValue(response1.getBody(), ID.class).uuid(),
                        mapper.readValue(response2.getBody(), ID.class).uuid());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            HttpHeaders httpHeaders = HttpHeaders.writableHttpHeaders(response1.getHeaders());
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return ResponseEntity.status(response1.getStatusCode()).headers(httpHeaders).body(body);
        });
    }

    record ID (String uuid) {}

}