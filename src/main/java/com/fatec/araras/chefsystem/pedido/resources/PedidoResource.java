package com.fatec.araras.chefsystem.pedido.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.fatec.araras.chefsystem.config.exceptions.StandardError;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fatec.araras.chefsystem.pedido.dto.PedidoRequestDTO;
import com.fatec.araras.chefsystem.pedido.dto.PedidoResponseDTO;
import com.fatec.araras.chefsystem.pedido.services.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.util.List;

@Tag(name = "Pedidos", description = "Registro e acompanhamento de pedidos pela mesa.")
@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoResource {
    private final PedidoService pedidoService;

    @Operation(summary = "Listar pedidos")
    @GetMapping
    public List<PedidoResponseDTO> getAll(@RequestParam(required = false) Long mesaId) {
        return pedidoService.getAll(mesaId);
    }

    @Operation(summary = "Consultar um pedido")
    @ApiResponse(responseCode = "200", description = "Pedido encontrado", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    @GetMapping("/{id}")
    public PedidoResponseDTO findById(@PathVariable Long id) {
        return pedidoService.findById(id);
    }

    @Operation(summary = "Enviar um pedido da mesa para a cozinha",
            description = "Cria o pedido como ABERTO e encaminha o envio assíncrono pelo RabbitMQ.")
    @ApiResponse(responseCode = "201", description = "Registro criado", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Mesa ou itens informados em formato inválido",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    @ApiResponse(responseCode = "404", description = "Mesa ou produto não encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    @ApiResponse(responseCode = "409", description = "Estoque insuficiente ou produto repetido no pedido",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> insert(@Valid @RequestBody PedidoRequestDTO request) {
        var pedido = pedidoService.insert(request);
        var uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}").buildAndExpand(pedido.id()).toUri();
        return ResponseEntity.created(uri).body(pedido);
    }
}
