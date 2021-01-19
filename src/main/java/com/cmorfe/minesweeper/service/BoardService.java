package com.cmorfe.minesweeper.service;

import com.cmorfe.minesweeper.assembler.SquareModelAssembler;
import com.cmorfe.minesweeper.entity.Board;
import com.cmorfe.minesweeper.entity.Square;
import com.cmorfe.minesweeper.entity.User;
import com.cmorfe.minesweeper.exception.NotFoundException;
import com.cmorfe.minesweeper.repository.BoardRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    private final SquareService squareService;

    private final SquareModelAssembler squareModelAssembler;

    public BoardService(BoardRepository boardRepository, SquareService squareService, SquareModelAssembler squareModelAssembler) {
        this.boardRepository = boardRepository;
        this.squareService = squareService;
        this.squareModelAssembler = squareModelAssembler;
    }

    public Board store(User user, int columns, int rows, int mines) {
        Board board = new Board(user, columns, rows, mines);

        boardRepository.save(board);

        List<Square> squares = new ArrayList<>();

        for (int column = 0; column < columns; column++) {
            for (int row = 0; row < rows; row++) {
                Square square = new Square(board, column, row);

                squares.add(square);

                squareService.store(square);
            }
        }

        Collections.shuffle(squares);

        squares.subList(0, mines).forEach(squareService::mine);

        return board;
    }

    public Board gameSquares(Board board) {
        ArrayList<List<EntityModel<Square>>> gameSquares = new ArrayList<>();

        LinkedList<Square> squares = squareService.getGameSquares(board);

        if (squares.size() != board.getColumns() * board.getRows()) {
            throw new RuntimeException("Size mismatch");
        }

        for (int column = 0; column < board.getColumns(); column++) {
            ArrayList<EntityModel<Square>> squareRow = new ArrayList<>();

            for (int row = 0; row < board.getRows(); row++) {

                squareRow.add(squareModelAssembler.toModel(squares.pop()));
            }

            gameSquares.add(squareRow);
        }

        board.setGameSquares(gameSquares);

        return board;
    }

    public Board show(Long id) {
            return boardRepository.findById(id)
                    .orElseThrow(NotFoundException::new);
    }

    public List<Board> index() {
        return (List<Board>) boardRepository.findAll();
    }

}
