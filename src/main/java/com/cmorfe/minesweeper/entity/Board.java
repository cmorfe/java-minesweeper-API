package com.cmorfe.minesweeper.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.hateoas.EntityModel;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "boards")
public class Board {

    public Board() {
    }

    public enum GameState {
        ON, WON, LOST
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private int columns;

    @Column(nullable = false)
    private int rows;

    @Column(nullable = false)
    private int mines;

    @Enumerated(EnumType.STRING)
    private GameState gameState;

    @Column(nullable = false)
    private int time;

    @OneToMany(mappedBy = "board")
    @JsonIgnore
    private List<Square> squares;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private ArrayList<List<EntityModel<Square>>> gameSquares;

    public Board(User user, int columns, int rows, int mines) {
        this.user = user;
        this.columns = columns;
        this.rows = rows;
        this.mines = mines;
        this.time = 0;
        this.gameState = GameState.ON;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int height) {
        this.rows = height;
    }

    public int getMines() {
        return mines;
    }

    public void setMines(int mines) {
        this.mines = mines;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public ArrayList<List<EntityModel<Square>>> getGameSquares() {
        return gameSquares;
    }

    public void setGameSquares(ArrayList<List<EntityModel<Square>>> gameSquares) {
        this.gameSquares = gameSquares;
    }

    public List<Square> getSquares() {
        return squares;
    }

    public void setSquares(List<Square> squares) {
        this.squares = squares;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
