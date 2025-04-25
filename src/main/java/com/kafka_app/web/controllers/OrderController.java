package com.kafka_app.web.controllers;

import com.kafka_app.event.OrderEvent;
import com.kafka_app.mappers.OrderMapper;
import com.kafka_app.web.model.OrderRequest;
import com.kafka_app.web.model.OrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

  @Value("${app.kafka.order-event-topic}")
  private String topicName;

  private final OrderMapper orderMapper;

  private final KafkaTemplate<String, OrderEvent> kafkaOrderEventTemplate;

  @SneakyThrows
  @PostMapping
  public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
    var futureOrderEvent =
        kafkaOrderEventTemplate.send(topicName, orderMapper.orderRequestToOrderEvent(orderRequest));
    var orderEvent = futureOrderEvent.get().getProducerRecord().value();
    return ResponseEntity.ok(orderMapper.orderEventToOrderResponse(orderEvent));
  }
}
