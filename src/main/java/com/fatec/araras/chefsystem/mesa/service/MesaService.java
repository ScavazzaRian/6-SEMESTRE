package com.fatec.araras.chefsystem.mesa.service;

import com.fatec.araras.chefsystem.config.exceptions.ResourceNotFoundException;
import com.fatec.araras.chefsystem.mesa.dto.MesaResponseDTO;
import com.fatec.araras.chefsystem.mesa.entities.Mesa;
import com.fatec.araras.chefsystem.mesa.repository.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesaService {
    @Autowired
    private MesaRepository repository;

    public List<MesaResponseDTO> findAll(){
        List<Mesa> list = repository.findAll();
        return list.stream()
                .map(MesaResponseDTO::new)
                .toList();
    }

    public MesaResponseDTO findById(Long id){
        Mesa mesa = repository.findById(id).orElseThrow(()->new ResourceNotFoundException(id));
        return new MesaResponseDTO(mesa);
    }
}
