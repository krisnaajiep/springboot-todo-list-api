package com.krisnaajiep.todolistapi.converter;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 10/05/25 14.46
@Last Modified 10/05/25 14.46
Version 1.0
*/

import com.krisnaajiep.todolistapi.enums.TaskSortDirection;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static com.krisnaajiep.todolistapi.enums.TaskSortDirection.*;

@Component
public class TaskSortDirectionConverter implements Converter<String, TaskSortDirection> {
    @Override
    public TaskSortDirection convert(@NonNull String source) {
        return source.equalsIgnoreCase("asc") ? ASC : DESC;
    }
}
