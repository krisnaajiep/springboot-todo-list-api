package com.krisnaajiep.todolistapi.repository;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 06/05/25 20.52
@Last Modified 06/05/25 20.52
Version 1.0
*/

import org.springframework.jdbc.core.JdbcTemplate;

abstract class AbstractJdbcRepository<T, ID> implements GenericRepository<T, ID> {
    JdbcTemplate jdbcTemplate;

    AbstractJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
