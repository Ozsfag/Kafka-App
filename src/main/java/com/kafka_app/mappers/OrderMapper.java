package com.kafka_app.mappers;

import com.kafka_app.event.OrderEvent;
import com.kafka_app.web.model.OrderRequest;
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
  @Mapping(target = "product", conditionQualifiedByName = "quantityToString", source = "product")
  OrderEvent orderRequestToOrder(OrderRequest orderRequest);

  @Named("quantityToString")
  default String quantityToString(Integer quantity) {
    return Integer.toString(quantity);
  }
}
