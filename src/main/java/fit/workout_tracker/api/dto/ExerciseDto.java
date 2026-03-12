package fit.workout_tracker.api.dto;

import java.util.Set;

import fit.workout_tracker.api.entity.Exercise;
import fit.workout_tracker.api.enums.ExerciseCategory;
import fit.workout_tracker.api.enums.MuscleGroup;

public record ExerciseDto(
    Long id,
    String name,
    String description,
    ExerciseCategory category,
    Set<MuscleGroup> muscleGroups
) {
    
    public static ExerciseDto from(Exercise exercise) {
        if (exercise == null)
            return null;
        return new ExerciseDto(
            exercise.getId(),
            exercise.getName(),
            exercise.getDescription(),
            exercise.getCategory(),
            exercise.getMuscleGroups()
        );
    }
}
