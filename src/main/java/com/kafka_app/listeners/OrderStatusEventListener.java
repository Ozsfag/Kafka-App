package com.kafka_app.listeners;

import com.kafka_app.event.OrderStatusEvent;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderStatusEventListener {

  @KafkaListener(
      topics = "${app.kafka.orderStatusEventTopic}",
      groupId = "app.kafka.orderStatusEventGroupId",
      containerFactory = "concurrentKafkaListenerContainerFactory")
  public void listenOrderStatusEvent(
      @Payload OrderStatusEvent orderStatusEvent,
      @Header(value = KafkaHeaders.RECEIVED, required = false) UUID key,
      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
      @Header(KafkaHeaders.REPLY_PARTITION) Integer partition,
      @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {

    log.info("Received orderStatusEvent: {}", orderStatusEvent);
    log.info("Key: {}, Partition: {}, Topic: {}, Timestamp: {}", key, partition, topic, timestamp);
  }
}
