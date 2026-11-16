package com.fatec.araras.chefsystem.config.mensageria;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "chefsystem.rabbit.enabled", havingValue = "true", matchIfMissing = true)
public class RabbitConfig {
    public static final String EXCHANGE = "chefsystem.pedidos";
    public static final String PEDIDO_CRIADO = "pedido.criado";
    public static final String FILA_COZINHA = "chefsystem.cozinha.pedidos";
    public static final String FILA_FALHAS = "chefsystem.cozinha.pedidos.dlq";
    public static final String EXCHANGE_FALHAS = "chefsystem.pedidos.dlx";

    @Bean
    JacksonJsonMessageConverter rabbitJsonConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    Declarables topologiaPedidos() {
        var exchange = new DirectExchange(EXCHANGE);
        var falhas = new DirectExchange(EXCHANGE_FALHAS);
        var cozinha = QueueBuilder.durable(FILA_COZINHA)
                .deadLetterExchange(EXCHANGE_FALHAS)
                .deadLetterRoutingKey(PEDIDO_CRIADO).build();
        var dlq = QueueBuilder.durable(FILA_FALHAS).build();
        return new Declarables(exchange, falhas, cozinha, dlq,
                BindingBuilder.bind(cozinha).to(exchange).with(PEDIDO_CRIADO),
                BindingBuilder.bind(dlq).to(falhas).with(PEDIDO_CRIADO));
    }
}
