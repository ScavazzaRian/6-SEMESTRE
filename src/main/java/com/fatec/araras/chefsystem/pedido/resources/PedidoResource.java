package com.fatec.araras.chefsystem.pedido.resources;

import com.fatec.araras.chefsystem.pedido.dto.PedidoRequestDTO;
import com.fatec.araras.chefsystem.pedido.dto.PedidoResponseDTO;
import com.fatec.araras.chefsystem.pedido.entities.Pedido;
import com.fatec.araras.chefsystem.pedido.services.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {
    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> getAll(){
        List<PedidoResponseDTO> list = pedidoService.getAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PedidoResponseDTO> findById(@PathVariable Long id){
        PedidoResponseDTO pedidoResponseDTO = pedidoService.findById(id);
        return ResponseEntity.ok().body(pedidoResponseDTO);
    }

    @PostMapping
    public ResponseEntity<Pedido> insert(@Valid @RequestBody PedidoRequestDTO obj){
        Pedido pedido = pedidoService.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(pedido.getId()).toUri();
        return ResponseEntity.created(uri).body(pedido);
    }
}
