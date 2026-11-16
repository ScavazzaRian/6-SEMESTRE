package com.fatec.araras.chefsystem.pedido.entities;

import com.fatec.araras.chefsystem.produto.entities.Produto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "itens_pedido")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false, length = 150)
    private String nomeProduto;

    @Column(nullable = false)
    private int quantidade;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precoUnitario;

    ItemPedido(Pedido pedido, Produto produto, int quantidade) {
        this.pedido = pedido;
        this.produto = produto;
        this.nomeProduto = produto.getNome();
        this.quantidade = quantidade;
        this.precoUnitario = produto.getPrecoVenda();
    }

    public BigDecimal subtotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }
}
