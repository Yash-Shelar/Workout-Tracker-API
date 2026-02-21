package fit.workout_tracker.api.entity;

import java.util.Set;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Exercise {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    private String description;

    @ElementCollection
    @CollectionTable(
        name = "exercise_category",
        joinColumns = @JoinColumn(name = "exercise_id", referencedColumnName = "id")
    )
    @Column(name = "category", columnDefinition = "smallint")
    @Enumerated(EnumType.ORDINAL)
    private Set<ExerciseCategory> categories;

    @ElementCollection
    @CollectionTable(
        name = "exercise_muscle_group",
        joinColumns = @JoinColumn(name = "exercise_id", referencedColumnName = "id")
    )
    @Column(name = "muscle_group", columnDefinition = "smallint")
    @Enumerated(EnumType.ORDINAL)
    private Set<MuscleGroup> muscleGroups;
}
