package com.cmorfe.minesweeper.service;

import com.cmorfe.minesweeper.entity.Board;
import com.cmorfe.minesweeper.entity.Square;
import com.cmorfe.minesweeper.exception.NoteNotFoundException;
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

        int column = square.getColumn();

        int row = square.getRow();

        return repository.findByBoardAndColumnBetweenAndRowBetween(
                board,
                column - 1,
                column + 1,
                row - 1,
                row + 1
        );
    }

    public int countMinedAdjacents(Square square) {
        Board board = square.getBoard();

        int column = square.getColumn();

        int row = square.getRow();

        return repository.countByBoardAndColumnBetweenAndRowBetweenAndMinedTrue(
                board,
                column - 1,
                column + 1,
                row - 1,
                row + 1
        );
    }

    public LinkedList<Square> getGameSquares(Board board) {
        return repository.findByBoardOrderByColumnDescRowDesc(board);
    }

    public Square open(long id) {
        Square square = repository.findById(id)
                .orElseThrow(NoteNotFoundException::new);

        openSquare(square);

        return square;
    }

    public void openSquare(Square square) {
        if (square.isOpen() || square.getMark() == Square.Mark.FLAG) {
            return;
        }

        square.setOpen(true);

        repository.save(square);

        if (square.isMined()) {
            Board board = square.getBoard();

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
                .orElseThrow(NoteNotFoundException::new);

    }

}
