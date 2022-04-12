package com.notayessir.bootstrap.controller;

import com.notayessir.bootstrap.result.Result;
import com.notayessir.processor.configure.AppConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class IndexController {

    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    AppConfiguration configuration;

    /**
     * 获取当前应用配置
     * @return  应用配置
     */
    @RequestMapping("/configuration")
    public Result<?> index(){
        return new Result<>("1000", "success", configuration);
    }

}
