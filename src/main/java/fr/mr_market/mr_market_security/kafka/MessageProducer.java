package fr.mr_market.mr_market_security.kafka;

import fr.mr_market.mr_market_security.model.mail.MailRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {

    private final KafkaTemplate<String, MailRequest> kafkaTemplate;

    public MessageProducer(KafkaTemplate<String, MailRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEmailMessage(String topic, MailRequest mailRequest) {
        kafkaTemplate.send(topic, mailRequest);
    }

}