package com.fatec.araras.chefsystem;

import com.fatec.araras.chefsystem.cozinha.services.CozinhaService;
import com.fatec.araras.chefsystem.pedido.repositories.PedidoRepository;
import com.fatec.araras.chefsystem.produto.repositories.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "chefsystem.rabbit.enabled=false",
        "spring.datasource.url=jdbc:h2:mem:fluxo-pedidos;DB_CLOSE_DELAY=-1"
})
class FluxoPedidosTest {
    @LocalServerPort int porta;
    @Autowired CozinhaService cozinha;
    @Autowired PedidoRepository pedidos;
    @Autowired ProdutoRepository produtos;
    final JsonMapper json = JsonMapper.builder().build();
    final HttpClient http = HttpClient.newHttpClient();

    @Test
    void mesaFazPedidoEACozinhaIniciaEConclui() throws Exception {
        var criado = requisicao("POST", "/pedidos", """
                {"mesaId": 1, "itens": [{"produtoId": 1, "quantidade": 2}]}
                """);
        assertThat(criado.statusCode()).isEqualTo(201);
        var pedido = json.readTree(criado.body());
        long id = pedido.get("id").asLong();
        assertThat(criado.headers().firstValue("location").orElseThrow()).endsWith("/pedidos/" + id);
        assertThat(pedido.get("statusPedido").asString()).isEqualTo("ABERTO");
        assertThat(pedido.get("total").decimalValue()).isEqualByComparingTo("24.00");
        assertThat(pedidos.findById(id).orElseThrow().getEnviadoEm()).isNull();
        assertThat(listaCozinhaContem(id)).isFalse();
        assertThat(status(id, "PREPARANDO").statusCode()).isEqualTo(409);

        cozinha.receber(id);
        var primeiroRecebimento = pedidos.findById(id).orElseThrow().getRecebidoCozinhaEm();
        cozinha.receber(id);
        assertThat(pedidos.findById(id).orElseThrow().getRecebidoCozinhaEm()).isEqualTo(primeiroRecebimento);
        assertThat(listaCozinhaContem(id)).isTrue();
        assertThat(status(id, "CONCLUIDO").statusCode()).isEqualTo(409);
        assertThat(status(id, "PREPARANDO").statusCode()).isEqualTo(200);
        assertThat(status(id, "CONCLUIDO").statusCode()).isEqualTo(200);
        assertThat(status(id, "CANCELADO").statusCode()).isEqualTo(409);
        assertThat(json.readTree(requisicao("GET", "/pedidos/" + id, null).body())
                .get("statusPedido").asString()).isEqualTo("CONCLUIDO");
    }

    @Test
    void cancelamentoDevolveEstoqueUmaVez() throws Exception {
        int antes = estoque(2L);
        var criado = requisicao("POST", "/pedidos", """
                {"mesaId": 2, "itens": [{"produtoId": 2, "quantidade": 3}]}
                """);
        assertThat(criado.statusCode()).isEqualTo(201);
        long id = json.readTree(criado.body()).get("id").asLong();
        assertThat(estoque(2L)).isEqualTo(antes - 3);
        cozinha.receber(id);
        assertThat(status(id, "PREPARANDO").statusCode()).isEqualTo(200);
        assertThat(status(id, "CANCELADO").statusCode()).isEqualTo(200);
        assertThat(status(id, "CANCELADO").statusCode()).isEqualTo(200);
        assertThat(estoque(2L)).isEqualTo(antes);
    }

    @Test
    void pedidoInvalidoNaoCriaPedidoNemAlteraEstoque() throws Exception {
        long antes = pedidos.count();
        int estoque = estoque(3L);
        assertThat(requisicao("POST", "/pedidos", """
                {"mesaId":1,"itens":[{"produtoId":3,"quantidade":1},{"produtoId":4,"quantidade":99999}]}
                """).statusCode()).isEqualTo(409);
        assertThat(requisicao("POST", "/pedidos", """
                {"mesaId":1,"itens":[{"produtoId":3,"quantidade":0}]}
                """).statusCode()).isEqualTo(400);
        assertThat(requisicao("POST", "/pedidos", """
                {"mesaId":9999,"itens":[{"produtoId":3,"quantidade":1}]}
                """).statusCode()).isEqualTo(404);
        assertThat(estoque(3L)).isEqualTo(estoque);
        assertThat(pedidos.count()).isEqualTo(antes);
    }

    private int estoque(Long id) {
        return produtos.findById(id).orElseThrow().getQuantidade();
    }

    private boolean listaCozinhaContem(long id) throws Exception {
        for (JsonNode pedido : json.readTree(requisicao("GET", "/cozinha/pedidos", null).body())) {
            if (pedido.get("id").asLong() == id) return true;
        }
        return false;
    }

    private HttpResponse<String> status(long id, String status) throws Exception {
        return requisicao("PATCH", "/cozinha/pedidos/" + id + "/status", "{\"status\":\"" + status + "\"}");
    }

    private HttpResponse<String> requisicao(String metodo, String path, String body) throws Exception {
        var builder = HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + porta + path));
        builder.method(metodo, body == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(body));
        if (body != null) builder.header("Content-Type", "application/json");
        return http.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }
}
