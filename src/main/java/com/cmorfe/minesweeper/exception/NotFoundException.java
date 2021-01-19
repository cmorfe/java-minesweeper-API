package com.cmorfe.minesweeper.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException() {
        super("Not found");
    }
}
