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

    public Board store(User user, int length, int height, int mines) {
        Board board = new Board(user, length, height, mines);

        boardRepository.save(board);

        List<Square> squares = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < height; j++) {
                Square square = new Square(board, i, j);

                squares.add(square);

                squareService.store(square);
            }
        }

        Collections.shuffle(squares);

        squares.subList(0, mines).forEach(squareService::mine);

        return board;
    }

    public Board update(long id, Board newBoard) {
        return boardRepository.findById(id)
                .map(board -> {
                    board.setTime(newBoard.getTime());
                    return boardRepository.save(board);
                })
                .orElseThrow(NotFoundException::new);

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
            return boardRepository.findById(id)
                    .orElseThrow(NotFoundException::new);
    }

    public List<Board> index(User user) {
        return (List<Board>) boardRepository.findByUserAndGameState(user, Board.GameState.ON);
    }

}
