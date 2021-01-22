package com.cmorfe.minesweeper.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "squares")
public class Square {

    public Square(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
        this.mark = Mark.NONE;
        this.open = false;
        this.mined = false;
    }

    public Square() {
    }

    public enum Mark {
        NONE, FLAG, QUESTION
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="board_id", nullable=false)
    @JsonIgnore
    private Board board;

    @Column(nullable = false)
    private int x;

    @Column(nullable = false)
    private int y;

    @Enumerated(EnumType.STRING)
    private Mark mark;

    @Column(nullable = false)
    private boolean open;

    @Column(nullable = false)
    private boolean mined;

    @Transient
    private int adjacents;

    @Transient
    private boolean shouldReload;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Mark getMark() {
        return mark;
    }

    public void setMark(Mark mark) {
        this.mark = mark;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isMined() {
        return mined;
    }

    public void setMined(boolean mined) {
        this.mined = mined;
    }

    public int getAdjacents() {
        return adjacents;
    }

    public void setAdjacents(int adjacents) {
        this.adjacents = adjacents;
    }

    public boolean isShouldReload() {
        return shouldReload;
    }

    public void setShouldReload(boolean shouldReload) {
        this.shouldReload = shouldReload;
    }

    public void toggleMark() {
        switch (mark) {
            case NONE:
                this.mark = Mark.FLAG;
                break;
            case FLAG:
                this.mark = Mark.QUESTION;
                break;
            case QUESTION:
                this.mark = Mark.NONE;
                break;
        }
    }
}
