package com.fatec.araras.chefsystem.mesa.service;

import com.fatec.araras.chefsystem.config.exceptions.ResourceNotFoundException;
import com.fatec.araras.chefsystem.mesa.dto.MesaRequestDTO;
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

    public Mesa insert(MesaRequestDTO obj){
        Mesa mesa = new Mesa(null, obj.getNumero(), obj.getQuantidade());
        return repository.save(mesa);
    }

    public void delete(Long id){
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException(id);
        }
        repository.deleteById(id);
    }

    public Mesa update(MesaRequestDTO obj, Long id){
        Mesa entity = repository.getReferenceById(id);
        updateData(entity, obj);
        return repository.save(entity);
    }

    private void updateData(Mesa entity, MesaRequestDTO obj) {
        entity.setNumero(obj.getNumero());
        entity.setQuantidade(obj.getQuantidade());
    }
}
