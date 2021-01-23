package com.cmorfe.minesweeper.repository;

import com.cmorfe.minesweeper.entity.Board;
import com.cmorfe.minesweeper.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends CrudRepository<Board, Long> {
    Optional<Board> findByIdAndUser(long id, User user);

    List<Board> findByUserAndGameState(User user, Board.GameState gameState);
}
