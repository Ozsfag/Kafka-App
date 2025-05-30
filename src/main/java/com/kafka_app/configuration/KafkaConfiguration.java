package com.kafka_app.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka_app.event.OrderEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaConfiguration {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${app.kafka.orderEventGroupId}")
  private String orderEventGroupId;

  @Bean
  public ProducerFactory<String, OrderEvent> kafkaOrderEventProducerFactory(
      ObjectMapper objectMapper) {
    Map<String, Object> config = new HashMap<>();
    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

    return new DefaultKafkaProducerFactory<>(
        config, new StringSerializer(), new JsonSerializer<>(objectMapper));
  }

  @Bean
  public KafkaTemplate<String, OrderEvent> kafkaOrderEventTemplate(
      ProducerFactory<String, OrderEvent> kafkaOrderEventProducerFactory) {
    return new KafkaTemplate<>(kafkaOrderEventProducerFactory);
  }

  @Bean
  public ConsumerFactory<String, OrderEvent> orderEventConsumerFactory(ObjectMapper objectMapper) {
    Map<String, Object> config = new HashMap<>();
    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    config.put(ConsumerConfig.GROUP_ID_CONFIG, orderEventGroupId);
    config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

    return new DefaultKafkaConsumerFactory<>(
        config, new StringDeserializer(), new JsonDeserializer<>(objectMapper));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, OrderEvent>
      concurrentKafkaOrderListenerContainerFactory(
          ConsumerFactory<String, OrderEvent> orderEventConsumerFactory) {
    ConcurrentKafkaListenerContainerFactory<String, OrderEvent> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(orderEventConsumerFactory);
    return factory;
  }
}
