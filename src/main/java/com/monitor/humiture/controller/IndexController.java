package com.monitor.humiture.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {


    @Value("${project.version}")
    private String version;

    @RequestMapping("/")
    public String index(){
        return "humiture "+ version;
    }


}
