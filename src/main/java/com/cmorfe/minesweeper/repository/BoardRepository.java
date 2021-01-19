package com.cmorfe.minesweeper.repository;

import com.cmorfe.minesweeper.entity.Board;
import org.springframework.data.repository.CrudRepository;

public interface BoardRepository extends CrudRepository<Board, Long> {
}
