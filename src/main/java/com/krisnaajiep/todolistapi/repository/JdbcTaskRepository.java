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

import com.krisnaajiep.todolistapi.mapper.TaskRowMapper;
import com.krisnaajiep.todolistapi.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JdbcTaskRepository extends AbstractJdbcRepository<Task, Integer> {
    public JdbcTaskRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Optional<Task> findById(Integer id) {
        String sql = "SELECT * FROM [Task] WHERE ID = ?";

        try {
            Task task = jdbcTemplate.queryForObject(sql, new TaskRowMapper(), id);
            return Optional.ofNullable(task);
        } catch (Exception e) {
            return Optional.empty();
        }
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
        String sql = "UPDATE [Task] SET Title = ?, Description = ? WHERE ID = ?";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            var ps = con.prepareStatement(sql);
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setInt(3, task.getId());
            return ps;
        }, keyHolder);

        task.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        return task;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM [Task] WHERE ID = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Task> findAll() {
        return List.of();
    }

    public List<Task> findAll(
            Integer userId,
            String keyword,
            String sortBy,
            String sortDir,
            Integer start,
            Integer limit
    ) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM [Task] WHERE UserID = ? ");

        List<Object> args = new ArrayList<>();
        args.add(userId);

        if (keyword != null && !keyword.isBlank()) {
            sqlBuilder.append("AND (LOWER(Title) LIKE LOWER(CONCAT('%', ?, '%')) ")
                    .append("OR LOWER(Description) LIKE LOWER(CONCAT('%', ?, '%'))) ");

            args.add(keyword);
            args.add(keyword);
        }

        sqlBuilder.append("ORDER BY ")
                .append(sortBy)
                .append(" ")
                .append(sortDir)
                .append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ");

        args.add(start);
        args.add(limit);

        return jdbcTemplate.query(sqlBuilder.toString(), new TaskRowMapper(), args.toArray());
    }
}
