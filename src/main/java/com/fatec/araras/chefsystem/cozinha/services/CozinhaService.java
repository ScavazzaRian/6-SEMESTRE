package com.fatec.araras.chefsystem.cozinha.services;

import com.fatec.araras.chefsystem.config.exceptions.ConflitoException;
import com.fatec.araras.chefsystem.config.exceptions.ResourceNotFoundException;
import com.fatec.araras.chefsystem.pedido.dto.PedidoResponseDTO;
import com.fatec.araras.chefsystem.pedido.entities.enums.StatusPedido;
import com.fatec.araras.chefsystem.pedido.repositories.PedidoRepository;
import com.fatec.araras.chefsystem.produto.repositories.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CozinhaService {
    private final PedidoRepository pedidos;
    private final ProdutoRepository produtos;

    @Transactional
    public void receber(Long id) {
        var pedido = pedidos.buscarParaAtualizar(id).orElseThrow(() -> new ResourceNotFoundException(id));
        pedido.receberNaCozinha();
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listar() {
        return pedidos.findAllByRecebidoCozinhaEmIsNotNullOrderByIdDesc()
                .stream().map(PedidoResponseDTO::new).toList();
    }

    @Transactional
    public PedidoResponseDTO alterarStatus(Long id, StatusPedido destino) {
        var pedido = pedidos.buscarParaAtualizar(id).orElseThrow(() -> new ResourceNotFoundException(id));
        if (pedido.getRecebidoCozinhaEm() == null) {
            throw new ConflitoException("O pedido ainda não chegou à cozinha.");
        }
        var atual = pedido.getStatusPedido();
        if (atual == destino && destino != StatusPedido.ABERTO) return new PedidoResponseDTO(pedido);
        if (!atual.permite(destino)) {
            throw new ConflitoException("Transição de " + atual + " para " + destino + " não permitida.");
        }
        if (destino == StatusPedido.CANCELADO) {
            for (var item : pedido.getItens().stream().sorted(Comparator.comparing(i -> i.getProduto().getId())).toList()) {
                Long produtoId = item.getProduto().getId();
                var produto = produtos.buscarParaAtualizar(produtoId)
                        .orElseThrow(() -> new ResourceNotFoundException(produtoId));
                produto.setQuantidade(Math.addExact(produto.getQuantidade(), item.getQuantidade()));
            }
        }
        pedido.alterarStatus(destino);
        return new PedidoResponseDTO(pedido);
    }
}
