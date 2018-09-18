package com.sit.node.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cloud.client.discovery.DiscoveryClient;

/**
 * Created by zhenglin on 2018/9/6.
 */
@RefreshScope
@RestController
public class TestController {
    @Autowired
    DiscoveryClient discoveryClient;
    @Value("${sck.user}")
    private String sckUser;

    @RequestMapping("/from")
    public String from() {
        return this.sckUser;
    }
    @GetMapping("/dc")
    public String dc() {
        String services = "Services: " + discoveryClient.getServices();
        System.out.println(services);
        return services;
    }
}
