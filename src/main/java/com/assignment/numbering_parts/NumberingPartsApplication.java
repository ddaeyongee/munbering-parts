package com.assignment.numbering_parts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@SpringBootApplication
public class NumberingPartsApplication {

    public static void main(String[] args) {
        SpringApplication.run(NumberingPartsApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("채번 API")
                        .version("1.0")
                        .description("GUID 및 Sequence 생성"));
    }
}
