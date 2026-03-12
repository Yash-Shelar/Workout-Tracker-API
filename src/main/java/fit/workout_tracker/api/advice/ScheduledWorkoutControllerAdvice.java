package fit.workout_tracker.api.advice;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import fit.workout_tracker.api.error.exception.ScheduleWorkoutException;
import fit.workout_tracker.api.error.exception.ScheduledWorkoutNotFound;
import fit.workout_tracker.api.error.exception.ScheduledWorkoutRescheduleException;
import fit.workout_tracker.api.error.exception.WorkoutNotFoundException;

@RestControllerAdvice
public class ScheduledWorkoutControllerAdvice {
    
    @ExceptionHandler(exception = { 
        ScheduledWorkoutNotFound.class,
        WorkoutNotFoundException.class
    })
    public ResponseEntity<Map<String, String>> handleNotFoundException(Exception e) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(exception = {
        ScheduledWorkoutRescheduleException.class,
        ScheduleWorkoutException.class
    })
    public ResponseEntity<Map<String, String>> handleBadRequestException(Exception e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of("message", e.getMessage()));
    }
}
