package com.coen424.survey.surveyproject.api;

import com.coen424.survey.surveyproject.models.LambdaFunction;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.*;
import software.amazon.awssdk.services.lambda.waiters.LambdaWaiter;

public class ApiHandler {
    private static LambdaClient getLambdaClient(){
        return LambdaClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }

    public String createLambdaFunction(LambdaFunction lf) {

        String lambdaRole = "arn:aws:iam::834502670743:role/service-role/Fetch_Survey_Responses-role-yjvqbou6";

        LambdaClient awsLambda = getLambdaClient();

        try {
            LambdaWaiter waiter = awsLambda.waiter();
            SdkBytes fileToUpload = SdkBytes.fromByteArray(lf.getFuncCode());

            FunctionCode code = FunctionCode.builder()
                    .zipFile(fileToUpload)
                    .build();

            CreateFunctionRequest functionRequest = CreateFunctionRequest.builder()
                    .functionName(lf.getFuncName().strip())
                    .description("Created by the Lambda Java API")
                    .code(code)
                    .handler(lf.getFuncName().strip() + "." + lf.getFuncHandler().strip())
                    .runtime("python3.9")
                    .role(lambdaRole)
                    .build();

            // Create a Lambda function using a waiter.
            CreateFunctionResponse functionResponse = awsLambda.createFunction(functionRequest);
            GetFunctionRequest getFunctionRequest = GetFunctionRequest.builder()
                    .functionName(lf.getFuncName().strip())
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
