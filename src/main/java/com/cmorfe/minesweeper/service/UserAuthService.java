package com.cmorfe.minesweeper.service;

import com.cmorfe.minesweeper.exception.UserAlreadyExistsException;
import com.cmorfe.minesweeper.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.util.Collections.emptyList;

@Service
public class UserAuthService implements UserDetailsService {
    private final UserRepository repository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserAuthService(UserRepository repository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.repository = repository;

        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.cmorfe.minesweeper.entity.User user = repository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(user.getUsername(), user.getPassword(), emptyList());
    }

    public com.cmorfe.minesweeper.entity.User findByUsername(String username) throws UsernameNotFoundException {
        com.cmorfe.minesweeper.entity.User user = repository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return user;
    }

    public void signin(com.cmorfe.minesweeper.entity.User user) throws UserAlreadyExistsException {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        if (repository.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistsException();
        }

        repository.save(user);
    }

    public void signout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
    }
}
