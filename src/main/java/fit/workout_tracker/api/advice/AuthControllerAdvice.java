package fit.workout_tracker.api.advice;

import java.util.Map;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import fit.workout_tracker.api.controller.AuthController;
import fit.workout_tracker.api.dto.ErrorDto;
import fit.workout_tracker.api.error.exception.UserAlreadyExistsException;
import io.jsonwebtoken.JwtException;
import org.springframework.security.core.AuthenticationException;

@RestControllerAdvice(assignableTypes = AuthController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthControllerAdvice {

    @ExceptionHandler(exception = {
        JwtException.class,
        AuthenticationException.class
    })
    public ResponseEntity<ErrorDto> authenticationExceptionHandler(
            Exception ex) {

        var message = switch (ex) {
                case AuthenticationException aex -> {
                    var cause = ex.getCause();
                    yield new ErrorDto(
                        cause != null
                            ? ex.getCause().getMessage()
                            : aex.getMessage(),
                        HttpStatus.UNAUTHORIZED.value()
                    );
                }
                default -> new ErrorDto(
                    ex.getMessage(),
                    HttpStatus.UNAUTHORIZED.value()
                );
        };

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(message);
    }

    @ExceptionHandler(exception = UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExistsException(
        Exception e
    ) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(Map.of("message", e.getMessage()));
    }
}
