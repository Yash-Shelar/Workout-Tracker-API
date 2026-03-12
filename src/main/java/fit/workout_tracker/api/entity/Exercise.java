package fit.workout_tracker.api.entity;

import java.util.Set;

import org.hibernate.annotations.BatchSize;

import fit.workout_tracker.api.enums.ExerciseCategory;
import fit.workout_tracker.api.enums.MuscleGroup;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
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
    name = "emg_entity_graph",
    attributeNodes = {
        @NamedAttributeNode(value = "muscleGroups")
    }
)
public class Exercise {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
        name = "exercise_seq_generator",
        sequenceName = "exercise_id_seq",
        allocationSize = 50
    )
    private Long id;

    @Column(length = 50)
    private String name;

    private String description;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "category", columnDefinition = "smallint")
    ExerciseCategory category;

    @ElementCollection
    @CollectionTable(
        name = "exercise_muscle_group",
        joinColumns = @JoinColumn(name = "exercise_id", referencedColumnName = "id")
    )
    @Column(name = "muscle_group", columnDefinition = "smallint")
    @Enumerated(EnumType.ORDINAL)
    @BatchSize(size = 50)
    private Set<MuscleGroup> muscleGroups;
}
