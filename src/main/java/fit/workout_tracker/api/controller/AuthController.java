package fit.workout_tracker.api.controller;

import fit.workout_tracker.api.dto.ErrorDto;
import fit.workout_tracker.api.dto.LoginUserDto;
import fit.workout_tracker.api.dto.SignUpUserDto;
import fit.workout_tracker.api.dto.TokenResponseDto;
import fit.workout_tracker.api.entity.User;
import fit.workout_tracker.api.service.AuthenticationService;
import fit.workout_tracker.api.service.JwtService;
import fit.workout_tracker.api.service.RefreshTokenService;
import jakarta.validation.Valid;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthController(
        AuthenticationService authenticationService,
        JwtService jwtService,
        RefreshTokenService refreshTokenService
    ) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(
        @Valid @RequestBody SignUpUserDto signUpDto
    ) {
        authenticationService.signUpUser(signUpDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(
        @Valid @RequestBody LoginUserDto loginUserDto
    ) {
        User user = authenticationService.authenticate(loginUserDto);
        if (!user.isVerified())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        String jwtToken = jwtService.generateToken(user);

        String refreshToken = refreshTokenService.generateRefreshToken(user);

        ResponseCookie cookie = ResponseCookie
            .from("refreshToken", refreshToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .sameSite("Strict")
            .maxAge(Duration.ofDays(refreshTokenService.getRefreshTokenExpiryInDays()))
            .build();

        var tokenResponseDto = new TokenResponseDto(
                jwtToken,
                jwtService.getExpirationTimeInMs()
        );

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(tokenResponseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
        @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {
        if (refreshToken != null)
            refreshTokenService.deleteByToken(refreshToken);

        ResponseCookie cookie = ResponseCookie
            .from("refreshToken", "")
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(0)
            .build();

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body("logged out successfully");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refersh(
        @CookieValue("refreshToken") String refreshToken
    ) {
        if (refreshTokenService.isRefreshTokenValid(refreshToken)) {
            var token = refreshTokenService.findRefreshTokenByToken(refreshToken);
            var user = token.getUser();

            String jwt = jwtService.generateToken(user);

            var tokenResponse = new TokenResponseDto(
                jwt,
                jwtService.getExpirationTimeInMs()
            );

            String newRefreshToken = refreshTokenService
                .rotateRefreshToken(refreshToken);

            ResponseCookie cookie = ResponseCookie
                .from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration
                    .ofDays(refreshTokenService.getRefreshTokenExpiryInDays()))
                .sameSite("Strict")
                .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(tokenResponse);
        }
        
        return ResponseEntity.badRequest().body(new ErrorDto(
            "Invalid or expired refresh token",
            HttpStatus.BAD_REQUEST.value()
        ));
    }
}
