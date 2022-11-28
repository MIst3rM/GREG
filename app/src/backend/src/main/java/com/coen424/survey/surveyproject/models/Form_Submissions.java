package com.coen424.survey.surveyproject.models;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.List;
import java.util.Map;

@DynamoDbBean
public class Form_Submissions {

    private String form_id;
    private String respondent_id;
    private String user_id;
    private Map<String, String> answers;

    @DynamoDbPartitionKey
    public String getForm_id() {
        return form_id;
    }

    public void setForm_id(String form_id) {
        this.form_id = form_id;
    }

    @DynamoDbSortKey
    public String getRespondent_id() {
        return respondent_id;
    }

    public void setRespondent_id(String respondent_id) {
        this.respondent_id = respondent_id;
    }

    @DynamoDbSecondarySortKey(indexNames = "user_id-index")
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Map<String, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, String> answers) {
        this.answers = answers;
    }
}
