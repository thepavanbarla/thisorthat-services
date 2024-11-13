package com.tot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AwsS3Config {

	@Value("${media.s3.bucket.region}")
	private String bucketRegion;
	
	@Value("${media.s3.user.access.key}")
	private String accessKey;
	
	@Value("${media.s3.user.secret.key}")
	private String secretKey;
	
	@Bean
    public AmazonS3 getAmazonS3Cient() {
        final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder
            .standard()
            .withRegion(Regions.fromName(bucketRegion))
            .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
            .build();
    }
	
}
