package com.tripcok.tripcokserver.global.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kafka")
public class KafkaController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @PostMapping("/publish")
    public String publishMessage(@RequestParam String message) {
        kafkaProducerService.sendMessage(message);
        return "Message published: " + message;
    }
}