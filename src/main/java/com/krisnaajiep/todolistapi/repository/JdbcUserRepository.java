package com.krisnaajiep.todolistapi.repository;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 06/05/25 20.53
@Last Modified 06/05/25 20.53
Version 1.0
*/

import com.krisnaajiep.todolistapi.mapper.UserRowMapper;
import com.krisnaajiep.todolistapi.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcUserRepository extends AbstractJdbcRepository<User, Integer> {
    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM [User] WHERE Email = ?";

        try {
            User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), email);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findById(Integer id) {
        String sql = "SELECT * FROM [User] WHERE ID = ?";

        try {
            User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public User save(User user) {
        String sql = "INSERT INTO [User] (Name, Email, Password) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            var ps = con.prepareStatement(sql, new String[]{"ID"});
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            return ps;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        return findById(user.getId()).orElse(user);
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public List<User> findAll() {
        return List.of();
    }
}
