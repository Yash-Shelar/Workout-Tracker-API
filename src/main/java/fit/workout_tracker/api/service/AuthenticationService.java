package fit.workout_tracker.api.service;

import fit.workout_tracker.api.dto.LoginUserDto;
import fit.workout_tracker.api.dto.SignUpUserDto;
import fit.workout_tracker.api.entity.User;
import fit.workout_tracker.api.entity.VerificationToken;
import fit.workout_tracker.api.error.exception.UserAlreadyExistsException;
import fit.workout_tracker.api.repository.UserRepository;
import fit.workout_tracker.api.repository.VerificationTokenRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final VerificationTokenRepository verificationTokenRepository;

    public AuthenticationService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager,
        EmailService emailService,
        VerificationTokenRepository verificationTokenRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public void signUpUser(SignUpUserDto signUpDto) {
        var existingUserOpt = userRepository.findByUserEmail(signUpDto.userEmail());
        if (existingUserOpt.isPresent()) {
            throw new UserAlreadyExistsException("User with email already exists");
        }

        User user = new User();
        user.setDisplayUserName(signUpDto.userName());
        user.setUserEmail(signUpDto.userEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.password()));
        user.setVerified(false);

        user = userRepository.save(user);

        createAndSaveTokenAsync(user)
            .thenAcceptAsync(vt -> emailService.sendEmail(vt))
            .exceptionally(e -> {
                e.printStackTrace();
                return null;
            });
    }

    private CompletableFuture<VerificationToken> createAndSaveTokenAsync(User user) {
        var verificationToken = new VerificationToken();
        verificationToken.setToken(UUID.randomUUID());
        verificationToken.setExpiryDate(Instant.now().plus(
            1, ChronoUnit.HOURS));
        verificationToken.setUser(user);

        return CompletableFuture.supplyAsync(() ->
            verificationTokenRepository.save(verificationToken));
    }

    public boolean verifyRegisteredUsers(String token) {
        var vTokenOpt = verificationTokenRepository.findById(UUID.fromString(token));
        if (vTokenOpt.isEmpty())
            return false;

        var vToken = vTokenOpt.get();
        if (vToken.getExpiryDate().isBefore(Instant.now()))
            return false;

        var user = vToken.getUser();
        user.setVerified(true);

        userRepository.save(user);
        verificationTokenRepository.delete(vToken);

        return true;
    }

    public User authenticate(LoginUserDto loginUserDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserDto.userEmail(),
                        loginUserDto.password()
                )
        );

        return userRepository.findByUserEmail(loginUserDto.userEmail())
                .orElseThrow(() ->
                        UsernameNotFoundException
                                .fromUsername(loginUserDto.userEmail())
                );
    }
}
