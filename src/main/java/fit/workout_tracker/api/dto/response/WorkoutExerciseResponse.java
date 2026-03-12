package fit.workout_tracker.api.dto.response;

import fit.workout_tracker.api.entity.WorkoutExercise;

public record WorkoutExerciseResponse(
    Long id,
    ExerciseResponse exercise,
    int sets,
    int reps,
    int weightKg,
    int exerciseOrder
) {
    public static WorkoutExerciseResponse from(
        WorkoutExercise workoutExercise
    ) {
        if (workoutExercise == null)
            return null;

        return new WorkoutExerciseResponse(
            workoutExercise.getId(),
            ExerciseResponse.from(workoutExercise.getExercise()),
            workoutExercise.getSets(),
            workoutExercise.getReps(),
            workoutExercise.getWeightKg(),
            workoutExercise.getExerciseOrder()
        );
    }
}
