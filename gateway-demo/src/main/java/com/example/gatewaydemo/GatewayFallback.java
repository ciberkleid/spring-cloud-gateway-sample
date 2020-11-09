package com.example.gatewaydemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class GatewayFallback {

    @GetMapping("/id")
    public String getDefaultUuid() {
        return "00000000-0000-0000-0000-000000000000";
    }

}