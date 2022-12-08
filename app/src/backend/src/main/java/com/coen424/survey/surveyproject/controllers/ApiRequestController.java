package com.coen424.survey.surveyproject.controllers;

import com.coen424.survey.surveyproject.api.ApiHandler;
import com.coen424.survey.surveyproject.models.LambdaFunction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;
import software.amazon.awssdk.services.lambda.model.LambdaException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import  org.springframework.web.bind.annotation.RequestBody;

import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import static java.time.temporal.ChronoUnit.SECONDS;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class ApiRequestController {

    private static LambdaClient getLambdaClient(){
        return LambdaClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }

    private static S3Client getS3Client(){
        return S3Client.builder()
                .region(Region.US_EAST_1)
                .build();
    }

    @PostMapping(value = "/api/v1/createLambdaFunction")
    public String createLambdaFunc(@AuthenticationPrincipal Jwt jwt, @RequestBody String body) {
        ApiHandler apiHandler = new ApiHandler();
        String msg = "";
        try {
            LambdaFunction lambdaFunction = new ObjectMapper().readValue(body, LambdaFunction.class);
            msg = apiHandler.createLambdaFunction(lambdaFunction);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "An error occurred. Please try again.";
        }
        return msg;
    }
    @GetMapping(value = "/api/v1/getResultsForSharedForm")
    public String getResultsForSharedForm(@RequestHeader(value = "x-api-key") String apiKey, String form_id) {

        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create("https://olwzb7pjjb.execute-api.us-east-1.amazonaws.com/test/surveyresults?form_id=" + form_id))
                .GET()
                .headers("x-api-key", apiKey, "Content-Type", "application/json")
                .timeout(Duration.of(10, SECONDS))
                .build();

        CompletableFuture<HttpResponse<String>> response = HttpClient.newBuilder()
                .build()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString());

        return response.join().body();
    }
    @GetMapping(value = "/api/v1/generateAPIKey")
    public String generateAPIKey(@RequestParam String name) {
        LambdaClient client = getLambdaClient();
        InvokeResponse res;

        try{
            String payload = "{\"name\": \"" + name + "\"}";
            SdkBytes payloadBytes = SdkBytes.fromUtf8String(payload);
            InvokeRequest request = InvokeRequest.builder()
                    .functionName("generateAPIKey")
                    .payload(payloadBytes)
                    .build();
            res = client.invoke(request);
            JSONParser parser = new JSONParser(res.payload().asUtf8String());
            try {
                JSONObject json = new JSONObject(parser.parseObject());
                return json.getString("body").replaceAll("\"", "").replaceAll("\\\\", "");
            } catch (ParseException | JSONException e) {
                throw new RuntimeException(e);
            }
        }catch(LambdaException e){
            return e.getMessage();
        }
    }

    @PostMapping(value = "/api/v1/uploadVideo")
    public String uploadVideo(@AuthenticationPrincipal Jwt jwt, @RequestPart(value = "file") MultipartFile file) {

        S3Client s3 = getS3Client();

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket("surveyadvisorassets")
                .key(file.getOriginalFilename())
                .acl("public-read")
                .build();

        try{
            s3.putObject(objectRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));
            return s3.utilities().getUrl(builder -> builder.bucket("surveyadvisorassets").key(file.getOriginalFilename())).toString();
        }catch(Exception e) {
            System.err.println(e.getMessage());
            return "An error occurred. Please try again later.";
        }
    }
}
