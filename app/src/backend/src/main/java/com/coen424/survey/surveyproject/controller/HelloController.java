package com.coen424.survey.surveyproject.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class HelloController {
        @RequestMapping(value = "hello", method = RequestMethod.GET)
        public String hello() {
            return "Hello World!!!";
        }
}
