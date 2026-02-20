package fit.workout_tracker.api.error.exception;

public class RefreshTokenInvalidException extends RuntimeException {
    public RefreshTokenInvalidException() {
        super();
    }

    public RefreshTokenInvalidException(String message) {
        super(message);
    }
}
