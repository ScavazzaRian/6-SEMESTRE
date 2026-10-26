package com.fatec.araras.chefsystem.produtos.resources;

import com.fatec.araras.chefsystem.produtos.dto.ProdutoRequestDTO;
import com.fatec.araras.chefsystem.produtos.dto.ProdutoResponseDTO;
import com.fatec.araras.chefsystem.produtos.entities.Produto;
import com.fatec.araras.chefsystem.produtos.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {
    @Autowired
    private ProdutoService service;

    @GetMapping()
    public ResponseEntity<List<ProdutoResponseDTO>> findAll(){
        List<ProdutoResponseDTO> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProdutoResponseDTO> findById(@PathVariable Long id){
        ProdutoResponseDTO obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> insert(@Valid @RequestBody ProdutoRequestDTO obj){
        Produto produto = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(produto.getId()).toUri();
        ProdutoResponseDTO devol = new ProdutoResponseDTO(produto);
        return ResponseEntity.created(uri).body(devol);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProdutoResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ProdutoRequestDTO obj){
        Produto produto = service.update(id, obj);
        ProdutoResponseDTO devolve = new ProdutoResponseDTO(produto);
        return ResponseEntity.ok().body(devolve);
    }
}
