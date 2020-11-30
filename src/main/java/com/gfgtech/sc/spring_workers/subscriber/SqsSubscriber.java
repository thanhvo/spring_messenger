package com.gfgtech.sc.spring_workers.subscriber;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import org.reactivestreams.Subscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.BaseSubscriber;

@Component
public class SqsSubscriber<T> extends BaseSubscriber<T> {
    public static final Logger LOGGER = LoggerFactory.getLogger(SqsSubscriber.class);
    private final AmazonSQSAsync sqsAsyncClient;
    private String queueUrl;
    private boolean done;

    public SqsSubscriber(AmazonSQSAsync sqsAsyncClient) {
        this.sqsAsyncClient = sqsAsyncClient;
        this.done = false;
    }

    public boolean isDone() {
        return done;
    }

    public void setQueueUrl(String queueUrl)
    {
        this.queueUrl = queueUrl;
    }

    public void hookOnSubscribe(Subscription subscription) {
        System.out.println("Subscribed");
        request(1);
    }

    public void hookOnNext(T item) {
        ReceiveMessageResult result = (ReceiveMessageResult)item;
        for (Message message : result.getMessages()) {
            LOGGER.info("message body: " + message.getBody());
            DeleteMessageRequest request = new DeleteMessageRequest()
                    .withQueueUrl(queueUrl)
                    .withReceiptHandle(message.getReceiptHandle());
            sqsAsyncClient.deleteMessageAsync(request);
            LOGGER.info("deleted message with handle " + message.getReceiptHandle());
        }
        request(1);
    }

    public void hookOnComplete() {
        //request(1);
        done = true;
        requestUnbounded();
    }
}
