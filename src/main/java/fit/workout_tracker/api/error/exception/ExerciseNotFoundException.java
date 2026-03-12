package fit.workout_tracker.api.error.exception;

public class ExerciseNotFoundException extends RuntimeException {

    public ExerciseNotFoundException() {
    }

    public ExerciseNotFoundException(String message) {
        super(message);
    }
    
}
