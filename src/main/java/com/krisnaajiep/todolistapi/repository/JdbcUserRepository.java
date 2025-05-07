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

import com.krisnaajiep.todolistapi.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcUserRepository extends AbstractJdbcRepository<User, Integer> {
    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public List<User> findAll() {
        return List.of();
    }
}
