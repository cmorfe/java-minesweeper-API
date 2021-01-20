package com.cmorfe.minesweeper.assembler;

import com.cmorfe.minesweeper.controller.BoardController;
import com.cmorfe.minesweeper.entity.Board;
import com.cmorfe.minesweeper.service.BoardService;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BoardModelAssembler implements RepresentationModelAssembler<Board, EntityModel<Board>> {

    private final BoardService boardService;

    public BoardModelAssembler(BoardService boardService, SquareModelAssembler squareModelAssembler) {
        this.boardService = boardService;
    }

    @Override
    public @NotNull EntityModel<Board> toModel(@NotNull Board board) {

        board = boardService.gameSquares(board);

        return EntityModel.of(board,
                linkTo(methodOn(BoardController.class).show(board.getId())).withSelfRel(),
                linkTo(methodOn(BoardController.class).index()).withRel("notes"));
    }
}
