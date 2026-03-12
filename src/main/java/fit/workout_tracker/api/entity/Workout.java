package fit.workout_tracker.api.entity;

import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
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
@NamedEntityGraph(
    name = "wwe_entity_graph",
    attributeNodes = {
        @NamedAttributeNode(
            value = "workoutExercises",
            subgraph = "workout_exercises_subgraph"
        )
    },
    subgraphs = {
        @NamedSubgraph(
            name = "workout_exercises_subgraph",
            attributeNodes = {
                @NamedAttributeNode(value = "exercise")
            }
        )
    }
)
public class Workout {
    
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "workout_seq_generator"
    )
    @SequenceGenerator(
        name = "workout_seq_generator",
        sequenceName = "workout_id_seq",
        allocationSize = 50
    )
    private Long id;

    @Column(length = 50)
    private String name;

    private String description;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "workout", orphanRemoval = true,
        cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @BatchSize(size = 50)
    private List<WorkoutExercise> workoutExercises;

    @OneToMany(mappedBy = "workout", orphanRemoval = true,
        cascade = { CascadeType.REMOVE })
    private List<ScheduledWorkout> scheduledWorkouts;
}
