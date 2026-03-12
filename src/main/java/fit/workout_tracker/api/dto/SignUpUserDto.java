package fit.workout_tracker.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SignUpUserDto(
        @NotEmpty(message = "User name cannot be empty")
        @Size(max = 255, message = "Username can contain at most 255 characters.")
        String userName,

        @Email
        @NotEmpty(message = "Email address is required.")
        String userEmail,

        @Size(min = 8, message = "Password must be at least 8 characters long.")
        @NotNull(message = "Password is required")
        String password
) {
}
