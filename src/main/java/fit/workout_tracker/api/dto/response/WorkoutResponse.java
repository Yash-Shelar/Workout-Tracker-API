package fit.workout_tracker.api.dto.response;

import java.util.List;

import fit.workout_tracker.api.entity.Workout;

public record WorkoutResponse(
    Long id,
    String name,
    String description,
    List<WorkoutExerciseResponse> workoutExercises
) {
    public static WorkoutResponse from(Workout workout) {
        if (workout == null)
            return null;
        
        var workoutExerciseResponses = workout.getWorkoutExercises().stream()
                .map(WorkoutExerciseResponse::from)
                .toList();

        return new WorkoutResponse(
            workout.getId(),
            workout.getName(),
            workout.getDescription(),
            workoutExerciseResponses
        );
    }
}
