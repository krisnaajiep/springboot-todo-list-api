package com.krisnaajiep.todolistapi.dto.request;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 10/05/25 00.03
@Last Modified 10/05/25 00.03
Version 1.0
*/

import com.krisnaajiep.todolistapi.enums.TaskSortBy;
import com.krisnaajiep.todolistapi.enums.TaskSortDirection;
import lombok.Data;

@Data
public class TasksRequestDto {
    private String keyword = "";
    private TaskSortBy sortBy = TaskSortBy.CREATED_AT;
    private TaskSortDirection sortDir = TaskSortDirection.ASC;
    private Integer page = 1;
    private Integer limit = 10;
}
