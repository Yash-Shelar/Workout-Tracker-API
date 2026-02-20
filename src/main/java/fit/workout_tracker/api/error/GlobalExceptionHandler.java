package fit.workout_tracker.api.error;

import fit.workout_tracker.api.dto.ErrorDto;
import io.jsonwebtoken.ExpiredJwtException;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(exception = {
        ExpiredJwtException.class,
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

    @ExceptionHandler(exception = {
        MethodArgumentNotValidException.class,
        HttpMessageNotReadableException.class,
        MissingRequestCookieException.class
    })
    public ResponseEntity<?> badRequestExceptionHandler(Exception ex) {
        if (ex instanceof MethodArgumentNotValidException v) {
                Map<String, String> errors = v.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .collect(Collectors.toMap(
                                e -> e.getField(),
                                e -> e.getDefaultMessage()
                        ));
                        
                return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        var msg = new ErrorDto(
                "Bad Request",
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.badRequest().body(msg);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDto> internalServerExceptionHandler(Exception ex) {
        var message = new ErrorDto(
                "Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        return ResponseEntity.internalServerError().body(message);
    }
}
