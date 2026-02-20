package fit.workout_tracker.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginUserDto(
        @Email
        @NotBlank(message = "Email cannot be empty or null.")
        String userEmail,

        @NotBlank(message = "password cannot be empty or null.")
        String password
) {
}
