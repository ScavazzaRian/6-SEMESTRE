package com.fatec.araras.chefsystem.pedido.repositories;

import com.fatec.araras.chefsystem.pedido.entities.Pedido;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findAllByOrderByIdDesc();
    List<Pedido> findAllByMesaIdOrderByIdDesc(Long mesaId);
    List<Pedido> findAllByRecebidoCozinhaEmIsNotNullOrderByIdDesc();

    @Query("select p.id from Pedido p where p.enviadoEm is null order by p.id")
    List<Long> buscarPendentes(Pageable pagina);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Pedido p where p.id = :id")
    Optional<Pedido> buscarParaAtualizar(@Param("id") Long id);
}
