package com.fatec.araras.chefsystem.cozinha.dto;

import com.fatec.araras.chefsystem.pedido.entities.enums.StatusPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record AlterarStatusDTO(
        @Schema(description = "Novo status do pedido",
                allowableValues = {"PREPARANDO", "CONCLUIDO", "CANCELADO"}, example = "PREPARANDO")
        @NotNull StatusPedido status
) {}
