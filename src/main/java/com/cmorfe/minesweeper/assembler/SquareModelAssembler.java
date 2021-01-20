package com.cmorfe.minesweeper.assembler;

import com.cmorfe.minesweeper.entity.Board;
import com.cmorfe.minesweeper.entity.Square;
import com.cmorfe.minesweeper.service.SquareService;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class SquareModelAssembler implements RepresentationModelAssembler<Square, EntityModel<Square>> {

    private final SquareService squareService;

    public SquareModelAssembler(SquareService squareService) {
        this.squareService = squareService;
    }

    @Override
    public @NotNull EntityModel<Square> toModel(@NotNull Square square) {

        if (square.isOpen()) {
            square.setAdjacents(squareService.countMinedAdjacents(square));
        }

        if (square.getBoard().getGameState() != Board.GameState.LOST) {
            square.setMined(false);
        }

        return EntityModel.of(square);
    }
}