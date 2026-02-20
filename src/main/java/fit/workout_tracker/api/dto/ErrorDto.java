package fit.workout_tracker.api.dto;

public record ErrorDto(
        String message,
        int statusCode
) {
}
