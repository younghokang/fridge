package com.poseidon.fridge;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.poseidon.fridge.model.Food;

public class JdbcFoodService {
    private JdbcTemplate jdbcTemplate;
    
    public JdbcFoodService() {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("classpath:schema.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Food save(Food food) {
        jdbcTemplate.update("INSERT INTO food (name, quantity, expiry_date) values (?,?,?)", 
                food.getName(), food.getQuantity(), food.getExpiryDate());
        return food;
    }
    
}
