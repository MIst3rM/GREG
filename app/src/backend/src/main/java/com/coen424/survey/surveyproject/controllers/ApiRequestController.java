package com.coen424.survey.surveyproject.controllers;

import com.coen424.survey.surveyproject.api.ApiHandler;
import com.coen424.survey.surveyproject.models.LambdaFunction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class ApiRequestController {
    @PostMapping(value = "/api/v1/createLambdaFunction")
    public String createLambdaFunc(@AuthenticationPrincipal Jwt jwt, @RequestBody String body) {
        ApiHandler apiHandler = new ApiHandler();
        String msg = "";
        try {
            LambdaFunction lambdaFunction = new ObjectMapper().readValue(body, LambdaFunction.class);
            msg = apiHandler.createLambdaFunction(lambdaFunction);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "An error occurred. Please try again.";
        }
        return msg;
    }
}
