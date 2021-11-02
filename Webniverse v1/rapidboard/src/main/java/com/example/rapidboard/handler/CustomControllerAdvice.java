package com.example.rapidboard.handler;

import com.example.rapidboard.domain.board.Board;
import com.example.rapidboard.handler.exception.CustomException;
import com.example.rapidboard.handler.exception.CustomValidationException;
import com.example.rapidboard.service.BoardService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequiredArgsConstructor
@ControllerAdvice
@Slf4j
public class CustomControllerAdvice {
    private final BoardService boardService;

    @ModelAttribute
    public void addAttributes(Model model) {
        log.info("add board list attribute");
        List<Board> boards = boardService.getBoardList();
        model.addAttribute("boards", boards);
    }

    @ExceptionHandler(CustomException.class)
    public ModelAndView exception(CustomException e){
        log.info("exception!!--------");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error/back");
        mv.setStatus(HttpStatus.BAD_REQUEST);
        mv.addObject("msg", e.getMessage());
        return mv;
    }

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView notFoundException(NotFoundException e){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error/404");
        mv.setStatus(HttpStatus.NOT_FOUND);
        mv.addObject("msg", e.getMessage());
        return mv;
    }

    @ExceptionHandler(CustomValidationException.class)
    public @ResponseBody ResponseEntity customValidationException(CustomValidationException e) {
        String result = e.getMessage();
        ResponseEntity<String> stringResponseEntity = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        return stringResponseEntity;
    }

    @ExceptionHandler(BindException.class)
    public @ResponseBody ResponseEntity processValidationError(BindException exception) {
        log.info(" api validation exception!!!!!!!!!!!!---------------");
        BindingResult bindingResult = exception.getBindingResult();

        StringBuilder msg = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            msg.append(fieldError.getDefaultMessage());
            msg.append("\n");
        }
        String result = msg.substring(0, msg.length() - 1);
        ResponseEntity<String> stringResponseEntity = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        return stringResponseEntity;
    }
}
