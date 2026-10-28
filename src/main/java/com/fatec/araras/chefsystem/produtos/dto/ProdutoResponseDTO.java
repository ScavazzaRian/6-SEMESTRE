package com.fatec.araras.chefsystem.produtos.dto;

import com.fatec.araras.chefsystem.produtos.entities.Produto;
import com.fatec.araras.chefsystem.produtos.entities.enums.CategoriaProduto;
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
    private String nome;
    private BigDecimal precoVenda;
    private Integer quantidade;
    private CategoriaProduto categoria;

    public ProdutoResponseDTO(Produto produto){
        nome = produto.getNome();
        precoVenda = produto.getPrecoVenda();
        quantidade = produto.getQuantidade();
        categoria = produto.getCategoria();
    }
}
