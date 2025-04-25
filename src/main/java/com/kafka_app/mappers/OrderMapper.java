package com.kafka_app.mappers;

import com.kafka_app.event.OrderEvent;
import com.kafka_app.web.model.OrderRequest;
import com.kafka_app.web.model.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
  @Mapping(target = "product", source = "product")
  @Mapping(target = "quantity", conditionQualifiedByName = "quantityToString", source = "quantity")
  OrderEvent orderRequestToOrderEvent(OrderRequest orderRequest);

  @Mapping(target = "product", source = "product")
  @Mapping(target = "quantity", source = "quantity")
  OrderResponse orderEventToOrderResponse(OrderEvent orderEvent);

  @Named("quantityToString")
  default String quantityToString(Integer quantity) {
    return Integer.toString(quantity);
  }
}
