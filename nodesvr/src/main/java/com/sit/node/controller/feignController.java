package com.sit.node.controller;

import com.sit.node.client.NodeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhenglin on 2018/9/6.
 */
@RestController
public class feignController {
    @Autowired
    NodeClient dcClient;

    @GetMapping("/consumer")
    public String dc() {
        return dcClient.consumer();
    }
}
