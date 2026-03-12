package fit.workout_tracker.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fit.workout_tracker.api.entity.User;
import fit.workout_tracker.api.entity.Workout;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
 
    @Query(workoutByUser)
    @EntityGraph(value = "wwe_entity_graph")
    List<Workout> findAllByUserEager(User user);

    @Query(workoutByUser)
    List<Workout> findAllByUser(User user);

    @Query(workoutByIdAndUser)
    @EntityGraph(value = "wwe_entity_graph")
    Optional<Workout> findByIdAndUserEager(Long id, User user);

    @Query(workoutByIdAndUser)
    Optional<Workout> findByIdAndUser(Long id, User user);

    static final String workoutByUser = """
        SELECT w FROM Workout w
        WHERE w.user = :user""";

    static final String workoutByIdAndUser = """
        SELECT w FROM Workout w
        WHERE w.id = :id AND w.user = :user""";
}
