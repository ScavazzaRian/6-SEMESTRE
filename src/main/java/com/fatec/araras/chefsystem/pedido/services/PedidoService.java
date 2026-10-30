package com.fatec.araras.chefsystem.pedido.services;

import com.fatec.araras.chefsystem.config.exceptions.ResourceNotFoundException;
import com.fatec.araras.chefsystem.mesa.entities.Mesa;
import com.fatec.araras.chefsystem.mesa.repository.MesaRepository;
import com.fatec.araras.chefsystem.pedido.dto.PedidoRequestDTO;
import com.fatec.araras.chefsystem.pedido.dto.PedidoResponseDTO;
import com.fatec.araras.chefsystem.pedido.entities.Pedido;
import com.fatec.araras.chefsystem.pedido.entities.enums.StatusPedido;
import com.fatec.araras.chefsystem.pedido.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private MesaRepository mesaRepository;

    public List<PedidoResponseDTO> getAll(){
        List<Pedido> list = pedidoRepository.findAll();
        return list.stream().map(PedidoResponseDTO::new).toList();
    }

    public PedidoResponseDTO findById(Long id){
        Pedido obj = pedidoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(id));
        return new PedidoResponseDTO(obj);
    }

    public Pedido insert(PedidoRequestDTO obj){
        Mesa mesa = mesaRepository.findById(obj.getMesaId()).orElseThrow(()->new ResourceNotFoundException(obj.getMesaId()));
        Pedido pedido = new Pedido();
        pedido.setMesa(mesa);
        pedido.setStatusPedido(StatusPedido.ABERTO);

        return pedidoRepository.save(pedido);
    }
}
