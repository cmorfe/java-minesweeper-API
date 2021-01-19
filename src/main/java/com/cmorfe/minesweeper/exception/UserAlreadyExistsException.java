package com.cmorfe.minesweeper.exception;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException() {
        super("User already exists");
    }
}
