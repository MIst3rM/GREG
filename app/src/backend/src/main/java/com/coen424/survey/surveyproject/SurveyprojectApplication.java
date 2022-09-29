package com.coen424.survey.surveyproject;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class SurveyprojectApplication {

	public static void main(String[] args) {

		SpringApplication.run(SurveyprojectApplication.class, args);
		DynamoDBConfig dynamoDBConfig = new DynamoDBConfig();
		DynamoDBMapper mapper = dynamoDBConfig.mapper();
	}

}

