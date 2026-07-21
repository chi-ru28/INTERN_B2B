package com.b2becommerce.identityservice.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("null")
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void publishEvent(String topic, Object event) {
        // kafkaTemplate.send(topic, event);
        System.out.println("Mocked Kafka event: Sent " + event.getClass().getSimpleName() + " to topic " + topic);
    }
}
