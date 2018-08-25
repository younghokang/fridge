package com.poseidon.fridge.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.poseidon.fridge.model.Food;

@Repository
public class JdbcFoodRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
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
    
    public int update(Food food) {
        return jdbcTemplate.update("UPDATE food SET quantity=? WHERE id=?", food.getQuantity(), food.getId());
    }

    public Food findById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM food WHERE id=?", foodRowMapper, id);
    }
    
    public List<Food> findAll() {
        return jdbcTemplate.query("SELECT * FROM food", foodRowMapper);
    }
    
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM food");
    }
    
    public int count() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM food", int.class);
    }
    
    public int remove(Food food) {
        return jdbcTemplate.update("DELETE FROM food WHERE id=?", food.getId());
    }

    public int insert(Food food) {
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
