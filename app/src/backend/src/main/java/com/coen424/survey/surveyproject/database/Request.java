package com.coen424.survey.surveyproject.database;

import com.coen424.survey.surveyproject.utils.RequestOptions;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
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

            ScanEnhancedRequest scanEnhancedRequest;
                scanEnhancedRequest = ScanEnhancedRequest.builder()
                    .filterExpression(options.getExpression())
                        .attributesToProject(options.getAttributesToProject())
                        .build();

            return table.scan(scanEnhancedRequest).items().stream().toList();

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public void update(RequestOptions options, T item){
        DynamoDbEnhancedClient enhancedClient = getEnhancedClient();
        try {
            DynamoDbTable<T> table = enhancedClient.table(options.getTable_type().getTableName(),  TableSchema.fromBean((Class<T>) options.getTable_type().getTableClass()));
            table.updateItem(item);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
    }
}