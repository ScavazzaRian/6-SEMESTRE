package com.fatec.araras.chefsystem.mesa.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Mesas", description = "Cadastro e consulta das mesas do restaurante.")
@RestController
@RequestMapping(value = "/mesas")
public class MesaResource {
    @Autowired
    private MesaService service;


    @Operation(summary = "Listar mesas")
    @GetMapping
    public ResponseEntity<List<MesaResponseDTO>> findAll(){
        List<MesaResponseDTO> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @Operation(summary = "Consultar uma mesa")
    @GetMapping(value = "/{id}")
    public ResponseEntity<MesaResponseDTO> findById(@PathVariable Long id){
        MesaResponseDTO mesa = service.findById(id);
        return ResponseEntity.ok().body(mesa);
    }

    @Operation(summary = "Cadastrar uma mesa")
    @ApiResponse(responseCode = "201", description = "Registro criado", useReturnTypeSchema = true)
    @PostMapping
    public ResponseEntity<MesaResponseDTO> insert(@Valid @RequestBody MesaRequestDTO obj){
        Mesa mesa = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(mesa.getId()).toUri();
        MesaResponseDTO val = new MesaResponseDTO(mesa);
        return ResponseEntity.created(uri).body(val);
    }

    @Operation(summary = "Excluir uma mesa")
    @ApiResponse(responseCode = "204", description = "Registro excluído", content = @Content)
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualizar uma mesa")
    @PutMapping(value = "/{id}")
    public ResponseEntity<MesaResponseDTO> update(@Valid @RequestBody MesaRequestDTO obj, @PathVariable Long id){
        Mesa mesa = service.update(obj, id);
        MesaResponseDTO val = new MesaResponseDTO(mesa);
        return ResponseEntity.ok().body(val);
    }
}
