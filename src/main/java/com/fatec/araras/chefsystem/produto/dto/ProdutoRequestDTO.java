package com.fatec.araras.chefsystem.produto.dto;

import com.fatec.araras.chefsystem.produto.entities.enums.CategoriaProduto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProdutoRequestDTO {
    @NotBlank
    private String nome;

    @NotNull
    @Positive
    private BigDecimal precoVenda;

    @NotNull
    @Positive
    private BigDecimal precoCompra;

    @PositiveOrZero
    @NotNull
    private Integer quantidade;

    @NotNull
    private CategoriaProduto categoria;
}
