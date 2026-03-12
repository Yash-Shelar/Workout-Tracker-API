package fit.workout_tracker.api.dto;

import java.time.OffsetDateTime;

import jakarta.validation.constraints.NotNull;

public record ScheduleWorkoutDto(
    @NotNull(message = "workout id required")
    Long workoutId,

    @NotNull(message = "scheduled time required")
    OffsetDateTime scheduleFor,

    String userNotes
) {
}