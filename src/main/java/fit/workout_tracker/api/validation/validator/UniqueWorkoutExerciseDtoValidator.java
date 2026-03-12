package fit.workout_tracker.api.validation.validator;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import fit.workout_tracker.api.dto.WorkoutExerciseDto;
import fit.workout_tracker.api.validation.annotation.UniqueWorkoutExercises;

public class UniqueWorkoutExerciseDtoValidator implements ConstraintValidator<UniqueWorkoutExercises, Collection<? extends WorkoutExerciseDto>> {

    @Override
    public boolean isValid(
        Collection<? extends WorkoutExerciseDto> collection,
        ConstraintValidatorContext context
    ) {

        if (collection == null)
            return true;

        Set<Integer> exerciseOrders = collection.stream()
            .map(workoutExerciseDto -> workoutExerciseDto.exerciseOrder())
            .collect(Collectors.toSet());

        return exerciseOrders.size() == collection.size();
    }
}
