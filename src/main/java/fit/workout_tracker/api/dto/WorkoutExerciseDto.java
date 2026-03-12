package fit.workout_tracker.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record WorkoutExerciseDto(
    @NotNull(message = "provide exerciseId.")
    Long exerciseId,

    @NotNull(message = "provide value for sets")
    @Min(value = 0, message = "sets cannot be negative")
    Integer sets,

    @NotNull(message = "provide value for reps")
    @Min(value = 0, message = "reps cannot be negative")
    Integer reps,

    @NotNull(message = "provide value for weightKg")
    @Min(value = 0, message = "weightKg cannot be negative")
    Integer weightKg,

    @NotNull(message = "provide order of exercise")
    @Min(value = 1, message = "order of exercise should be >= 1")
    Integer exerciseOrder
)
{
}
