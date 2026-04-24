package dev.jgregorio.demo.data.infrastructure.api.exception;

import dev.jgregorio.demo.data.domain.exception.ResourceNotFoundException;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException e) {
        log.warn("Resource not found: {}", e.getMessage());
        return createProblemDetail(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                "Resource Not Found",
                "https://example.com/probs/resource-not-found");
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception e) {
        log.error("An unexpected error occurred", e);
        return createProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                "Internal Server Error",
                "https://example.com/probs/internal-server-error");
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        log.warn("Validation failed: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        ProblemDetail problemDetail =
                createProblemDetail(
                        HttpStatus.BAD_REQUEST,
                        "Validation failed",
                        "Bad Request",
                        "https://example.com/probs/bad-request");
        problemDetail.setProperty("errors", errors);
        return createResponseEntity(problemDetail, headers, status, request);
    }

    private ProblemDetail createProblemDetail(
            HttpStatus status, String detail, String title, String type) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setType(URI.create(type));
        return problemDetail;
    }
}
