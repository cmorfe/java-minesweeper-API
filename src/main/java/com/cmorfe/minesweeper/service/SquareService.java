package com.cmorfe.minesweeper.service;

import com.cmorfe.minesweeper.entity.Board;
import com.cmorfe.minesweeper.entity.Square;
import com.cmorfe.minesweeper.entity.User;
import com.cmorfe.minesweeper.exception.NotFoundException;
import com.cmorfe.minesweeper.repository.BoardRepository;
import com.cmorfe.minesweeper.repository.SquareRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class SquareService {
    private final SquareRepository squareRepository;

    private final UserAuthService userAuthService;

    private final BoardRepository boardRepository;

    public SquareService(SquareRepository squareRepository, UserAuthService userAuthService, BoardRepository boardRepository) {
        this.squareRepository = squareRepository;

        this.userAuthService = userAuthService;

        this.boardRepository = boardRepository;
    }

    public void store(Square square) {
        squareRepository.save(square);
    }

    public List<Square> adjacents(Square square) {
        Board board = square.getBoard();

        int x = square.getX();

        int y = square.getY();

        return squareRepository.findByBoardAndXBetweenAndYBetween(board, x - 1, x + 1, y - 1, y + 1);
    }

    public int countMinedAdjacents(Square square) {
        Board board = square.getBoard();

        int x = square.getX();

        int y = square.getY();

        return squareRepository.countByBoardAndXBetweenAndYBetweenAndMinedTrue(board, x - 1, x + 1, y - 1, y + 1);
    }

    public LinkedList<Square> getGameSquares(Board board) {
        return squareRepository.findByBoardOrderByXAscYAsc(board);
    }

    public Square open(long boardId, long id) {
        User user = userAuthService.authUser();

        Board board = boardRepository.findByIdAndUser(boardId, user)
                .orElseThrow(NotFoundException::new);

        Square square = squareRepository.findByIdAndBoard(id, board)
                .orElseThrow(NotFoundException::new);

        openSquare(square);

        return square;
    }

    public void openSquare(Square square) {
        if (square.isOpen() || square.getMark() == Square.Mark.FLAG) {
            return;
        }

        square.setOpen(true);

        squareRepository.save(square);

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
        return squareRepository.countByBoardAndMinedAndOpen(board, false, true);
    }

    public void mine(Square square) {
        square.setMined(true);

        squareRepository.save(square);
    }

    public Square mark(long boardId, long id) {
        User user = userAuthService.authUser();

        Board board = boardRepository.findByIdAndUser(boardId, user)
                .orElseThrow(NotFoundException::new);

        return squareRepository.findByIdAndBoard(id, board)
                .map(this::toggleMark)
                .orElseThrow(NotFoundException::new);

    }

    @NotNull
    private Square toggleMark(Square square) {
        square.toggleMark();

        return squareRepository.save(square);
    }

}
