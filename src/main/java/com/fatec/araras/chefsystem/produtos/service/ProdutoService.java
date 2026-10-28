package com.fatec.araras.chefsystem.produtos.service;

import com.fatec.araras.chefsystem.config.exceptions.ResourceNotFoundException;
import com.fatec.araras.chefsystem.produtos.dto.ProdutoRequestDTO;
import com.fatec.araras.chefsystem.produtos.dto.ProdutoResponseDTO;
import com.fatec.araras.chefsystem.produtos.entities.Produto;
import com.fatec.araras.chefsystem.produtos.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository repository;

    public List<ProdutoResponseDTO> findAll(){
        List<Produto> list = repository.findAll();
        return list.stream()
                .map(ProdutoResponseDTO::new)
                .toList();
    }

    public ProdutoResponseDTO findById(Long id){
        Produto produto = repository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException(id));
        return new ProdutoResponseDTO(produto);
    }

    public Produto insert(ProdutoRequestDTO obj){
        Produto produto = new Produto(null, obj.getNome(), obj.getPrecoCompra(), obj.getPrecoVenda(), obj.getQuantidade(), obj.getCategoria());
        return repository.save(produto);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }

        repository.deleteById(id);
    }

    public Produto update(Long id, ProdutoRequestDTO obj){
        Produto entity = repository.getReferenceById(id);
        updateData(entity, obj);
        return repository.save(entity);
    }

    private void updateData(Produto entity, ProdutoRequestDTO obj) {
        entity.setNome(obj.getNome());
        entity.setPrecoCompra(obj.getPrecoCompra());
        entity.setPrecoVenda(obj.getPrecoVenda());
        entity.setQuantidade(obj.getQuantidade());
        entity.setCategoria(obj.getCategoria());
    }
}
