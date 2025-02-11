package org.project.student.aop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AOPConfig {

    @Bean
    public LogAspect logAspect() {
        return new LogAspect();
    }
}
