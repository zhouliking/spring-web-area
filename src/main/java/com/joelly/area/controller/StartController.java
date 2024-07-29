package com.joelly.area.controller;

import com.joelly.area.controller.base.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StartController extends BaseController {

    @GetMapping("/hello")
    public String hello() {
        return "OK";
    }

}