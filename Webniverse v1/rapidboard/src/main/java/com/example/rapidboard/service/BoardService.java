package com.example.rapidboard.service;

import com.example.rapidboard.domain.board.Board;
import com.example.rapidboard.domain.board.BoardRepository;
import com.example.rapidboard.domain.comment.Comment;
import com.example.rapidboard.domain.post.Post;
import com.example.rapidboard.handler.exception.CustomException;
import com.example.rapidboard.handler.exception.CustomValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public String getBoardName(Long boardId){
        return boardRepository.findById(boardId).get().getName();
    }

    @Transactional
    public List<Board> getBoardList(){
        return boardRepository.findAllByIsDeleted(0);
    }

    @Transactional
    public void createBoard(String boardname){
        log.info("Create Board : {}", boardname);
        String boardnamePattern = "^[ㄱ-ㅎㅏ-ㅣ가-힣A-Za-z0-9+]*$";
        if (!Pattern.matches(boardnamePattern, boardname)) {
            throw new CustomValidationException("Boardname must be in Korean, English, and Nubmer.");
        }

        boardRepository.save(Board.createBoard(boardname));
    }

    @Transactional
    public void deleteBoard(Long boardId){
        log.info("Delete Board : {}", boardId);
        Board boardEntity = findById(boardId);
        boardEntity.deleteBoard();
    }

    @Transactional
    public Board findById(Long boardId) {
        Board boardEntity = boardRepository.findById(boardId).orElseThrow(()->{
            throw new CustomException("Board dose not exist.");
        });
        if (boardEntity.getIsDeleted() == 1){
            throw new CustomException("Board dose not exist.");
        }
        return boardEntity;
    }
}
