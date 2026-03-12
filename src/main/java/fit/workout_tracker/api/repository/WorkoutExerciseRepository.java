package fit.workout_tracker.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import fit.workout_tracker.api.entity.Workout;
import fit.workout_tracker.api.entity.WorkoutExercise;

public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, Long> {
    
    @Query("""
        SELECT
            CASE WHEN COUNT(we) > 0
                THEN TRUE
                ELSE FALSE
            END
        FROM WorkoutExercise we
        WHERE we.workout = :workout
        AND we.exerciseOrder = :exerciseOrder""")
    boolean existByExerciseOrder(int exerciseOrder, Workout workout);

    @Modifying
    @Query("""
        DELETE FROM WorkoutExercise we
        WHERE we.workout = :workout""")
    void deleteWorkoutExercisesForWorkout(Workout workout);
}
