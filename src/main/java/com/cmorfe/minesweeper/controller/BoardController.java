package com.cmorfe.minesweeper.controller;


import com.cmorfe.minesweeper.assembler.BoardModelAssembler;
import com.cmorfe.minesweeper.entity.Board;
import com.cmorfe.minesweeper.service.BoardService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.LinkRelation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("boards")
public class BoardController {

    private final BoardService boardService;

    private final BoardModelAssembler boardModelAssembler;

    public BoardController(BoardService boardService, BoardModelAssembler boardModelAssembler) {
        this.boardService = boardService;

        this.boardModelAssembler = boardModelAssembler;
    }

    @GetMapping("{id}")
    public EntityModel<Board> show(@PathVariable Long id) {
        Board board = boardService.show(id);

        return boardModelAssembler.toModel(board);
    }

    @PostMapping("{id}")
    public ResponseEntity<?> update(@RequestBody Board newBoard, @PathVariable Long id) {
        Board board = boardService.update(id, newBoard);

        EntityModel<Board> model = boardModelAssembler.toModel(board);

        return ResponseEntity
                .accepted()
                .body(model);
    }

    @PostMapping()
    public ResponseEntity<?> store(@RequestBody Board board) {
        Board newBoard = boardService.store(board.getLength(), board.getHeight(), board.getMines());

        EntityModel<Board> model = boardModelAssembler.toModel(newBoard);

        return ResponseEntity
                .created(model.getRequiredLink(LinkRelation.of("self")).toUri())
                .body(model);
    }

    @GetMapping()
    public List<Board> index() {
        return boardService.index();
    }
}
