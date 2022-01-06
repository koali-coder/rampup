package com.example.demo.controller;

import com.example.demo.entity.Demo;
import com.example.demo.model.RestData;
import com.example.demo.service.IDemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhouyw
 */
@Slf4j
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    public IDemoService demoService;

    @GetMapping("/hello")
    public String hello(@RequestParam("param") String param) {
        log.info("demo controller hello");
        return "hello world " + param;
    }

    @GetMapping("/list")
    public RestData<List<Demo>> list() {
        log.info("demo controller list");
        return RestData.success(demoService.list());
    }

}
