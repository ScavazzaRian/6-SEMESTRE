package com.fatec.araras.chefsystem;

import com.fatec.araras.chefsystem.config.mensageria.RabbitConfig;
import com.fatec.araras.chefsystem.pedido.dto.ItemPedidoRequestDTO;
import com.fatec.araras.chefsystem.pedido.dto.PedidoRequestDTO;
import com.fatec.araras.chefsystem.pedido.mensageria.PedidoCriado;
import com.fatec.araras.chefsystem.pedido.repositories.PedidoRepository;
import com.fatec.araras.chefsystem.pedido.services.PedidoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@EnabledIfEnvironmentVariable(named = "CHEF_TEST_RABBIT", matches = "true")
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:rabbit-fluxo;DB_CLOSE_DELAY=-1",
        "chefsystem.rabbit.publicacao-intervalo-ms=250"
})
class RabbitFluxoTest {
    @Autowired PedidoService pedidoService;
    @Autowired PedidoRepository pedidos;
    @Autowired RabbitTemplate rabbit;

    @Test
    void publicaConsomeEEncaminhaEventoInvalidoParaDlq() throws Exception {
        var pedido = pedidoService.insert(new PedidoRequestDTO(1L, List.of(new ItemPedidoRequestDTO(1L, 1))));
        var prazo = System.nanoTime() + Duration.ofSeconds(20).toNanos();
        while (System.nanoTime() < prazo) {
            var registro = pedidos.findById(pedido.id()).orElseThrow();
            if (registro.getRecebidoCozinhaEm() != null && registro.getEnviadoEm() != null) break;
            Thread.sleep(100);
        }
        var registro = pedidos.findById(pedido.id()).orElseThrow();
        assertThat(registro.getEnviadoEm()).isNotNull();
        assertThat(registro.getRecebidoCozinhaEm()).isNotNull();

        rabbit.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.PEDIDO_CRIADO, new PedidoCriado(-1L));
        var mensagem = rabbit.receive(RabbitConfig.FILA_FALHAS, 10_000);
        assertThat(mensagem).isNotNull();
        assertThat(mensagem.getMessageProperties().getHeaders()).containsKey("x-death");
    }
}
