package com.mavs.demopark.web.controller.exception;

import com.mavs.demopark.exception.UsernameUniqueViolationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroMessage> MethodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                                       HttpServletRequest request,
                                                                       BindingResult result){
        log.error("Api Error - ",ex);
        return ResponseEntity
              .status(HttpStatus.UNPROCESSABLE_ENTITY)
              .contentType(MediaType.APPLICATION_JSON)
              .body(new ErroMessage(request ,HttpStatus.UNPROCESSABLE_ENTITY,"campo(s) invalido(s)",result ));
    }

    @ExceptionHandler(UsernameUniqueViolationException.class)
    public ResponseEntity<ErroMessage> UsernameUniqueViolationException(RuntimeException ex,HttpServletRequest request){

        log.error("Api Error - ",ex);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErroMessage(request ,HttpStatus.CONFLICT, ex.getMessage()));
    }
}
