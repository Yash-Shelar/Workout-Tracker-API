package fit.workout_tracker.api.error.exception;

public class ScheduledWorkoutNotFound extends RuntimeException {

    public ScheduledWorkoutNotFound() {
    }

    public ScheduledWorkoutNotFound(String message) {
        super(message);
    }
    
}
