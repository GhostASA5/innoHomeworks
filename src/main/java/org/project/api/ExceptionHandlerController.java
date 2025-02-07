package org.project.api;

import lombok.extern.slf4j.Slf4j;
import org.project.exception.CourseNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    @ExceptionHandler(ClassNotFoundException.class)
    public ResponseEntity<ErrorResponse> dialogNotFound(CourseNotFoundException ex) {
        log.error("Ошибка при попытке получить курс", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getLocalizedMessage()));
    }
}
