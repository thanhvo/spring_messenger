package com.gfgtech.sc.spring_workers.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

public class SqsPublisher {
    private final SqsAsyncClient sqsAsyncClient;
    private final String queueUrl;
    public static final Logger LOGGER = LoggerFactory.getLogger(SqsPublisher.class);

    public SqsPublisher(SqsAsyncClient sqsAsyncClient, String queueName) {
        this.sqsAsyncClient = sqsAsyncClient;
        try {
            this.queueUrl = this.sqsAsyncClient.getQueueUrl(
                    GetQueueUrlRequest.builder().queueName(queueName).build()
            ).get().queueUrl();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void send() {
        sqsAsyncClient.sendMessage(
                SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody("Hello")
                .build()
        );
    }
}
