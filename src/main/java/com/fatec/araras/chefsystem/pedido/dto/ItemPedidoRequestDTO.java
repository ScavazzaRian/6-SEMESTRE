package com.fatec.araras.chefsystem.pedido.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemPedidoRequestDTO(
        @Schema(example = "1") @NotNull @Positive Long produtoId,
        @Schema(example = "2") @NotNull @Positive Integer quantidade
) {}
