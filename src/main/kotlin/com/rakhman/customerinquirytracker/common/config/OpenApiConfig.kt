package com.rakhman.customerinquirytracker.common.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
/**
 * @Configuration сообщает Spring,
 * что класс содержит настройки приложения
 */
class OpenApiConfig {

    @Bean
            /**
             * @Bean просит вызвать метод при запуске и зарегистрировать вовзращенный
             * объект в контексте Spring
             */
    fun inquiryOpenApi(): OpenAPI{
        return OpenAPI().info(
            Info()
                .title("Customer InquiryTracker API")
                .version("v1.0.0")
                .description("Rest API для учета обращений клиентов в техподдержку")
        )
    }
}