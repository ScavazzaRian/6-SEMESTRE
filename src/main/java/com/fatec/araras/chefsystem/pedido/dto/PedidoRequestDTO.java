package com.fatec.araras.chefsystem.pedido.dto;

import com.fatec.araras.chefsystem.pedido.entities.Pedido;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoRequestDTO {
    @NotNull(message = "A mesa é obrigatória")
    @Positive(message = "O ID da mesa deve ser positivo")
    private Long mesaId;
}
