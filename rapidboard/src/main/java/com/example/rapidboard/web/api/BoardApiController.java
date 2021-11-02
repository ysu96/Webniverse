package com.example.rapidboard.web.api;

import com.example.rapidboard.service.BoardService;
import com.example.rapidboard.web.dto.CmDto;
import com.example.rapidboard.web.dto.board.BoardDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardApiController {
    private final BoardService boardService;

    @PostMapping("/api/board/create/{boardname}")
    public ResponseEntity<?> createBoard(@PathVariable String boardname) {
        log.info("[create board] boardName : {}", boardname);
        boardService.createBoard(boardname);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/board/delete/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId){
        log.info("[delete board] board id : {}", boardId);
        boardService.deleteBoard(boardId);
        return ResponseEntity.noContent().build();
    }
}
