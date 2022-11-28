package com.coen424.survey.surveyproject.controllers;

import com.coen424.survey.surveyproject.database.Request;
import com.coen424.survey.surveyproject.models.Form_Submissions;
import com.coen424.survey.surveyproject.models.Forms;
import com.coen424.survey.surveyproject.models.SharedResults;
import com.coen424.survey.surveyproject.utils.RequestOptions;
import com.coen424.survey.surveyproject.utils.Table;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class DBRequestController {
    @PostMapping(value = "/api/v1/getForm")
    public Forms getForm(@AuthenticationPrincipal Jwt jwt, String form_id) {
        System.out.println(form_id);
        RequestOptions options = new RequestOptions.RequestBuilder()
                .setPartition_key(jwt.getClaimAsString("sub")) // user_id
                .setSort_key(form_id)
                .setTable_type(Table.FORMS)
                .build();
        Request<Forms> request = new Request<>();
        return request.get(options);
    }

    @GetMapping(value = "/api/v1/getAllForms")
    public List<Forms> getAllForms(@AuthenticationPrincipal Jwt jwt) {
        RequestOptions options = new RequestOptions.RequestBuilder()
                .setPartition_key(jwt.getClaimAsString("sub")) // user_id
                .setTable_type(Table.FORMS)
                .build();
        Request<Forms> request = new Request<>();
        return request.getAll(options);
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

    @GetMapping(value = "/api/v1/getAllSharedForms")
    public List<Forms> getAllSharedForms() {
        // First get a list of surveys that are shared publicly
        RequestOptions options = new RequestOptions.RequestBuilder()
                .setTable_type(Table.SHARED_RESULTS)
                .build();
        Request<SharedResults> request = new Request<>();
        List<SharedResults> res = request.scan(options);

        // Now get the forms that are shared publicly
        List<Forms> forms = new ArrayList<>();
        res.forEach(form -> {
            RequestOptions options2 = new RequestOptions.RequestBuilder()
                    .setPartition_key(form.getUser_id())
                    .setSort_key(form.getForm_id())
                    .setTable_type(Table.FORMS)
                    .build();
            Request<Forms> request2 = new Request<>();
            forms.add(request2.get(options2));
        });
        return forms;
    }

    @GetMapping(value = "/api/v1/getResultsForSharedForm")
    public Map<String, Map<String, List<String>>> getResultsForShared(String form_id) {
        //First find out if the form results are shared
        RequestOptions options = new RequestOptions.RequestBuilder()
                .setSort_key(form_id)
                .setTable_type(Table.SHARED_RESULTS)
                .build();
        Request<SharedResults> request = new Request<>();
        List<String> form_ids = new ArrayList<>();
        request.scan(options).forEach(res -> {
            form_ids.add(res.getForm_id());
        });

        //If the form results are shared, get all the results
        if(form_ids.size() > 0) {

            //Then get all the submissions for each form
            RequestOptions options2 = new RequestOptions.RequestBuilder()
                        .setPartition_key(form_id)
                        .setTable_type(Table.FORM_SUBMISSIONS)
                        .build();
            Request<Form_Submissions> request2 = new Request<>();
            List<Form_Submissions> submissions = new ArrayList<>(request2.getAll(options2));

            //Then convert the submissions to a list of maps
            Map<String, Map<String, List<String>>> results = new HashMap<>();
            submissions.forEach(submission -> {
                if (results.containsKey(submission.getForm_id())) {
                    Map<String, List<String>> res = results.get(submission.getForm_id());
                    submission.getAnswers().forEach((k, v) -> {
                        if (res.containsKey(k)) {
                            res.get(k).add(v);
                        } else {
                            List<String> list = new ArrayList<>();
                            list.add(v);
                            res.put(k, list);
                        }
                    });
                } else {
                    Map<String, List<String>> res = new HashMap<>();
                    submission.getAnswers().forEach((k, v) -> {
                        List<String> list = new ArrayList<>();
                        list.add(v);
                        res.put(k, list);
                    });
                    results.put(submission.getForm_id(), res);
                }
            });
            return results;
        }else{
            return null;
        }
    }
}