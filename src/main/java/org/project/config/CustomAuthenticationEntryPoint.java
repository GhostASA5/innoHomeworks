package org.project.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Кастомная реализация точки входа для аутентификации.
 * Обрабатывает случаи неудачной аутентификации, отправляя HTTP-ответ 401 (Unauthorized)
 * с соответствующими заголовками WWW-Authenticate.
 *
 * @author GhostASA5
 * @version 1.0
 * @see BasicAuthenticationEntryPoint
 * @see SecurityConfig
 */
@Component
public class CustomAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    /**
     * Обрабатывает неудачную попытку аутентификации.
     *
     * @param request HTTP-запрос
     * @param response HTTP-ответ
     * @param authException исключение аутентификации
     * @throws IOException при ошибках ввода-вывода
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }

    /**
     * Устанавливает имя realm для аутентификации после инициализации бина.
     */
    @Override
    public void afterPropertiesSet() {
        setRealmName("MyAppRealm");
        super.afterPropertiesSet();
    }
}
