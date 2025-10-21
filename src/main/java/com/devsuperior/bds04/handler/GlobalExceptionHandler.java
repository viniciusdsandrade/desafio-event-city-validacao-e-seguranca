package com.devsuperior.bds04.handler;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Schema(description = "Classe responsável por tratar exceções globalmente na aplicação.")
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<List<ErrorDetails>> handleBadRequestException(
            BadRequestException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(
                now(), exception.getMessage(), webRequest.getDescription(false), "BAD_REQUEST");
        return new ResponseEntity<>(List.of(errorDetails), BAD_REQUEST);
    }

    // ⬇️ ALTERADO AQUI
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @Schema(description = "Manipula erros de validação e retorna 422 com a lista de campos inválidos.")
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException exception, WebRequest request) {

        List<Map<String, String>> items = new ArrayList<>();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            Map<String, String> item = new HashMap<>();
            item.put("fieldName", error.getField());
            item.put("message", error.getDefaultMessage());
            items.add(item);
        }

        Map<String, Object> body = new HashMap<>();
        body.put("errors", items);

        return ResponseEntity.status(UNPROCESSABLE_ENTITY).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<List<ErrorDetails>> handleIllegalArgumentException(
            IllegalArgumentException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(
                now(), exception.getMessage(), webRequest.getDescription(false), "INVALID_ARGUMENT");
        return new ResponseEntity<>(List.of(errorDetails), BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<List<ErrorDetails>> handleEntityNotFoundException(
            EntityNotFoundException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(
                now(), exception.getMessage(), webRequest.getDescription(false), "RESOURCE_NOT_FOUND");
        return new ResponseEntity<>(List.of(errorDetails), NOT_FOUND);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<List<ErrorDetails>> handleUnsupportedMediaTypeException(
            HttpMediaTypeNotSupportedException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(
                now(), exception.getMessage(), webRequest.getDescription(false), "UNSUPPORTED_MEDIA_TYPE");
        return new ResponseEntity<>(List.of(errorDetails), UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<List<ErrorDetails>> handleGlobalException(
            Exception exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(
                now(), exception.getMessage(), webRequest.getDescription(false), "INTERNAL_SERVER_ERROR");
        return new ResponseEntity<>(List.of(errorDetails), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<List<ErrorDetails>> handleNotImplementedException(
            UnsupportedOperationException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(
                now(), exception.getMessage(), webRequest.getDescription(false), "NOT_IMPLEMENTED");
        return new ResponseEntity<>(List.of(errorDetails), NOT_IMPLEMENTED);
    }
}