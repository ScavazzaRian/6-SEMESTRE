package com.fatec.araras.chefsystem.pedido.mensageria;

import com.fatec.araras.chefsystem.config.mensageria.RabbitConfig;
import com.fatec.araras.chefsystem.pedido.repositories.PedidoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "chefsystem.rabbit.enabled", havingValue = "true", matchIfMissing = true)
public class PedidoCriadoPublisher {
    private final RabbitTemplate rabbit;
    private final PedidoRepository pedidos;
    private final TransactionTemplate transacao;

    @Scheduled(fixedDelayString = "${chefsystem.rabbit.publicacao-intervalo-ms:2000}")
    public void publicarPendentes() {
        for (Long id : pedidos.buscarPendentes(PageRequest.of(0, 50))) {
            try {
                var correlacao = new CorrelationData();
                rabbit.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.PEDIDO_CRIADO,
                        new PedidoCriado(id), mensagem -> {
                            mensagem.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                            mensagem.getMessageProperties().setMessageId("pedido-criado-" + id);
                            return mensagem;
                        }, correlacao);
                var confirmacao = correlacao.getFuture().get(5, TimeUnit.SECONDS);
                if (!confirmacao.ack() || correlacao.getReturned() != null) {
                    log.warn("Pedido {} ficou pendente: o RabbitMQ não confirmou o roteamento.", id);
                    return;
                }
                transacao.executeWithoutResult(ignorado ->
                        pedidos.buscarParaAtualizar(id).ifPresent(pedido -> pedido.confirmarEnvio()));
                log.info("Pedido {} publicado no RabbitMQ.", id);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } catch (Exception e) {
                log.warn("Pedido {} ficou pendente e será reenviado: {}", id, e.getMessage());
                return;
            }
        }
    }
}
