package com.cmorfe.minesweeper.security;

public class Constants {
    public static final String SECRET = "M1ne5weeper";
    public static final long EXPIRATION_TIME = 900_000_000; // 15.000 mins
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/signin";
}
