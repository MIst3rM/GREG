package com.coen424.survey.surveyproject.database;


import com.coen424.survey.surveyproject.models.Questions;
import com.coen424.survey.surveyproject.models.Users;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import java.util.Iterator;


public class Request {

    private static DynamoDbEnhancedClient getEnhancedClient(){
        Region region = Region.US_EAST_1;
        DynamoDbClient ddb = DynamoDbClient.builder()
                .region(region)
                .build();

        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(ddb)
                .build();
        return enhancedClient;
    }

    public static String getUser(String user_id){

        DynamoDbEnhancedClient enhancedClient = getEnhancedClient();
        Users result = null;

        try {
            DynamoDbTable<Users> table = enhancedClient.table("Users", TableSchema.fromBean(Users.class));
            Key key = Key.builder()
                    .partitionValue(user_id)
                    .build();

            result = table.getItem(
                    (GetItemEnhancedRequest.Builder requestBuilder) -> requestBuilder.key(key));

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return result.getName();
    }

    public static String getQuestion(String question_id){

        DynamoDbEnhancedClient enhancedClient = getEnhancedClient();
        Questions question = null;

        try {
            DynamoDbTable<Questions> table = enhancedClient.table("Questions", TableSchema.fromBean(Questions.class));
            Key key = Key.builder()
                    .partitionValue(question_id)//.partitionValue(form_id)
                    .build();

            question = table.getItem(
                    (GetItemEnhancedRequest.Builder requestBuilder) -> requestBuilder.key(key));

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return question.getQuestion();
    }

    public static void setQuestion(String qs, String question_id ){

        DynamoDbEnhancedClient enhancedClient = getEnhancedClient();

        try {
            // Create a DynamoDbTable object
            DynamoDbTable<Questions> mappedTable = enhancedClient.table("Questions", TableSchema.fromBean(Questions.class));
            Questions question = new Questions();
            question.setQuestion(qs);
            question.setQuestion_id(question_id);

            PutItemEnhancedRequest enReq = PutItemEnhancedRequest.builder(Questions.class)
                    .item(question)
                    .build();

            mappedTable.putItem(enReq);

        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void deleteQuestion(final String question_id) {

        DynamoDbEnhancedClient enhancedClient = getEnhancedClient();

        try{
            DynamoDbTable<Questions> table = enhancedClient.table("Questions", TableSchema.fromBean(Questions.class));

            Key key = Key.builder()
                    .partitionValue(question_id)
                    .build();

            DeleteItemEnhancedRequest deleteRequest = DeleteItemEnhancedRequest
                    .builder()
                    .key(key)
                    .build();

            table.deleteItem(deleteRequest);
        } catch (Exception e) {
            e.getStackTrace();
        }

    }

    public static String updateQuestion(String question_id, String question) {

        DynamoDbEnhancedClient enhancedClient = getEnhancedClient();
        Questions qs = null;

        try {

            DynamoDbTable<Questions> mappedTable = enhancedClient.table("Questions", TableSchema.fromBean(Questions.class));
            Key key = Key.builder()
                    .partitionValue(question_id)
                    .build();

            // Get the question by using the key and update the question.
            qs = mappedTable.getItem(r->r.key(key));
            qs.setQuestion(question);

            UpdateItemEnhancedRequest updateRequest = UpdateItemEnhancedRequest
                    .builder(Questions.class)
                    .item(qs)
                    .build();

            mappedTable.updateItem(updateRequest);

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return qs.getQuestion();
    }

    public static String queryTable(String question_id) {

        DynamoDbEnhancedClient enhancedClient = getEnhancedClient();
        String result="";

        try{
            DynamoDbTable<Questions> mappedTable = enhancedClient.table("Questions", TableSchema.fromBean(Questions.class));
            QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder()
                    .partitionValue(question_id)
                    .build());

            // Get items in the table and write out the ID value.
            Iterator<Questions> results = mappedTable.query(queryConditional).items().iterator();

            while (results.hasNext()) {
                Questions qs = results.next();
                result = qs.getQuestion_id();
                System.out.println("The question id is "+result);
            }


        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return result;
    }


    public static void getAllQuestions() {

        DynamoDbEnhancedClient enhancedClient = getEnhancedClient();
        //Questions ques = null;

        try{
            DynamoDbTable<Questions> table = enhancedClient.table("Questions", TableSchema.fromBean(Questions.class));
            Iterator<Questions> questions = table.scan().items().iterator();
            while (questions.hasNext()) {
                Questions question = questions.next();
                System.out.println("The question id is "+question.getQuestion_id());
                System.out.println("The question is " +question.getQuestion());
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }

}
