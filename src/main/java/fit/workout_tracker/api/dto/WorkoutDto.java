package fit.workout_tracker.api.dto;

import java.util.List;

import fit.workout_tracker.api.validation.annotation.UniqueWorkoutExercises;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record WorkoutDto(
    @NotNull(message = "provide name for the workout")
    String name,

    @NotNull(message = "provide description for the workout")
    String description,

    @Valid
    @UniqueWorkoutExercises(message = "exercise order for workout exercises should be unique.")
    List<WorkoutExerciseDto> exercises
)
{
}
