package com.kafka_app.listeners;

import com.kafka_app.event.OrderStatusEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderStatusEventListener {

  @KafkaListener(
      topics = "${app.kafka.orderStatusEventTopic}",
      groupId = "app.kafka.orderStatusEventGroupId",
      containerFactory = "concurrentKafkaOrderListenerContainerFactory")
  public void listenOrderStatusEvent(@Payload OrderStatusEvent orderStatusEvent) {

    log.info("Received orderStatusEvent: {}", orderStatusEvent);
  }
}
