package com.fatec.araras.chefsystem.cozinha.mensageria;

import com.fatec.araras.chefsystem.config.mensageria.RabbitConfig;
import com.fatec.araras.chefsystem.cozinha.services.CozinhaService;
import com.fatec.araras.chefsystem.pedido.mensageria.PedidoCriado;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "chefsystem.rabbit.enabled", havingValue = "true", matchIfMissing = true)
public class PedidoCriadoConsumer {
    private final CozinhaService cozinha;

    @RabbitListener(queues = RabbitConfig.FILA_COZINHA)
    public void consumir(PedidoCriado evento) {
        if (evento.pedidoId() == null || evento.pedidoId() < 1) {
            throw new AmqpRejectAndDontRequeueException("O evento precisa conter um pedidoId válido.");
        }
        // O serviço confirma a transação antes de o listener retornar e dar ACK.
        cozinha.receber(evento.pedidoId());
        log.info("Cozinha recebeu o pedido {}.", evento.pedidoId());
    }
}
