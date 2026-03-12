package fit.workout_tracker.api.dto;

import java.time.Instant;

import fit.workout_tracker.api.enums.WorkoutStatus;

public record ScheduledWorkoutResponse(
    Long id,
    Long workoutId,
    Instant scheduledFor,
    Instant completedAt,
    WorkoutStatus status,
    String userNotes
) {
    
}
