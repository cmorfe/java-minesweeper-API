package com.cmorfe.minesweeper.repository;

import com.cmorfe.minesweeper.entity.Board;
import com.cmorfe.minesweeper.entity.Square;
import org.springframework.data.repository.CrudRepository;

import java.util.LinkedList;
import java.util.List;

public interface SquareRepository extends CrudRepository<Square, Long> {
    List<Square> findByBoardAndXBetweenAndYBetween(
            Board board,
            int x0,
            int x1,
            int y0,
            int y1
    );

    int countByBoardAndXBetweenAndYBetweenAndMinedTrue(
            Board board,
            int x0,
            int x1,
            int y0,
            int y1
    );

    LinkedList<Square> findByBoardOrderByXAscYAsc(Board board);

    int countByBoardAndMinedAndOpen(Board board, boolean mined, boolean open);
}
