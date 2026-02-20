package fit.workout_tracker.api.service;

import fit.workout_tracker.api.dto.LoginUserDto;
import fit.workout_tracker.api.dto.SignUpUserDto;
import fit.workout_tracker.api.entity.User;
import fit.workout_tracker.api.repository.UserRepository;
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

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public boolean signUpUser(SignUpUserDto signUpDto) {
        var existingUserOpt = userRepository.findByUserEmail(signUpDto.userEmail());
        if (existingUserOpt.isPresent())
            return false;

        User user = new User();
        user.setDisplayUserName(signUpDto.userName());
        user.setUserEmail(signUpDto.userEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.password()));

        userRepository.save(user);
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
