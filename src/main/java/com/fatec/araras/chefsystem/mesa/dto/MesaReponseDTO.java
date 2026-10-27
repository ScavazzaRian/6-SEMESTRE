package com.fatec.araras.chefsystem.mesa.dto;

import com.fatec.araras.chefsystem.mesa.entities.Mesa;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MesaReponseDTO {
    private Integer numero;
    private Integer quantidade;

    public MesaReponseDTO(Mesa mesa){
        numero = mesa.getNumero();
        quantidade = mesa.getQuantidade();
    }
}
