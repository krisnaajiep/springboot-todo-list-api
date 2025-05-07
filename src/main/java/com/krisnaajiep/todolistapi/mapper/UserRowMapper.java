package com.krisnaajiep.todolistapi.mapper;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 06/05/25 21.04
@Last Modified 06/05/25 21.04
Version 1.0
*/

import com.krisnaajiep.todolistapi.model.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("ID"));
        user.setName(rs.getString("Name"));
        user.setEmail(rs.getString("Email"));
        user.setPassword(rs.getString("Password"));
        user.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        user.setUpdatedAt(rs.getTimestamp("UpdatedAt").toLocalDateTime());
        return user;
    }
}
