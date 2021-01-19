package com.cmorfe.minesweeper.repository;

import com.cmorfe.minesweeper.entity.Board;
import com.cmorfe.minesweeper.entity.Square;
import org.springframework.data.repository.CrudRepository;

import java.util.LinkedList;
import java.util.List;

public interface SquareRepository extends CrudRepository<Square, Long> {
}