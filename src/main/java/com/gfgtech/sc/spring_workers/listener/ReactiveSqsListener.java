package com.gfgtech.sc.spring_workers.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;


public class ReactiveSqsListener {

    public static final Logger LOGGER = LoggerFactory.getLogger(ReactiveSqsListener.class);
    private final SqsAsyncClient sqsAsyncClient;
    private final String queueUrl;

    public ReactiveSqsListener(SqsAsyncClient sqsAsyncClient, String queueName) {
        this.sqsAsyncClient = sqsAsyncClient;
        try {
            this.queueUrl = this.sqsAsyncClient.getQueueUrl(
                    GetQueueUrlRequest.builder().queueName(queueName).build()
            ).get().queueUrl();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void listen() {
        Mono<ReceiveMessageResponse> receiveMessageResponseMono = Mono.fromFuture(() ->
                sqsAsyncClient.receiveMessage(
                        ReceiveMessageRequest.builder()
                                .maxNumberOfMessages(5)
                                .queueUrl(queueUrl)
                                .waitTimeSeconds(10)
                                .visibilityTimeout(30)
                                .build()
                )
        );

        receiveMessageResponseMono
                .repeat()
                .retry()
                .map(ReceiveMessageResponse::messages)
                .map(Flux::fromIterable)
                .flatMap(messageFlux -> messageFlux)
                .subscribe(message -> {
                    LOGGER.info("message body: " + message.body());
                    sqsAsyncClient.deleteMessage(DeleteMessageRequest.builder().queueUrl(queueUrl).receiptHandle(message.receiptHandle()).build())
                            .thenAccept(deleteMessageResponse -> {
                                LOGGER.info("deleted message with handle " + message.receiptHandle());
                            });
                });
    }
}
