package com.krisnaajiep.todolistapi.repository;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 06/05/25 21.00
@Last Modified 06/05/25 21.00
Version 1.0
*/

import com.krisnaajiep.todolistapi.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcTaskRepository extends AbstractJdbcRepository<Task, Integer> {
    public JdbcTaskRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Optional<Task> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Task save(Task task) {
        String sql = "INSERT INTO [Task] (UserID, Title, Description) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            var ps = con.prepareStatement(sql, new String[]{"ID"});
            ps.setInt(1, task.getUserId());
            ps.setString(2, task.getTitle());
            ps.setString(3, task.getDescription());
            return ps;
        }, keyHolder);

        task.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        return task;
    }

    @Override
    public Task update(Task task) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public List<Task> findAll() {
        return List.of();
    }
}
