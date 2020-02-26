package com.learn.spring.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String getHello(Principal principal) {
        return "hello" + principal;
    }
}
