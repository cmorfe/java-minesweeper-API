package com.cmorfe.minesweeper.controller;


import com.cmorfe.minesweeper.assembler.SquareModelAssembler;
import com.cmorfe.minesweeper.entity.Square;
import com.cmorfe.minesweeper.entity.User;
import com.cmorfe.minesweeper.exception.NotFoundException;
import com.cmorfe.minesweeper.repository.UserRepository;
import com.cmorfe.minesweeper.service.SquareService;
import com.cmorfe.minesweeper.service.UserAuthService;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("boards/{board_id}/squares")
public class SquareController {

    private final SquareService squareService;

    private final SquareModelAssembler squareModelAssembler;

    public SquareController(SquareService squareService, UserAuthService userAuthService, SquareModelAssembler squareModelAssembler) {
        this.squareService = squareService;

        this.squareModelAssembler = squareModelAssembler;
    }

    @PostMapping("{id}/mark")
    public ResponseEntity<?> mark(@PathVariable long board_id, @PathVariable Long id) {
        Square square = squareService.mark(board_id, id);

        EntityModel<Square> model = squareModelAssembler.toModel(square);

        return ResponseEntity
                .accepted()
                .body(model);
    }

    @PostMapping("{id}/open")
    public ResponseEntity<?> open(@PathVariable long board_id, @PathVariable Long id) {
        Square square = squareService.open(board_id, id);

        EntityModel<Square> model = squareModelAssembler.toModel(square);

        return ResponseEntity
                .accepted()
                .body(model);
    }
}
