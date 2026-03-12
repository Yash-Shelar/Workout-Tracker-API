package fit.workout_tracker.api.error.exception;

public class WorkoutNotFoundException extends RuntimeException {

    public WorkoutNotFoundException() {
    }

    public WorkoutNotFoundException(String message) {
        super(message);
    }
    
}
