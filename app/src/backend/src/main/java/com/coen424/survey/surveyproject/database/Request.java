package com.coen424.survey.surveyproject.database;


import com.coen424.survey.surveyproject.models.Users;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;


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

}
