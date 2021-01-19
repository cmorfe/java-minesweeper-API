package com.cmorfe.minesweeper.entity;

import javax.persistence.*;

@Entity
@Table(name = "squares")
public class Square {

    public Square(Board board, int column, int row) {
        this.board = board;
        this.column = column;
        this.row = row;
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
    private Board board;

    @Column(nullable = false)
    private int column;

    @Column(nullable = false)
    private int row;

    @Enumerated(EnumType.STRING)
    private Mark mark;

    @Column(nullable = false)
    private boolean open;

    @Column(nullable = false)
    private boolean mined;

    private int adjacents;

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

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
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
