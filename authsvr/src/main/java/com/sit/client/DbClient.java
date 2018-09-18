package com.sit.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by zhenglin on 2018/9/13.
 */
@FeignClient("dbsvr")
public interface DbClient {

    @GetMapping("/user/get")
    Map<String,Object> getUserInfo(@RequestParam("account")String account);

}
