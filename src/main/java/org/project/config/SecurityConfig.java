package org.project.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


/**
 * Основной класс конфигурации безопасности Spring Security.
 * Определяет правила доступа к эндпоинтам, настройки аутентификации и авторизации,
 * а также подключает кастомные компоненты безопасности.
 *
 * @author GhostASA5
 * @version 1.0
 * @see CustomAuthenticationEntryPoint
 * @see LoggingFilter
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationEntryPoint entryPoint;

    private final LoggingFilter loggingFilter;

    /**
     * Конфигурирует цепочку фильтров безопасности.
     *
     * @param http builder для настройки безопасности
     * @return сконфигурированную цепочку фильтров
     * @throws Exception при ошибках конфигурации
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/login", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/tasks").hasAnyRole("VIEWER", "USER", "ADMIN")
                        .requestMatchers("/tasks/add").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/tasks/delete/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic()
                .authenticationEntryPoint(entryPoint)
                .and()
                .addFilterBefore(loggingFilter, BasicAuthenticationFilter.class)
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/tasks")
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }

    /**
     * Создает in-memory хранилище пользователей с тестовыми учетными записями.
     *
     * @return UserDetailsService с предопределенными пользователями
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails viewer = User.withDefaultPasswordEncoder()
                .username("viewer")
                .password("viewerpass")
                .roles("VIEWER")
                .build();

        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("userpass")
                .roles("USER")
                .build();

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("adminpass")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(viewer, user, admin);
    }
}
