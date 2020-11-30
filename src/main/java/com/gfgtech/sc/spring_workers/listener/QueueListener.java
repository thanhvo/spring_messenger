package com.gfgtech.sc.spring_workers.listener;

import com.gfgtech.sc.spring_workers.domain.SqsMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Header;

import javax.validation.Valid;

//@Component
public class QueueListener {

    private static final String QUEUE = "spring_workers";
    public static final Logger LOGGER = LoggerFactory.getLogger(QueueListener.class);

    // @SqsListener listens to the message from the specified queue.
    // Here in this example we are printing the message on the console and the message will be deleted from the queue once it is successfully delivered.
    @SqsListener(value = QUEUE, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void getMessageFromSqs(@Valid SqsMessage message, @Header("MessageId") String messageId)
    {
        LOGGER.info("Received message= {} with messageId= {}", message, messageId);
        LOGGER.info("The content of the message: id = {}, message = {}", message.getId(), message.getMessage());
    }
}
