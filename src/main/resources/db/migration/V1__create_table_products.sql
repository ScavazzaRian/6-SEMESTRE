CREATE TABLE produtos (
      id BIGINT NOT NULL AUTO_INCREMENT,
      nome VARCHAR(150) NOT NULL,
      preco_compra DECIMAL(10, 2) NOT NULL,
      preco_venda DECIMAL(10, 2) NOT NULL,
      quantidade INT NOT NULL,

      CONSTRAINT pk_produtos PRIMARY KEY (id),
      CONSTRAINT uk_produtos_nome UNIQUE (nome)
);