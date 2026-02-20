package fit.workout_tracker.api.dto;

public record TokenResponseDto(
        String accessToken,
        Long expiresInMs
) {
}
