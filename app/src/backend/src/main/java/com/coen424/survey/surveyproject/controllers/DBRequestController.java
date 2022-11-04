package com.coen424.survey.surveyproject.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.coen424.survey.surveyproject.database.Request.getUser;

@RestController
public class DBRequestController {

    @RequestMapping(value = "getUser", method = RequestMethod.GET)
    public String getUserName(String user_id){
       return getUser(user_id);
    }
}
