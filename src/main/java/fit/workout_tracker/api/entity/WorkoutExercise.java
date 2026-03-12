package fit.workout_tracker.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WorkoutExercise {
    
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "workout_exercise_seq_generator"
    )
    @SequenceGenerator(
        name = "workout_exercise_seq_generator",
        sequenceName = "workout_exercise_id_seq",
        allocationSize = 50
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    private int sets;

    private int reps;

    private int weightKg;

    private int exerciseOrder;
}
