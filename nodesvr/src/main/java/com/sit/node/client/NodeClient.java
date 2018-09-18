package com.sit.node.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by zhenglin on 2018/9/6.
 */
@FeignClient("nodesvr")
public interface  NodeClient {

    @GetMapping("/dc")
    String consumer();
}
