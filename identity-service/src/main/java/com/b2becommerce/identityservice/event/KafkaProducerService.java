package com.b2becommerce.identityservice.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.lang.NonNull;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void publishEvent(@NonNull String topic, @NonNull Object event) {
        kafkaTemplate.send(topic, event);
    }
}
