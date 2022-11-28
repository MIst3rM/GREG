package com.coen424.survey.surveyproject.database;

import com.coen424.survey.surveyproject.utils.RequestOptions;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.*;

public class Request<T> {
    private static DynamoDbEnhancedClient getEnhancedClient() {
        Region region = Region.US_EAST_1;
        DynamoDbClient ddb = DynamoDbClient.builder()
                .region(region)
                .build();

        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(ddb)
                .build();
    }

    @SuppressWarnings("unchecked")
    public T get(RequestOptions options){
        DynamoDbEnhancedClient enhancedClient = getEnhancedClient();
        T result = null;

        try {
            DynamoDbTable<T> table = enhancedClient.table(options.getTable_type().getTableName(),  TableSchema.fromBean((Class<T>) options.getTable_type().getTableClass()));
            Key key = Key.builder()
                    .partitionValue(options.getPartition_key())
                    .sortValue(options.getSort_key())
                    .build();

            result = table.getItem(
                    (GetItemEnhancedRequest.Builder requestBuilder) -> requestBuilder.key(key));

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<T> getAll(RequestOptions options){
        DynamoDbEnhancedClient enhancedClient = getEnhancedClient();
        List<T> result = new ArrayList<>();

        try {
            DynamoDbTable<T> table = enhancedClient.table(options.getTable_type().getTableName(),  TableSchema.fromBean((Class<T>) options.getTable_type().getTableClass()));
            Key key = Key.builder()
                    .partitionValue(options.getPartition_key())
                    .build();

            QueryConditional queryConditional = QueryConditional
                    .keyEqualTo(key);

            for(Page<T> page: table.query(r -> r.queryConditional(queryConditional)).stream().toList()){
                result.addAll(page.items());
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public void put(RequestOptions options, T item){
        DynamoDbEnhancedClient enhancedClient = getEnhancedClient();
        try {
            DynamoDbTable<T> table = enhancedClient.table(options.getTable_type().getTableName(),  TableSchema.fromBean((Class<T>) options.getTable_type().getTableClass()));
            table.putItem(item);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void delete(RequestOptions options){
        DynamoDbEnhancedClient enhancedClient = getEnhancedClient();
        try {
            DynamoDbTable<T> table = enhancedClient.table(options.getTable_type().getTableName(),  TableSchema.fromBean((Class<T>) options.getTable_type().getTableClass()));
            Key key = Key.builder()
                    .partitionValue(options.getPartition_key())
                    .sortValue(options.getSort_key())
                    .build();
            table.deleteItem(key);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
    }
    @SuppressWarnings("unchecked")
    public List<T> scan(RequestOptions options) {
        DynamoDbEnhancedClient enhancedClient = getEnhancedClient();
        try{
            DynamoDbTable<T> table = enhancedClient.table(options.getTable_type().getTableName(),  TableSchema.fromBean((Class<T>) options.getTable_type().getTableClass()));

            AttributeValue att = AttributeValue.builder()
                    .bool(true)
                    .build();

            AttributeValue att2 = null;
            if(options.getSort_key() != null){
                att2 = AttributeValue.builder()
                        .s(options.getSort_key())
                        .build();
            }

            Map<String, AttributeValue> expressionValues = new HashMap<>();
            expressionValues.put(":value", att);

            if(att2 != null){
                expressionValues.put(":value2", att2);
            }

            Expression expression;
            if(att2 != null) {
                expression= Expression.builder()
                        .expression("isShared = :value and form_id = :value2")
                        .expressionValues(expressionValues)
                        .build();
            }else{
                expression= Expression.builder()
                        .expression("isShared = :value")
                        .expressionValues(expressionValues)
                        .build();
            }

            ScanEnhancedRequest scanEnhancedRequest;
            if(att2 != null) {
                scanEnhancedRequest = ScanEnhancedRequest.builder()
                    .filterExpression(expression)
                        .attributesToProject("form_id")
                        .build();
            }else{
                scanEnhancedRequest = ScanEnhancedRequest.builder()
                        .filterExpression(expression)
                        .build();
            }

            return table.scan(scanEnhancedRequest).items().stream().toList();

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
}