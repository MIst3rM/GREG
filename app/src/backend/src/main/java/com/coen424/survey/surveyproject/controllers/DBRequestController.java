package com.coen424.survey.surveyproject.controllers;

import com.coen424.survey.surveyproject.database.Request;
import com.coen424.survey.surveyproject.models.Forms;
import com.coen424.survey.surveyproject.utils.RequestOptions;
import com.coen424.survey.surveyproject.utils.Table;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class DBRequestController {
    @GetMapping(value = "/api/v1/getForm")
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
    public MappingJacksonValue getAllForms(@AuthenticationPrincipal Jwt jwt) {
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAll();

        FilterProvider filters = new SimpleFilterProvider().addFilter("formsFilter", filter);

        RequestOptions options = new RequestOptions.RequestBuilder()
                .setPartition_key(jwt.getClaimAsString("sub")) // user_id
                .setTable_type(Table.FORMS)
                .build();
        Request<Forms> request = new Request<>();
        List<Forms> surveys = request.getAll(options);
        MappingJacksonValue mapping = new MappingJacksonValue(surveys);
        mapping.setFilters(filters);
        return mapping;
    }

    @PostMapping(value = "/api/v1/createForm")
    public String createForm(@AuthenticationPrincipal Jwt jwt, @RequestBody String form) {
        System.out.println(form);
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

    @DeleteMapping(value = "/api/v1/deleteForm")
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

    @GetMapping(value = "/api/v1/getNumberOfSubmissions")
    public int getNumberOfSubmissions(@AuthenticationPrincipal Jwt jwt, String form_id) {
        RequestOptions options = new RequestOptions.RequestBuilder()
                .setPartition_key(jwt.getClaimAsString("sub")) // user_id
                .setSort_key(form_id)
                .setTable_type(Table.FORMS)
                .build();
        Request<Forms> request = new Request<>();
        Forms survey = request.get(options);
        return survey.getSubmissions_count();
    }
    @PutMapping(value = "/api/v1/setShareable")
    public String setShareable(@AuthenticationPrincipal Jwt jwt, @RequestBody String requestBody) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(requestBody);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        RequestOptions options = new RequestOptions.RequestBuilder()
                .setPartition_key(jwt.getClaimAsString("sub")) // user_id
                .setSort_key(jsonObject.optString("form_id"))
                .setTable_type(Table.FORMS)
                .build();
        Request<Forms> request = new Request<>();
        Forms survey = request.get(options);

        if(survey.getStatus().equalsIgnoreCase("expired")) {
            survey.setSharedPublicly(jsonObject.optBoolean("sharedPublicly"));
            request.update(options, survey);
            try {
                if(jsonObject.getBoolean("sharedPublicly")) {
                    return "Form shared successfully";
                } else {
                    return "Form unshared successfully";
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }else{
            return "Form is " + survey.getStatus().toLowerCase() +  ". It cannot be shared at this time.";
        }
    }

    @GetMapping(value = "/api/v1/getAllSharedForms")
    public MappingJacksonValue getAllSharedForms() {
        // First get a list of surveys that are shared publicly
        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":value", AttributeValue.builder().bool(true).build());

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("form_id", "title", "description", "submissions_count", "status");

        FilterProvider filters = new SimpleFilterProvider().addFilter("formsFilter", filter);

        RequestOptions options = new RequestOptions.RequestBuilder()
                .setTable_type(Table.FORMS)
                .setExpression(Expression.builder()
                        .expression("sharedPublicly = :value")
                        .expressionValues(expressionValues)
                        .build())
                .setAttributesToProject(new String[]{"form_id", "title", "description", "submissions_count"})
                .build();
        Request<Forms> request = new Request<>();
        List<Forms> surveys = request.scan(options);
        MappingJacksonValue mapping = new MappingJacksonValue(surveys);
        mapping.setFilters(filters);
        return mapping;
    }
}