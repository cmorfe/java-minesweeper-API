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
        Board board = square.getBoard();

        if (square.isOpen() || square.getMark() == Square.Mark.FLAG || board.getGameState() != Board.GameState.ON) {
            return;
        }

        setOpenSquare(square);

        if (square.isMined()) {
            setGameLost(board);

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

        if (countNotMinedClosedSquares(board) == 0) {
            setGameWon(board);
        }
    }

    private void setGameWon(Board board) {
        board.setGameState(Board.GameState.WON);

        boardRepository.save(board);
    }

    private void setGameLost(Board board) {
        board.setGameState(Board.GameState.LOST);

        boardRepository.save(board);
    }

    private void setOpenSquare(Square square) {
        square.setOpen(true);

        squareRepository.save(square);
    }

    private int countNotMinedClosedSquares(Board board) {
        return squareRepository.countByBoardAndMinedAndOpen(board, false, false);
    }

    public void mine(Square square) {
        square.setMined(true);

        squareRepository.save(square);
    }

    public Square mark(long boardId, long id) {
        User user = userAuthService.authUser();

        Board board = boardRepository.findByIdAndUser(boardId, user)
                .orElseThrow(NotFoundException::new);

        Square square = squareRepository.findByIdAndBoard(id, board)
                .orElseThrow(NotFoundException::new);

        if (board.getGameState() != Board.GameState.ON) {
            return square;
        }

        return toggleMark(square);
    }

    @NotNull
    private Square toggleMark(Square square) {
        square.toggleMark();

        return squareRepository.save(square);
    }

}
