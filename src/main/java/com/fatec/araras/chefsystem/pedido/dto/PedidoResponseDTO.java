package com.fatec.araras.chefsystem.pedido.dto;

import com.fatec.araras.chefsystem.mesa.dto.MesaResponseDTO;
import com.fatec.araras.chefsystem.pedido.entities.ItemPedido;
import com.fatec.araras.chefsystem.pedido.entities.Pedido;
import com.fatec.araras.chefsystem.pedido.entities.enums.StatusPedido;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record PedidoResponseDTO(Long id, MesaResponseDTO mesa, StatusPedido statusPedido,
                                Instant criadoEm, Instant recebidoCozinhaEm, List<Item> itens, BigDecimal total) {
    public PedidoResponseDTO(Pedido pedido) {
        this(pedido.getId(), new MesaResponseDTO(pedido.getMesa()), pedido.getStatusPedido(),
                pedido.getCriadoEm(), pedido.getRecebidoCozinhaEm(),
                pedido.getItens().stream().map(Item::new).toList(),
                pedido.getItens().stream().map(ItemPedido::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    public record Item(Long produtoId, String nome, int quantidade, BigDecimal precoUnitario, BigDecimal subtotal) {
        Item(ItemPedido item) {
            this(item.getProduto().getId(), item.getNomeProduto(), item.getQuantidade(),
                    item.getPrecoUnitario(), item.subtotal());
        }
    }
}
