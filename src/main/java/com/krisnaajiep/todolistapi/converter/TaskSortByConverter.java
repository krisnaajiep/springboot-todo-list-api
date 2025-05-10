package com.krisnaajiep.todolistapi.converter;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 10/05/25 14.44
@Last Modified 10/05/25 14.44
Version 1.0
*/

import com.krisnaajiep.todolistapi.enums.TaskSortBy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static com.krisnaajiep.todolistapi.enums.TaskSortBy.*;

@Component
public class TaskSortByConverter implements Converter<String, TaskSortBy> {
    @Override
    public TaskSortBy convert(@NonNull String source) {
        return switch (source) {
            case String s when s.equalsIgnoreCase("title") -> TITLE;
            case String s when s.equalsIgnoreCase("updatedAt") -> UPDATED_AT;
            default -> CREATED_AT;
        };
    }
}
