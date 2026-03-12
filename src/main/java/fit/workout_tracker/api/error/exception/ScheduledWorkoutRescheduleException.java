package fit.workout_tracker.api.error.exception;

public class ScheduledWorkoutRescheduleException extends RuntimeException {

    public ScheduledWorkoutRescheduleException() {
    }

    public ScheduledWorkoutRescheduleException(String message) {
        super(message);
    }
    
}
