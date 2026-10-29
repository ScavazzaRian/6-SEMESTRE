package com.fatec.araras.chefsystem.pedido.repositories;

import com.fatec.araras.chefsystem.pedido.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
