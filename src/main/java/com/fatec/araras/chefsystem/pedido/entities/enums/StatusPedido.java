package com.fatec.araras.chefsystem.pedido.entities.enums;

public enum StatusPedido {
    ABERTO, PREPARANDO, CONCLUIDO, CANCELADO;

    public boolean permite(StatusPedido destino) {
        return switch (this) {
            case ABERTO -> destino == PREPARANDO || destino == CANCELADO;
            case PREPARANDO -> destino == CONCLUIDO || destino == CANCELADO;
            case CONCLUIDO, CANCELADO -> false;
        };
    }
}
