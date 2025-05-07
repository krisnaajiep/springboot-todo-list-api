package com.krisnaajiep.todolistapi.repository;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 06/05/25 20.42
@Last Modified 06/05/25 20.42
Version 1.0
*/

import java.util.List;
import java.util.Optional;

interface GenericRepository<T, ID> {
    Optional<T> findById(ID id);
    T save(T t);
    T update(T t);
    void deleteById(ID id);
    List<T> findAll();
}
