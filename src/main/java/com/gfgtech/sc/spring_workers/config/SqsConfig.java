package com.gfgtech.sc.spring_workers.config;

import com.gfgtech.sc.spring_workers.listener.ReactiveSqsListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.util.ArrayList;
import java.util.List;

// Marker annotation that tells spring to generate bean definitions at runtime for the methods annotated with @Bean annotation.
@Configuration
public class SqsConfig {

    // Value is populated by the region code.
    @Value("${cloud.aws.region.static}")
    private String region;

    // Value is populated with the aws access key.
    @Value("${cloud.aws.credentials.access-key}")
    private String awsAccessKey;

    // Value is populated with the aws secret key
    @Value("${cloud.aws.credentials.secret-key}")
    private String awsSecretKey;

    private final int QUEUE_NUM = 50;

    // @Bean annotation tells that a method produces a bean that is to be managed by the spring container.
    @Bean
    public QueueMessagingTemplate queueMessagingTemplate() {
        return new QueueMessagingTemplate(amazonSQSAsync());
    }

    @Bean
    // @Primary annotation gives a higher preference to a bean (when there are multiple beans of the same type).
    @Primary
    // AmazonSQSAsync is an interface for accessing the SQS asynchronously.
    // Each asynchronous method will return a Java Future object representing the asynchronous operation.
    public AmazonSQSAsync amazonSQSAsync() {
        return AmazonSQSAsyncClientBuilder
                .standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
                .build();
    }

    @Bean
    public SqsAsyncClient amazonSQSAsyncClient() {
        return SqsAsyncClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(new AwsCredentials() {
                    @Override
                    public String accessKeyId() {
                        return awsAccessKey;
                    }

                    @Override
                    public String secretAccessKey() {
                        return awsSecretKey;
                    }
                }))
                .build();
    }

    @Bean
    public List<ReactiveSqsListener> listeners() {
        ArrayList<ReactiveSqsListener> listeners = new ArrayList<ReactiveSqsListener>();
        SqsAsyncClient sqsAsyncClient = SqsAsyncClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(new AwsCredentials() {
                    @Override
                    public String accessKeyId() {
                        return awsAccessKey;
                    }

                    @Override
                    public String secretAccessKey() {
                        return awsSecretKey;
                    }
                }))
                .build();
        for (int i = 1; i <= QUEUE_NUM; i++) {
            String queueName = "spring-worker-" + i;
            ReactiveSqsListener listener = new ReactiveSqsListener(sqsAsyncClient, queueName);
            listener.listen();
            listeners.add(listener);
        }
        return listeners;
    }

}