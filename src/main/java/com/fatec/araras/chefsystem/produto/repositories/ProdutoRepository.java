package com.fatec.araras.chefsystem.produto.repositories;

import com.fatec.araras.chefsystem.produto.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
