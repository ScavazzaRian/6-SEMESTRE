package com.fatec.araras.chefsystem.pedido.services;

import com.fatec.araras.chefsystem.config.exceptions.ConflitoException;
import com.fatec.araras.chefsystem.config.exceptions.ResourceNotFoundException;
import com.fatec.araras.chefsystem.mesa.repository.MesaRepository;
import com.fatec.araras.chefsystem.pedido.dto.ItemPedidoRequestDTO;
import com.fatec.araras.chefsystem.pedido.dto.PedidoRequestDTO;
import com.fatec.araras.chefsystem.pedido.dto.PedidoResponseDTO;
import com.fatec.araras.chefsystem.pedido.entities.Pedido;
import com.fatec.araras.chefsystem.pedido.repositories.PedidoRepository;
import com.fatec.araras.chefsystem.produto.repositories.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository pedidos;
    private final MesaRepository mesas;
    private final ProdutoRepository produtos;

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> getAll(Long mesaId) {
        var lista = mesaId == null ? pedidos.findAllByOrderByIdDesc() : pedidos.findAllByMesaIdOrderByIdDesc(mesaId);
        return lista.stream().map(PedidoResponseDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO findById(Long id) {
        return pedidos.findById(id).map(PedidoResponseDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Transactional
    public PedidoResponseDTO insert(PedidoRequestDTO request) {
        var mesa = mesas.findById(request.mesaId())
                .orElseThrow(() -> new ResourceNotFoundException(request.mesaId()));
        var ids = new HashSet<Long>();
        var pedido = new Pedido(mesa);
        // Mesma ordem de bloqueio na criação e no cancelamento.
        for (var item : request.itens().stream().sorted(Comparator.comparing(ItemPedidoRequestDTO::produtoId)).toList()) {
            if (!ids.add(item.produtoId())) {
                throw new ConflitoException("Informe cada produto uma vez, com a quantidade total.");
            }
            var produto = produtos.buscarParaAtualizar(item.produtoId())
                    .orElseThrow(() -> new ResourceNotFoundException(item.produtoId()));
            if (produto.getQuantidade() < item.quantidade()) {
                throw new ConflitoException("Estoque insuficiente para " + produto.getNome() + ".");
            }
            produto.setQuantidade(produto.getQuantidade() - item.quantidade());
            pedido.adicionarItem(produto, item.quantidade());
        }
        // A própria linha pendente funciona como outbox para PedidoCriado.
        return new PedidoResponseDTO(pedidos.save(pedido));
    }
}
