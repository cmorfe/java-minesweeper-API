package com.cmorfe.minesweeper.service;

import com.cmorfe.minesweeper.assembler.SquareModelAssembler;
import com.cmorfe.minesweeper.entity.Board;
import com.cmorfe.minesweeper.entity.Square;
import com.cmorfe.minesweeper.entity.User;
import com.cmorfe.minesweeper.exception.NotFoundException;
import com.cmorfe.minesweeper.repository.BoardRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    private final SquareService squareService;

    private final UserAuthService userAuthService;

    private final SquareModelAssembler squareModelAssembler;

    public BoardService(
            BoardRepository boardRepository,
            SquareService squareService,
            UserAuthService userAuthService,
            SquareModelAssembler squareModelAssembler
    ) {
        this.boardRepository = boardRepository;

        this.squareService = squareService;

        this.userAuthService = userAuthService;

        this.squareModelAssembler = squareModelAssembler;
    }

    public Board store(int length, int height, int mines) {
        User user = userAuthService.authUser();

        Board board = new Board(user, length, height, mines);

        boardRepository.save(board);

        List<Square> squares = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < height; j++) {
                storeSquare(squares, new Square(board, i, j));
            }
        }

        Collections.shuffle(squares);

        squares.subList(0, mines).forEach(squareService::mine);

        return board;
    }

    private void storeSquare(List<Square> squares, Square square) {
        squares.add(square);

        squareService.store(square);
    }

    public Board update(long id, Board newBoard) {
        User user = userAuthService.authUser();

        return boardRepository.findByIdAndUser(id, user)
                .map(board -> setTime(newBoard, board))
                .orElseThrow(NotFoundException::new);

    }

    @NotNull
    private Board setTime(Board newBoard, Board board) {
        board.setTime(newBoard.getTime());
        return boardRepository.save(board);
    }

    public Board gameSquares(Board board) {
        ArrayList<List<EntityModel<Square>>> gameSquares = new ArrayList<>();

        LinkedList<Square> squares = squareService.getGameSquares(board);

        if (squares.size() != board.getLength() * board.getHeight()) {
            throw new RuntimeException("Size mismatch");
        }

        for (int length = 0; length < board.getLength(); length++) {
            ArrayList<EntityModel<Square>> squareRow = new ArrayList<>();

            for (int height = 0; height < board.getHeight(); height++) {

                squareRow.add(squareModelAssembler.toModel(squares.pop()));
            }

            gameSquares.add(squareRow);
        }

        board.setGameSquares(gameSquares);

        return board;
    }

    public Board show(Long id) {
        User user = userAuthService.authUser();

        return boardRepository.findByIdAndUser(id, user)
                .orElseThrow(NotFoundException::new);
    }

    public List<Board> index() {
        User user = userAuthService.authUser();

        return boardRepository.findByUserAndGameState(user, Board.GameState.ON);
    }
}
