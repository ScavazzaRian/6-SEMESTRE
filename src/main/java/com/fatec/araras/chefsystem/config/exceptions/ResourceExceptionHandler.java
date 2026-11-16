package com.fatec.araras.chefsystem.config.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<StandardError> conflito(ConflitoException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new StandardError(
                Instant.now(), 409, "Conflito", e.getMessage(), request.getRequestURI()));
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

        String mensagemFinal = e.getBindingResult().getAllErrors().stream()
                .map(erro -> (erro instanceof FieldError campo ? campo.getField() + ": " : "")
                        + erro.getDefaultMessage())
                .collect(java.util.stream.Collectors.joining("; "));

        StandardError standardError = new StandardError(Instant.now(), status.value(), "Valores informados estão incorretos", mensagemFinal, request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<StandardError> jsonInvalid(
                HttpMessageNotReadableException e,
                HttpServletRequest request
        ){
            HttpStatus status = HttpStatus.BAD_REQUEST;

            StandardError standardError = new StandardError(Instant.now(), status.value(), "Requisição inválida", "Corpo da requisição inválido ou valor não reconhecido", request.getRequestURI());
            return ResponseEntity.status(status).body(standardError);
        }
}
