package com.fitness.coachiq.Config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> validation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        return ResponseEntity.badRequest().body(Map.of("timestamp", Instant.now(), "status", 400, "error", "Validation failed", "path", req.getRequestURI()));
    }
    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<?> denied(AccessDeniedException ex, HttpServletRequest req) {
        return ResponseEntity.status(403).body(Map.of("timestamp", Instant.now(), "status", 403, "error", ex.getMessage(), "path", req.getRequestURI()));
    }
    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<?> runtime(RuntimeException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("timestamp", Instant.now(), "status", 400, "error", ex.getMessage() == null ? "Request failed" : ex.getMessage(), "path", req.getRequestURI()));
    }
    @ExceptionHandler(Exception.class)
    ResponseEntity<?> generic(Exception ex, HttpServletRequest req) {
        return ResponseEntity.status(500).body(Map.of("timestamp", Instant.now(), "status", 500, "error", "Internal server error", "path", req.getRequestURI()));
    }
}
