package com.example.rapidboard.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
//    @Query(value = "SELECT * FROM board WHERE is_deleted=0", nativeQuery = true)
//    List<Board> findAllBoard();

    List<Board> findAllByIsDeleted(int is_deleted);

    Optional<Board> findByName(String boardname);
}
