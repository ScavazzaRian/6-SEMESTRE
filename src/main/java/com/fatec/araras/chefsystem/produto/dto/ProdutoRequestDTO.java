package com.fatec.araras.chefsystem.produto.dto;

import com.fatec.araras.chefsystem.produto.entities.enums.CategoriaProduto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
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
    @Size(max = 150)
    private String nome;

    @NotNull
    @Positive
    @Digits(integer = 10, fraction = 2)
    private BigDecimal precoVenda;

    @NotNull
    @Positive
    @Digits(integer = 10, fraction = 2)
    private BigDecimal precoCompra;

    @PositiveOrZero
    @NotNull
    private Integer quantidade;

    @NotNull
    private CategoriaProduto categoria;
}
