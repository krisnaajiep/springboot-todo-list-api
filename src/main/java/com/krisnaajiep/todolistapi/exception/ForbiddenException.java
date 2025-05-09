package com.krisnaajiep.todolistapi.exception;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 09/05/25 14.50
@Last Modified 09/05/25 14.50
Version 1.0
*/

public class ForbiddenException extends RuntimeException{
    private static final String MESSAGE = "Forbidden";

    public ForbiddenException() {
        super(MESSAGE);
    }
}
