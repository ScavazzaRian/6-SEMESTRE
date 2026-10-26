package com.fatec.araras.chefsystem.config.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ResourceExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(
            ResourceNotFoundException e,
            HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardError standardError = new StandardError(Instant.now(), status.value(), "Recurso não encontrado", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> database(
            HttpServletRequest request
    ){
        HttpStatus status = HttpStatus.CONFLICT;

        StandardError standardError = new StandardError(Instant.now(), status.value(), "Conflito de integridade", "A operação viola uma restrição do banco de dados.", request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError>  method(
            MethodArgumentNotValidException  e,
            HttpServletRequest request
    ){
        HttpStatus status = HttpStatus.BAD_REQUEST;

        FieldError erro = e.getBindingResult().getFieldErrors().getFirst();
        String campo = erro.getField();
        String mensagem = erro.getDefaultMessage();
        String mensagemFinal = campo + " " + mensagem;

        StandardError standardError = new StandardError(Instant.now(), status.value(), "Produto já cadastrado", mensagemFinal, request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }
}
