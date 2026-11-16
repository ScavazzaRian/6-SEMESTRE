package com.fatec.araras.chefsystem.produto.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.fatec.araras.chefsystem.produto.dto.ProdutoRequestDTO;
import com.fatec.araras.chefsystem.produto.dto.ProdutoResponseDTO;
import com.fatec.araras.chefsystem.produto.entities.Produto;
import com.fatec.araras.chefsystem.produto.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Produtos", description = "Cardápio e estoque.")
@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {
    @Autowired
    private ProdutoService service;

    @Operation(summary = "Listar produtos")
    @GetMapping()
    public ResponseEntity<List<ProdutoResponseDTO>> findAll(){
        List<ProdutoResponseDTO> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @Operation(summary = "Consultar um produto")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ProdutoResponseDTO> findById(@PathVariable Long id){
        ProdutoResponseDTO obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @Operation(summary = "Cadastrar um produto")
    @ApiResponse(responseCode = "201", description = "Registro criado", useReturnTypeSchema = true)
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> insert(@Valid @RequestBody ProdutoRequestDTO obj){
        Produto produto = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(produto.getId()).toUri();
        ProdutoResponseDTO devol = new ProdutoResponseDTO(produto);
        return ResponseEntity.created(uri).body(devol);
    }

    @Operation(summary = "Excluir um produto")
    @ApiResponse(responseCode = "204", description = "Registro excluído", content = @Content)
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualizar um produto")
    @PutMapping(value = "/{id}")
    public ResponseEntity<ProdutoResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ProdutoRequestDTO obj){
        Produto produto = service.update(id, obj);
        ProdutoResponseDTO devolve = new ProdutoResponseDTO(produto);
        return ResponseEntity.ok().body(devolve);
    }
}
