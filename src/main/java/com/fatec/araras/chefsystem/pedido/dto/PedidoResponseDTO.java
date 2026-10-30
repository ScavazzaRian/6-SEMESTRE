package com.fatec.araras.chefsystem.pedido.dto;

import com.fatec.araras.chefsystem.mesa.entities.Mesa;
import com.fatec.araras.chefsystem.pedido.entities.Pedido;
import com.fatec.araras.chefsystem.pedido.entities.enums.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PedidoResponseDTO {
    private Mesa mesa;
    private StatusPedido statusPedido;

    public PedidoResponseDTO(Pedido pedido) {
        mesa = pedido.getMesa();
        statusPedido = StatusPedido.ABERTO;
    }
}
