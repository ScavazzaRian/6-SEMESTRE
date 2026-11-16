package com.fatec.araras.chefsystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@OpenAPIDefinition(info = @Info(
        title = "ChefSystem API",
        version = "1.0",
        description = "Gerencie mesas e produtos, registre pedidos e acompanhe o preparo na cozinha."
))
public class OpenApiConfig {
}
