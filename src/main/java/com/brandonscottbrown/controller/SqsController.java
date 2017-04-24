package com.brandonscottbrown.controller;

import com.brandonscottbrown.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sqs")
public class SqsController {

    private static final Logger logger = LoggerFactory.getLogger(SqsController.class);
    private static final String NOTIFICATIONS_TEST_QUEUE = "notificationsTestQueue";

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @PostMapping(value = "/send")
    @ResponseStatus(HttpStatus.OK)
    public void sendMessageToQueue(@RequestBody Message message){
        logger.info("Sending message {} to SQS", message.toString());
        this.queueMessagingTemplate.convertAndSend(NOTIFICATIONS_TEST_QUEUE,message);
    }

    @SqsListener(NOTIFICATIONS_TEST_QUEUE)
    private void recieveMessageFromQueue(Message message){
        logger.info("Received message {} from queue", message.toString());
    }
}
