package com.fatec.araras.chefsystem.mesa.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MesaRequestDTO {
    @NotNull
    @Positive
    private Integer numero;

    @NotNull
    @Positive
    private Integer quantidade;
}
