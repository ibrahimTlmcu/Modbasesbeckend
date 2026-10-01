package com.example.backend.controllers;

import com.example.backend.services.KafkaMessageProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final KafkaMessageProducer producer;

    public MessageController(KafkaMessageProducer producer) {
        this.producer = producer;
    }

    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestParam String message) {
        producer.sendMessage("demo-topic", message);
        return ResponseEntity.ok("Mesaj gönderildi.");
    }
}
