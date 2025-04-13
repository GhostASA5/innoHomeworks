package org.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;


/**
 * Конфигурация интеграции Thymeleaf с Spring Security.
 * Регистрирует диалект Spring Security для использования в Thymeleaf шаблонах.
 *
 * @author GhostASA5
 * @version 1.0
 * @see SpringSecurityDialect
 */
@Configuration
public class ThymeleafConfig {

    /**
     * Регистрирует диалект Spring Security для Thymeleaf.
     *
     * @return экземпляр SpringSecurityDialect
     */
    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }
}
