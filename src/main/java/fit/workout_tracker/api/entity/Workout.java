package fit.workout_tracker.api.entity;

import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Workout {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    private String description;

    private boolean isActive;

    @CreationTimestamp
    private Instant createdAt;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "workout", orphanRemoval = true)
    private List<WorkoutExercise> workoutExercises;

    @OneToMany(mappedBy = "workout", orphanRemoval = true)
    private List<ScheduledWorkout> scheduledWorkouts;
}
