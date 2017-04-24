package com.brandonscottbrown.controller;

import com.brandonscottbrown.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/relay")
public class RelayController {

    private static final Logger logger = LoggerFactory.getLogger(SqsController.class);
    private static final String NOTIFICATIONS_RELAY_TEST_QUEUE = "notificationsRelayTestQueue";
    private static final String NOTIFICATIONS_RELAY_FINAL_TEST_QUEUE = "notificationsRelayFinalTestQueue";

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @PostMapping(value = "/send")
    @ResponseStatus(HttpStatus.OK)
    public void sendMessageToRelayQueue(@RequestBody Message message){
        logger.info("Sending message {} to SQS", message.toString());
        this.queueMessagingTemplate.convertAndSend(NOTIFICATIONS_RELAY_TEST_QUEUE, message);
    }

    @SqsListener(NOTIFICATIONS_RELAY_TEST_QUEUE)
    @SendTo(NOTIFICATIONS_RELAY_FINAL_TEST_QUEUE)
    private Message recieveMessageFromQueue(Message message){
        logger.info("Received message {} from queue", message.toString());
        message.setContent("This is now a relayed message");
        return message;
    }

    @SqsListener(NOTIFICATIONS_RELAY_FINAL_TEST_QUEUE)
    private void recieveRelayedMessageFromQueue(Message message){
        logger.info("Received message {} from queue", message.toString());
    }
}
