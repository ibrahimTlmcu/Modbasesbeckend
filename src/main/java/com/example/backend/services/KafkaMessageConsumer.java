package com.example.backend.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageConsumer {

    @KafkaListener(topics = "demo-topic", groupId = "myGroup")
    public void consume(String message) {
        System.out.println("Mesaj alındı: " + message);
    }
}
