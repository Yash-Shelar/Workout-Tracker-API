package fit.workout_tracker.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateWorkoutExerciseDto(
    @NotNull(message = "workout exercise id cannot be null")
    Long workoutExerciseId,

    Long exerciseId,

    @Min(value = 0, message = "sets cannot be negative")
    Integer sets,

    @Min(value = 0, message = "reps cannot be negative")
    Integer reps,

    @Min(value = 0, message = "weightKg cannot be negative")
    Integer weightKg,

    @Min(value = 1, message = "order of exercise should be >= 1")
    Integer exerciseOrder
) {
    
}
