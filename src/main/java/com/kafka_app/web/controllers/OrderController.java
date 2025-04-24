package com.kafka_app.web.controllers;

import com.kafka_app.event.OrderEvent;
import com.kafka_app.mappers.OrderMapper;
import com.kafka_app.web.model.OrderRequest;
import com.kafka_app.web.model.OrderResponse;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderMapper orderMapper;
  private final KafkaTemplate<String, OrderEvent> kafkaOrderEventTemplate;

  @SneakyThrows
  @PostMapping
  public ResponseEntity<OrderResponse> createOrder(
      @RequestBody OrderRequest orderRequest) {
     CompletableFuture<SendResult<String, OrderEvent>> futureResult = kafkaOrderEventTemplate.send("${app.kafka.order-event-topic}", orderMapper.orderRequestToOrder(orderRequest));
     var result = futureResult.get().getProducerRecord().value();
     return ResponseEntity.ok(new OrderResponse(result.getProduct(), result.getQuantity()));
  }
}
