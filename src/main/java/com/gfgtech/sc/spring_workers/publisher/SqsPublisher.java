package com.gfgtech.sc.spring_workers.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.stream.IntStream;

@Component
public class SqsPublisher {
    private final QueueMessagingTemplate queueMessagingTemplate;
    public static final Logger LOGGER = LoggerFactory.getLogger(SqsPublisher.class);

    public SqsPublisher(QueueMessagingTemplate queueMessagingTemplate) {
        this.queueMessagingTemplate = queueMessagingTemplate;
    }

    @PostConstruct
    public void send() {
        IntStream.range(1, 51)
                .mapToObj(idx -> String.format("spring-worker-%s", idx))
                .forEach(queueName -> {
                    queueMessagingTemplate.convertAndSend(queueName, "Hello");
                    LOGGER.info("queueName: {}", queueName);
                });
    }
}
