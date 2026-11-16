package com.fatec.araras.chefsystem.cozinha.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.fatec.araras.chefsystem.config.exceptions.StandardError;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fatec.araras.chefsystem.cozinha.dto.AlterarStatusDTO;
import com.fatec.araras.chefsystem.cozinha.services.CozinhaService;
import com.fatec.araras.chefsystem.pedido.dto.PedidoResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Cozinha", description = "Recebimento de pedidos e atualização do preparo.")
@RestController
@RequestMapping("/cozinha/pedidos")
@RequiredArgsConstructor
public class CozinhaResource {
    private final CozinhaService cozinha;

    @Operation(summary = "Listar pedidos recebidos pela cozinha")
    @GetMapping
    public List<PedidoResponseDTO> listar() {
        return cozinha.listar();
    }

    @Operation(summary = "Atualizar o status de um pedido",
            description = "Aberto → Preparando → Concluído. O chef também pode cancelar um pedido aberto ou em preparo.")
    @ApiResponse(responseCode = "200", description = "Status atualizado", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Status ausente ou inválido",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    @ApiResponse(responseCode = "409", description = "Pedido ainda não recebido ou transição não permitida",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    @PatchMapping("/{id}/status")
    public PedidoResponseDTO alterarStatus(@PathVariable Long id, @Valid @RequestBody AlterarStatusDTO request) {
        return cozinha.alterarStatus(id, request.status());
    }
}
