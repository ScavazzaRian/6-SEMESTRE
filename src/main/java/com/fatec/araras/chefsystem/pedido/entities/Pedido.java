package com.fatec.araras.chefsystem.pedido.entities;

import com.fatec.araras.chefsystem.mesa.entities.Mesa;
import com.fatec.araras.chefsystem.pedido.entities.enums.StatusPedido;
import com.fatec.araras.chefsystem.produto.entities.Produto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mesa_id", nullable = false)
    private Mesa mesa;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusPedido statusPedido = StatusPedido.ABERTO;

    @Column(nullable = false)
    private Instant criadoEm;

    private Instant enviadoEm;
    private Instant recebidoCozinhaEm;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.PERSIST)
    @OrderBy("id ASC")
    private List<ItemPedido> itens = new ArrayList<>();

    public Pedido(Mesa mesa) {
        this.mesa = mesa;
        this.criadoEm = Instant.now();
    }

    public void adicionarItem(Produto produto, int quantidade) {
        itens.add(new ItemPedido(this, produto, quantidade));
    }

    public void confirmarEnvio() {
        if (enviadoEm == null) enviadoEm = Instant.now();
    }

    public void receberNaCozinha() {
        if (recebidoCozinhaEm == null) recebidoCozinhaEm = Instant.now();
    }

    public void alterarStatus(StatusPedido status) {
        statusPedido = status;
    }
}
