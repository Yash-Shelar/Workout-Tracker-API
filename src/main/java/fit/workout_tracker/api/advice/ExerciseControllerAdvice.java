package fit.workout_tracker.api.advice;

import java.util.Map;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import fit.workout_tracker.api.controller.ExerciseController;
import fit.workout_tracker.api.error.exception.ExerciseNotFoundException;

@RestControllerAdvice(assignableTypes = ExerciseController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExerciseControllerAdvice {

    @ExceptionHandler(exception = {
        ExerciseNotFoundException.class
    })
    public ResponseEntity<Map<String, String>> notFoundExceptionHandler(Exception e) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(Map.of(
                "message", e.getMessage()
            ));
    }
}
