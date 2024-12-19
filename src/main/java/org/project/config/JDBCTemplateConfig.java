package org.project.config;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class JDBCTemplateConfig {

    public static JdbcTemplate getJdbcTemplate() {
        var driver = new DriverManagerDataSource("jdbc:postgresql://localhost:5437/bank", "postgres", "postgres");
        driver.setDriverClassName("org.postgresql.Driver");
        return new JdbcTemplate(driver);
    }
}
