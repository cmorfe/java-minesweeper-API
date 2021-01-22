package com.cmorfe.minesweeper.service;

import com.cmorfe.minesweeper.entity.Board;
import com.cmorfe.minesweeper.entity.Square;
import com.cmorfe.minesweeper.exception.NotFoundException;
import com.cmorfe.minesweeper.repository.BoardRepository;
import com.cmorfe.minesweeper.repository.SquareRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class SquareService {
    private final SquareRepository repository;

    private final BoardRepository boardRepository;

    public SquareService(SquareRepository repository, BoardRepository boardRepository) {
        this.repository = repository;
        this.boardRepository = boardRepository;
    }

    public Square store(Square square) {
        return repository.save(square);
    }

    public List<Square> adjacents(Square square) {
        Board board = square.getBoard();

        int column = square.getX();

        int row = square.getY();

        return repository.findByBoardAndXBetweenAndYBetween(
                board,
                column - 1,
                column + 1,
                row - 1,
                row + 1
        );
    }

    public int countMinedAdjacents(Square square) {
        Board board = square.getBoard();

        int column = square.getX();

        int row = square.getY();

        return repository.countByBoardAndXBetweenAndYBetweenAndMinedTrue(
                board,
                column - 1,
                column + 1,
                row - 1,
                row + 1
        );
    }

    public LinkedList<Square> getGameSquares(Board board) {
        return repository.findByBoardOrderByXAscYAsc(board);
    }

    public Square open(long id) {
        Square square = repository.findById(id)
                .orElseThrow(NotFoundException::new);

        openSquare(square);

        return square;
    }

    public void openSquare(Square square) {
        if (square.isOpen() || square.getMark() == Square.Mark.FLAG) {
            return;
        }

        square.setOpen(true);

        repository.save(square);

        Board board = square.getBoard();

        if (square.isMined()) {

            board.setGameState(Board.GameState.LOST);

            boardRepository.save(board);

            return;
        }

        if (countMinedAdjacents(square) == 0) {
            List<Square> adjacents = adjacents(square);

            adjacents.forEach(adjacent -> {
                if (adjacent.getId() != square.getId()) {
                    openSquare(adjacent);
                }
            });
        }

        if (countNotMinedOpenSquares(board) == 0) {
            board.setGameState(Board.GameState.WON);

            boardRepository.save(board);
        }
    }

    private int countNotMinedOpenSquares(Board board) {
        return repository.countByBoardAndMinedAndOpen(board, false, true);
    }


    public void mine(Square square) {
        square.setMined(true);

        repository.save(square);
    }

    public Square mark(long id) {
        return repository.findById(id)
                .map(square -> {
                    square.toggleMark();
                    return repository.save(square);
                })
                .orElseThrow(NotFoundException::new);

    }

}
