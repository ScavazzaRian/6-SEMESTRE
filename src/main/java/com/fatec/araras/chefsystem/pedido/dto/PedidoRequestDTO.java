package com.fatec.araras.chefsystem.pedido.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;

public record PedidoRequestDTO(
        @Schema(description = "ID da mesa", example = "1") @NotNull @Positive Long mesaId,
        @NotEmpty @Size(max = 100) List<@NotNull @Valid ItemPedidoRequestDTO> itens
) {}
