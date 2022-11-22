package com.coen424.survey.surveyproject.controllers;

import com.coen424.survey.surveyproject.api.ApiHandler;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class ApiRequestController {

    @PostMapping(value = "/api/v1/createLambdaFunction")
    public String createLambdaFunc(@AuthenticationPrincipal Jwt jwt,
                                       String funcName,
                                       byte[] funcCode,
                                       String funcHandler) {
        System.out.println(Arrays.toString(funcCode));
//        ApiHandler apiHandler = new ApiHandler();
//
//        return apiHandler.createLambdaFunction(funcName, funcCode, funcHandler);
        return "Function created successfully";
    }
}
