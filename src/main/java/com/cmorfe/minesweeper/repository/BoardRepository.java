package com.cmorfe.minesweeper.repository;

import com.cmorfe.minesweeper.entity.Board;
import com.cmorfe.minesweeper.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BoardRepository extends CrudRepository<Board, Long> {
    List<Board> findByUserAndGameState(
            User user,
            Board.GameState gameState
    );
}
