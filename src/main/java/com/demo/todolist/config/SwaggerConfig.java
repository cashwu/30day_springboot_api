package com.demo.todolist.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author cash.wu
 * @since 2024/09/03
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Todo List API")
                        .version("1.0")
                        .description("一個簡單但功能強大的待辦事項 API")
                        .contact(new Contact()
                                .name("Cash")
                                .email("cash@cashwu.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("開發環境"),
                        new Server().url("https://api.example.com").description("生產環境")
                ));
    }
}
