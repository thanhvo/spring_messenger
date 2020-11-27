package com.gfgtech.sc.spring_workers.listener;

import com.amazonaws.services.sqs.model.*;
import com.gfgtech.sc.spring_workers.subscriber.SqsSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
    public void continuousListener()  {
        Thread thread = new Thread(){
            public void run(){
                while (true) {
                    CompletableFuture<ReceiveMessageResult> result = CompletableFuture.supplyAsync(() -> {
                        ReceiveMessageResult message = null;
                        try {
                            ReceiveMessageRequest request = new ReceiveMessageRequest()
                                .withQueueUrl(queueUrl)
                                .withWaitTimeSeconds(10)
                                .withVisibilityTimeout(30)
                                .withMaxNumberOfMessages(10);
                                message = sqsAsyncClient.receiveMessageAsync(request).get();
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        return message;
                    });
                    Mono<ReceiveMessageResult> receiveMessageResponseMono = Mono.fromFuture(result);
                    receiveMessageResponseMono
                            .map(ReceiveMessageResult::getMessages)
                            .subscribe(messages -> {
                                        for (Message message : messages) {
                                            LOGGER.info("message body: " + message.getBody());
                                            DeleteMessageRequest request = new DeleteMessageRequest()
                                                    .withQueueUrl(queueUrl)
                                                    .withReceiptHandle(message.getReceiptHandle());
                                            sqsAsyncClient.deleteMessageAsync(request);
                                            LOGGER.info("deleted message with handle " + message.getReceiptHandle());
                                        }
                                    }
                            );
                }
            }
        };
        thread.start();
    }
}
