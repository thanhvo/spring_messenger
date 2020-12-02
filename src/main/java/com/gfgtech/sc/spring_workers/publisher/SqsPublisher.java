package com.gfgtech.sc.spring_workers.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.stream.IntStream;


//@Component
public class SqsPublisher {
    @Autowired
    private final QueueMessagingTemplate queueMessagingTemplate;
    private String queueName;
    public static final Logger LOGGER = LoggerFactory.getLogger(SqsPublisher.class);

    public SqsPublisher(QueueMessagingTemplate queueMessagingTemplate, String queueName) {
        this.queueMessagingTemplate = queueMessagingTemplate;
        this.queueName = queueName;
    }

    @PostConstruct
    public void send() {
        LOGGER.info("queueName: {}", queueName);
        queueMessagingTemplate.convertAndSend(queueName, "Hello");
    }
}
