package com.fatec.araras.chefsystem.mesa.resource;

import com.fatec.araras.chefsystem.mesa.dto.MesaResponseDTO;
import com.fatec.araras.chefsystem.mesa.service.MesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/mesas")
public class MesaResource {
    @Autowired
    private MesaService service;


    @GetMapping
    public ResponseEntity<List<MesaResponseDTO>> findAll(){
        List<MesaResponseDTO> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<MesaResponseDTO> findById(@PathVariable Long id){
        MesaResponseDTO mesa = service.findById(id);
        return ResponseEntity.ok().body(mesa);
    }
}
