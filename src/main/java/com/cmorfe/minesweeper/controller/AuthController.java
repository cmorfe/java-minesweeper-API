package com.cmorfe.minesweeper.controller;


import com.cmorfe.minesweeper.entity.User;
import com.cmorfe.minesweeper.service.UserAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping()
public class AuthController {

    private final UserAuthService userAuthService;

    public AuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @PostMapping("signin")
    public ResponseEntity<Object> signin(@RequestBody User user) {
        userAuthService.signin(user);

        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PostMapping("signout")
    public ResponseEntity<Object> signout(HttpServletRequest request, HttpServletResponse response) {
        userAuthService.signout(request, response);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}