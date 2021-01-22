package com.cmorfe.minesweeper.controller;


import com.cmorfe.minesweeper.assembler.BoardModelAssembler;
import com.cmorfe.minesweeper.entity.Board;
import com.cmorfe.minesweeper.entity.User;
import com.cmorfe.minesweeper.service.BoardService;
import com.cmorfe.minesweeper.service.UserAuthService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.LinkRelation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("boards")
public class BoardController {

    private final UserAuthService userAuthService;

    private final BoardService service;

    private final BoardModelAssembler assembler;

    public BoardController(UserAuthService userAuthService, BoardService service, BoardModelAssembler assembler) {
        this.userAuthService = userAuthService;

        this.service = service;

        this.assembler = assembler;
    }

    @GetMapping("{id}")
    public EntityModel<Board> show(@PathVariable Long id) {
        Board board = service.show(id);

        return assembler.toModel(board);
    }

    @PostMapping("{id}")
    public ResponseEntity<?> update(@RequestBody Board newBoard, @PathVariable Long id) {
        Board board = service.update(id, newBoard);

        EntityModel<Board> model = assembler.toModel(board);

        return ResponseEntity
                .accepted()
                .body(model);
    }

    @PostMapping()
    public ResponseEntity<?> store(@RequestBody Board board) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userAuthService.findByUsername(auth.getName());

        Board newBoard = service.store(user, board.getLength(), board.getHeight(), board.getMines());

        EntityModel<Board> model = assembler.toModel(newBoard);

        return ResponseEntity
                .created(model.getRequiredLink(LinkRelation.of("self")).toUri())
                .body(model);
    }

    @GetMapping()
    public CollectionModel<EntityModel<Board>> index() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userAuthService.findByUsername(auth.getName());

        List<EntityModel<Board>> boards = service.index(user).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(boards,
                linkTo(methodOn(BoardController.class).index()).withSelfRel());
    }
}
