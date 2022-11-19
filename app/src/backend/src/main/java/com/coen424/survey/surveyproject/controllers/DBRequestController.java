package com.coen424.survey.surveyproject.controllers;

import com.coen424.survey.surveyproject.database.Request;
import com.coen424.survey.surveyproject.models.Forms;
import com.coen424.survey.surveyproject.utils.RequestOptions;
import com.coen424.survey.surveyproject.utils.Table;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
public class DBRequestController {
    @PostMapping(value = "/api/v1/getForm")
    public Forms getForm(@AuthenticationPrincipal Jwt jwt, String form_id) {
        RequestOptions options = new RequestOptions.RequestBuilder()
                .setPartition_key(jwt.getClaimAsString("sub")) // user_id
                .setSort_key(form_id)
                .setTable_type(Table.FORMS)
                .build();
        Request<Forms> request = new Request<>();
        return request.get(options);
    }

    @GetMapping(value = "/api/v1/getAllForms")
    public void getAllForms(@AuthenticationPrincipal Jwt jwt) {
        RequestOptions options = new RequestOptions.RequestBuilder()
                .setPartition_key(jwt.getClaimAsString("sub")) // user_id
                .setTable_type(Table.FORMS)
                .build();
        Request<Forms> request = new Request<>();
        System.out.println(request.getAll(options));
    }
    @PostMapping(value = "/api/v1/createForm")
    public String createForm(@AuthenticationPrincipal Jwt jwt, @RequestBody String form) {
        RequestOptions options = new RequestOptions.RequestBuilder()
                .setTable_type(Table.FORMS)
                .build();
        Request<Forms> request = new Request<>();
        try {
            Forms f = new ObjectMapper().readValue(form, Forms.class);
            f.setUser_id(jwt.getClaimAsString("sub"));
            request.put(options, f);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "An error occurred. Please try again.";
        }
        return "Form created successfully";
    }

    @PostMapping(value = "/api/v1/deleteForm")
    public String deleteForm(@AuthenticationPrincipal Jwt jwt, String form_id) {
        RequestOptions options = new RequestOptions.RequestBuilder()
                .setPartition_key(jwt.getClaimAsString("sub")) // user_id
                .setSort_key(form_id)
                .setTable_type(Table.FORMS)
                .build();
        Request<Forms> request = new Request<>();
        request.delete(options);
        return "Form deleted successfully";
    }

}