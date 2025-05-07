package com.krisnaajiep.todolistapi.mapper;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 06/05/25 21.06
@Last Modified 06/05/25 21.06
Version 1.0
*/

import com.krisnaajiep.todolistapi.model.Task;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskRowMapper implements RowMapper<Task> {
    @Override
    public Task mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        Task task = new Task();
        task.setId(rs.getInt("ID"));
        task.setTitle(rs.getString("Title"));
        task.setDescription(rs.getString("Description"));
        task.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        task.setUpdatedAt(rs.getTimestamp("UpdatedAt").toLocalDateTime());
        return task;
    }
}
