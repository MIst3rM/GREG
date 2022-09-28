package com.coen424.survey.surveyproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class SurveyprojectApplication {

	public static void main(String[] args) {

		SpringApplication.run(SurveyprojectApplication.class, args);

//		try (DynamoDbClient client = DynamoDbClient.builder()
//				.region(Region.US_EAST_1)
//				.endpointOverride(
//						URI.create("http://dynamodb-local:8000")
//				).build()) {
//			System.out.println("DynamoDB local started");
//		}
	}

}

