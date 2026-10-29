package com.fatec.araras.chefsystem.pedido.entities;

import com.fatec.araras.chefsystem.mesa.entities.Mesa;
import com.fatec.araras.chefsystem.pedido.entities.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mesa_id", nullable = false)
    private Mesa mesa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPedido statusPedido = StatusPedido.ABERTO;
}
