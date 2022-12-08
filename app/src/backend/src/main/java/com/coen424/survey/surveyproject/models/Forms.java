package com.coen424.survey.surveyproject.models;

import com.fasterxml.jackson.annotation.JsonFilter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.List;
import java.util.Map;
@JsonFilter("formsFilter")
@DynamoDbBean
public class Forms {
    private String user_id;

    private String form_id;

    private String title;

    private String description;

    private Map<String, List<String>> questions;

    private String status;

    private boolean sharedPublicly;

    private String created_at;

    private String last_modified_at;

    private String published_at;

    private String closing_at;

    private String surveyJson;

    private int submissions_count;

    @DynamoDbPartitionKey
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    @DynamoDbSortKey
    public String getForm_id() {
        return form_id;
    }

    public void setForm_id(String form_id) {
        this.form_id = form_id;
    }

    public Map<String, List<String>> getQuestions() {
        return questions;
    }

    public void setQuestions(Map<String, List<String>> questions) {
        this.questions = questions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getLast_modified_at() {
        return last_modified_at;
    }

    public void setLast_modified_at(String last_modified_at) {
        this.last_modified_at = last_modified_at;
    }

    public String getSurveyJson() {
        return surveyJson;
    }

    public void setSurveyJson(String surveyJson) {
        this.surveyJson = surveyJson;
    }

    public boolean getSharedPublicly() {
        return sharedPublicly;
    }

    public void setSharedPublicly(boolean sharedPublicly) {
        this.sharedPublicly = sharedPublicly;
    }

    public int getSubmissions_count() {
        return submissions_count;
    }

    public void setSubmissions_count(int submissions_count) {
        this.submissions_count = submissions_count;
    }

    public String getPublished_at() {
        return published_at;
    }

    public void setPublished_at(String published_at) {
        this.published_at = published_at;
    }

    public String getClosing_at() {
        return closing_at;
    }

    public void setClosing_at(String closing_at) {
        this.closing_at = closing_at;
    }

    @Override
    public String toString() {
        return "Forms{" +
                "user_id='" + user_id + '\'' +
                ", form_id='" + form_id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", questions=" + questions +
                ", status='" + status + '\'' +
                ", sharedPublicly=" + sharedPublicly +
                ", created_at='" + created_at + '\'' +
                ", last_modified_at='" + last_modified_at + '\'' +
                ", published_at='" + published_at + '\'' +
                ", closing_at='" + closing_at + '\'' +
                ", surveyJson='" + surveyJson + '\'' +
                ", submissions_count=" + submissions_count +
                '}';
    }
}
