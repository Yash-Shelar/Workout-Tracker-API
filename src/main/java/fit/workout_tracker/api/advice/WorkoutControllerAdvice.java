package fit.workout_tracker.api.advice;

import java.util.Map;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import fit.workout_tracker.api.controller.WorkoutController;
import fit.workout_tracker.api.error.exception.ExerciseNotFoundException;
import fit.workout_tracker.api.error.exception.WorkoutExerciseOrderException;
import fit.workout_tracker.api.error.exception.WorkoutNotFoundException;

@RestControllerAdvice(assignableTypes = WorkoutController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WorkoutControllerAdvice {
    
    @ExceptionHandler(exception = {
        WorkoutNotFoundException.class,
        ExerciseNotFoundException.class
    })
    public ResponseEntity<Map<String, String>> entityNotFoundExceptionHandler(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(Map.of(
                "message", ex.getMessage()
            ));
    }

    @ExceptionHandler(exception = {
        WorkoutExerciseOrderException.class
    })
    public ResponseEntity<Map<String, String>> repeatedWorkoutExerciseOrder(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of(
                "message", ex.getMessage()
            ));
    }
}
