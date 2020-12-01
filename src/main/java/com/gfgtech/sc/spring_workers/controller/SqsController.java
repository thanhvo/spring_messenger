package com.gfgtech.sc.spring_workers.controller;

import com.gfgtech.sc.spring_workers.domain.SqsMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

// For creating the REST controllers.
@RestController
// Used to map incoming web requests onto the handler methods in the controller.
@RequestMapping(value = "/sqs")
public class SqsController {
    private static final String QUEUE = "spring_workers";

    public static final Logger LOGGER = LoggerFactory.getLogger(SqsController.class);

    // QueueMessagingTemplate initializes the messaging template by configuring the destination resolver as well as the message converter.
    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    // HTTP POST url - http://localhost:9091/sqs/send
    @PostMapping(value = "/send")
    // @ResponseStatus annotation marks the method with the status-code and the reason message that should be returned.
    @ResponseStatus(code = HttpStatus.CREATED)
    public void sendMessageToSqs(@RequestBody @Valid final SqsMessage message)
    {
        LOGGER.info("Sending the message to the Amazon sqs.");
        queueMessagingTemplate.convertAndSend(QUEUE, message);
        LOGGER.info("Message sent successfully to the Amazon sqs.");
    }
}
