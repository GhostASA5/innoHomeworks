package org.project.controller;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер для обработки страницы входа в систему.
 * Предоставляет endpoint для отображения формы аутентификации.
 *
 * @author GhostASA5
 * @version 1.0
 */
@Controller
public class LoginController {

    /**
     * Отображает страницу входа в систему.
     *
     * @return имя Thymeleaf шаблона "login"
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
