package com.fatec.araras.chefsystem.mesa.resource;

import com.fatec.araras.chefsystem.mesa.dto.MesaRequestDTO;
import com.fatec.araras.chefsystem.mesa.dto.MesaResponseDTO;
import com.fatec.araras.chefsystem.mesa.entities.Mesa;
import com.fatec.araras.chefsystem.mesa.service.MesaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @PostMapping
    public ResponseEntity<MesaResponseDTO> insert(@Valid @RequestBody MesaRequestDTO obj){
        Mesa mesa = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(mesa.getId()).toUri();
        MesaResponseDTO val = new MesaResponseDTO(mesa);
        return ResponseEntity.created(uri).body(val);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<MesaResponseDTO> update(@Valid @RequestBody MesaRequestDTO obj, @PathVariable Long id){
        Mesa mesa = service.update(obj, id);
        MesaResponseDTO val = new MesaResponseDTO(mesa);
        return ResponseEntity.ok().body(val);
    }
}
