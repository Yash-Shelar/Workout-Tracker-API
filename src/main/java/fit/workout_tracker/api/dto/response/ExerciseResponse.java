package fit.workout_tracker.api.dto.response;

import java.util.Set;

import fit.workout_tracker.api.entity.Exercise;
import fit.workout_tracker.api.enums.ExerciseCategory;
import fit.workout_tracker.api.enums.MuscleGroup;

public record ExerciseResponse(
    Long id,
    String name,
    String description,
    Set<MuscleGroup> muscleGroups,
    ExerciseCategory category
) {
    public static ExerciseResponse from(Exercise exercise) {
        if (exercise == null)
            return null;
        var response = new ExerciseResponse(
            exercise.getId(),
            exercise.getName(),
            exercise.getDescription(),
            exercise.getMuscleGroups(),
            exercise.getCategory()
        );

        return response;
    }
}
