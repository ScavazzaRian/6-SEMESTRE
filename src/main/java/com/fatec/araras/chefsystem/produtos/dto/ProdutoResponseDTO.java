package com.fatec.araras.chefsystem.produtos.dto;

import com.fatec.araras.chefsystem.produtos.entities.Produto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProdutoResponseDTO {
    private Long id;
    private String nome;
    private BigDecimal precoVenda;
    private Integer quantidade;

    public ProdutoResponseDTO(Produto produto){
        id = produto.getId();
        nome = produto.getNome();
        precoVenda = produto.getPrecoVenda();
        quantidade = produto.getQuantidade();
    }
}
