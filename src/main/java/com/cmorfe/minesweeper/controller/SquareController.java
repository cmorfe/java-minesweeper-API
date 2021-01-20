package com.cmorfe.minesweeper.controller;


import com.cmorfe.minesweeper.assembler.SquareModelAssembler;
import com.cmorfe.minesweeper.entity.Square;

import com.cmorfe.minesweeper.service.SquareService;
import org.springframework.hateoas.EntityModel;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("boards/{board_id}/squares")
public class SquareController {

    private final SquareService service;

    private final SquareModelAssembler assembler;

    public SquareController(SquareService service, SquareModelAssembler assembler) {
        this.service = service;

        this.assembler = assembler;
    }

    @PostMapping("{id}/mark")
    public ResponseEntity<?> mark(@PathVariable long board_id, @PathVariable Long id) {
        Square square = service.mark(id);

        EntityModel<Square> entityModel = assembler.toModel(square);

        return ResponseEntity
                .accepted()
                .body(entityModel);
    }

    @PostMapping("{id}/open")
    public ResponseEntity<?> open(@PathVariable long board_id, @PathVariable Long id) {
        Square square = service.open(id);

        EntityModel<Square> entityModel = assembler.toModel(square);

        return ResponseEntity
                .accepted()
                .body(entityModel);
    }
}
