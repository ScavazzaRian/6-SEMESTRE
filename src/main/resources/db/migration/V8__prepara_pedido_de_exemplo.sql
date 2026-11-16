-- Completa o pedido da V6 com uma unidade do primeiro produto.
-- Só aproveita o registro legado, ainda aberto, sem itens e com estoque disponível.
INSERT INTO itens_pedido (pedido_id, produto_id, nome_produto, quantidade, preco_unitario)
SELECT pedido.id, produto.id, produto.nome, 1, produto.preco_venda
FROM pedidos pedido
JOIN mesas mesa ON mesa.id = pedido.mesa_id
CROSS JOIN produtos produto
WHERE pedido.id = 1 AND mesa.numero = 1 AND pedido.status = 'ABERTO'
  AND pedido.enviado_em = pedido.criado_em AND pedido.recebido_cozinha_em IS NULL
  AND produto.id = 1 AND produto.quantidade >= 1
  AND NOT EXISTS (SELECT 1 FROM itens_pedido item WHERE item.pedido_id = pedido.id);

-- Reserva o estoque para que o cancelamento possa devolver a quantidade corretamente.
UPDATE produtos
SET quantidade = quantidade - 1
WHERE id = 1 AND EXISTS (
    SELECT 1 FROM itens_pedido item
    JOIN pedidos pedido ON pedido.id = item.pedido_id
    JOIN mesas mesa ON mesa.id = pedido.mesa_id
    WHERE item.pedido_id = 1 AND item.produto_id = 1 AND item.quantidade = 1
      AND mesa.numero = 1 AND pedido.status = 'ABERTO'
      AND pedido.enviado_em = pedido.criado_em AND pedido.recebido_cozinha_em IS NULL
);

-- O pedido de exemplo começa na fila lógica da cozinha, sem publicar uma mensagem de teste.
UPDATE pedidos
SET recebido_cozinha_em = CURRENT_TIMESTAMP
WHERE id = 1 AND status = 'ABERTO'
  AND enviado_em = criado_em AND recebido_cozinha_em IS NULL
  AND EXISTS (SELECT 1 FROM mesas mesa WHERE mesa.id = pedidos.mesa_id AND mesa.numero = 1)
  AND EXISTS (SELECT 1 FROM itens_pedido item
              WHERE item.pedido_id = pedidos.id AND item.produto_id = 1 AND item.quantidade = 1);
