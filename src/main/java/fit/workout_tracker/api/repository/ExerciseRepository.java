package fit.workout_tracker.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fit.workout_tracker.api.entity.Exercise;
import fit.workout_tracker.api.enums.ExerciseCategory;
import fit.workout_tracker.api.enums.MuscleGroup;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    @EntityGraph(value = "emg_entity_graph")
    @Query("""
        SELECT e FROM Exercise e
        WHERE e.id = :id""")
    Optional<Exercise> findByIdEager(Long id);

    @Query("""
        SELECT e FROM Exercise e""")
    List<Exercise> findAllPageable(Pageable pageable);

    @Query("""
        SELECT e FROM Exercise e
        WHERE e.category = :exerciseCategory""")
    List<Exercise> findByCategory(ExerciseCategory exerciseCategory);

    @Query("""
        SELECT e FROM Exercise e
        WHERE :muscleGroup MEMBER OF e.muscleGroups""")
    List<Exercise> findByMuscleGroup(MuscleGroup muscleGroup);
}
