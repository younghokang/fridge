package com.poseidon.fridge;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.poseidon.fridge.model.Food;

public class JdbcFoodServiceTests {
    
    private static DataSource dataSource = new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.HSQL)
            .addScript("classpath:schema.sql")
            .build();
    private JdbcTemplate jdbcTemplate;
    
    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        deleteAll();
    }
    
    @Test
    public void connect() {
        int count = jdbcTemplate.queryForObject("SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS", int.class);
        assertThat(count, equalTo(1));
    }
    
    @Test
    public void insert() {
        Food milk = new Food("파스퇴르 우유 1.8L", 1, "2018-09-28");
        int affectedRows = insert(milk);
        assertThat(affectedRows, equalTo(1));
        assertThat(count(), equalTo(1));
        
        Food cola = new Food("코카콜라 500mL", 2, "2018-10-30");
        affectedRows = insert(cola);
        
        assertThat(affectedRows, equalTo(1));
        assertThat(count(), equalTo(2));
        assertThat(cola.getId(), notNullValue());
    }
    
    @Test
    public void delete_all() {
        deleteAll();
        assertThat(count(), equalTo(0));
    }
    
    @Test
    public void select_all() {
        List<Food> foods = Arrays.asList(
                new Food("파스퇴르 우유 1.8L", 1, "2018-09-28"),
                new Food("코카콜라 500mL", 2, "2018-10-30")
        );
        for(Food food : foods) {
            insert(food);
        }
        List<Food> storeFoods = findAll();
        assertThat(storeFoods.size(), equalTo(2));
        assertThat(storeFoods.get(0).getName(), equalTo(foods.get(0).getName()));
    }

    @Test
    public void selectOne() {
        Food milk = new Food("파스퇴르 우유 1.8L", 1, "2018-09-28");
        int affectedRows = insert(milk);
        assertThat(affectedRows, equalTo(1));
        
        Food storeFood = findById(milk.getId());
        assertThat(storeFood.getId(), equalTo(milk.getId()));
    }
    
    @Test
    public void updateFood() {
        Food cola = new Food("코카콜라 500mL", 2, "2018-10-30");
        int affectedRows = insert(cola);
        assertThat(affectedRows, equalTo(1));
        
        cola.decreaseQuantity(1);
        assertThat(cola.getQuantity(), equalTo(1));
        
        affectedRows = update(cola);
        assertThat(affectedRows, equalTo(1));
        
        Food storeFood = findById(cola.getId());
        assertThat(storeFood.getQuantity(), equalTo(cola.getQuantity()));
    }

    private int update(Food food) {
        return jdbcTemplate.update("UPDATE food SET quantity=? WHERE id=?", food.getQuantity(), food.getId());
    }

    private Food findById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM food WHERE id=?", foodRowMapper, id);
    }
    
    private List<Food> findAll() {
        return jdbcTemplate.query("SELECT * FROM food", foodRowMapper);
    }
    
    private static RowMapper<Food> foodRowMapper = new RowMapper<Food>() {
        @Override
        public Food mapRow(ResultSet rs, int rowNum) throws SQLException {
            Food food = new Food(rs.getString("name"),
                    rs.getInt("quantity"),
                    rs.getString("expiry_date"));
            food.setId(rs.getLong("id"));
            return food;
        }
    };

    private void deleteAll() {
        jdbcTemplate.update("DELETE FROM food");
    }
    
    private int count() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM food", int.class);
    }

    private int insert(Food food) {
        int affectedRows;
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        affectedRows = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO food (name, quantity, expiry_date) values (?,?,?)", 
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, food.getName());
                ps.setInt(2, food.getQuantity());
                ps.setString(3, food.getExpiryDate());
                return ps;
            }
        }, keyHolder);
        food.setId(keyHolder.getKey().longValue());
        return affectedRows;
    }
    
}
