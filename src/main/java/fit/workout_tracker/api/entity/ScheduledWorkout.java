package fit.workout_tracker.api.entity;

import java.time.Instant;

import fit.workout_tracker.api.enums.WorkoutStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ScheduledWorkout {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    private Instant scheduledFor;

    private Instant completedAt;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", columnDefinition = "smallint")
    private WorkoutStatus status;

    @Column(columnDefinition = "TEXT")
    private String userNotes;
}
