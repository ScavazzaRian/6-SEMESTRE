package com.fatec.araras.chefsystem.produtos.repositories;

import com.fatec.araras.chefsystem.produtos.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
