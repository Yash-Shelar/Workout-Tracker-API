package fit.workout_tracker.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import fit.workout_tracker.api.entity.ScheduledWorkout;
import fit.workout_tracker.api.entity.User;
import fit.workout_tracker.api.entity.Workout;
import fit.workout_tracker.api.enums.WorkoutStatus;

public interface ScheduledWorkoutRepository extends JpaRepository<ScheduledWorkout, Long> {

    @Query("""
        SELECT sw FROM ScheduledWorkout sw
        WHERE sw.user = :user""")
    List<ScheduledWorkout> findByUser(User user, Pageable pageable);

    @Query("""
        SELECT sw FROM ScheduledWorkout sw
        WHERE sw.user = :user
        AND sw.status = :status""")
    List<ScheduledWorkout> findAllByUserAndStatusPageable(WorkoutStatus status, User user, Pageable pageable);

    @Query("""
        SELECT sw FROM ScheduledWorkout sw
        WHERE sw.id = :id AND sw.user = :user""")
    Optional<ScheduledWorkout> findByIdAndUser(Long id, User user);
    
    @Modifying
    @Query("""
        DELETE FROM ScheduledWorkout sw
        WHERE sw.workout = :workout""")
    void deleteScheduledWorkoutsForWorkout(Workout workout);
}
