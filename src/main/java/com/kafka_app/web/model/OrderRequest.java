package com.kafka_app.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public record OrderRequest(@NotBlank @Getter String product, @NotNull @Getter Integer quantity) {}
