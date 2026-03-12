package fit.workout_tracker.api.error.exception;

public class WorkoutExerciseNotFound extends RuntimeException {

    public WorkoutExerciseNotFound() {
    }

    public WorkoutExerciseNotFound(String message) {
        super(message);
    }
    
}
