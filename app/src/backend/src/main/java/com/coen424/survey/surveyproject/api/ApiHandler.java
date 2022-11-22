package com.coen424.survey.surveyproject.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.*;
import software.amazon.awssdk.services.iam.waiters.IamWaiter;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.*;
import software.amazon.awssdk.services.iam.model.GetPolicyRequest;
import software.amazon.awssdk.services.iam.model.GetPolicyResponse;
import software.amazon.awssdk.services.iam.model.CreatePolicyRequest;
import software.amazon.awssdk.services.lambda.waiters.LambdaWaiter;

public class ApiHandler {
    @Value("${amazon.aws.lambda.role}")
    private String lambdaRole;
    private static LambdaClient getLambdaClient(){
        return LambdaClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }

    public String createLambdaFunction(String functionName, byte[] file, String handler) {

        LambdaClient awsLambda = getLambdaClient();

        try {
            LambdaWaiter waiter = awsLambda.waiter();
            SdkBytes fileToUpload = SdkBytes.fromByteArray(file);

            FunctionCode code = FunctionCode.builder()
                    .zipFile(fileToUpload)
                    .build();

            CreateFunctionRequest functionRequest = CreateFunctionRequest.builder()
                    .functionName(functionName)
                    .description("Created by the Lambda Java API")
                    .code(code)
                    .handler(handler)
                    .runtime("Python 3.9")
                    .role(lambdaRole)
                    .build();

            // Create a Lambda function using a waiter.
            CreateFunctionResponse functionResponse = awsLambda.createFunction(functionRequest);
            GetFunctionRequest getFunctionRequest = GetFunctionRequest.builder()
                    .functionName(functionName)
                    .build();
            WaiterResponse<GetFunctionResponse> waiterResponse = waiter.waitUntilFunctionExists(getFunctionRequest);
            waiterResponse.matched().response().ifPresent(System.out::println);
            System.out.println("The function ARN is " + functionResponse.functionArn());

        } catch(LambdaException e) {
            System.err.println(e.getMessage());
            return "Error creating Lambda function";
        }
        return "Function created successfully";
    }
}
