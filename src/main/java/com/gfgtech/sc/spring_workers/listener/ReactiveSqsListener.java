package com.gfgtech.sc.spring_workers.listener;

import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import com.amazonaws.services.sqs.model.*;
import com.gfgtech.sc.spring_workers.domain.SqsMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
public class ReactiveSqsListener {
    public static final Logger LOGGER = LoggerFactory.getLogger(ReactiveSqsListener.class);
    private final AmazonSQSAsync sqsAsyncClient;
    private final String queueUrl;
    private final String queueName = "spring_workers";

    public ReactiveSqsListener(AmazonSQSAsync sqsAsyncClient) {
        this.sqsAsyncClient = sqsAsyncClient;
        try {
            this.queueUrl = this.sqsAsyncClient.getQueueUrl(queueName).getQueueUrl();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void continuousListener() {
        CompletableFuture<ReceiveMessageResult> result = CompletableFuture.supplyAsync(() -> {
            ReceiveMessageResult message = null;
            try {
                message = sqsAsyncClient.receiveMessageAsync(queueUrl).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return message;
        });
        Mono<ReceiveMessageResult> receiveMessageResponseMono = Mono.fromFuture(result);
        receiveMessageResponseMono
                .repeat()
                .retry()
                .subscribe(messages -> {
                    if (!messages.getMessages().isEmpty()) {
                        for (Message message : messages.getMessages()) {
                            LOGGER.info("message body: " + message.getBody());
                            sqsAsyncClient.deleteMessageAsync(queueUrl, message.getReceiptHandle());
                        }
                    }
                });
    }
}
