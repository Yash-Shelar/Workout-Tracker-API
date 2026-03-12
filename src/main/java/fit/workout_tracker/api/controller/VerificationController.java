package fit.workout_tracker.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fit.workout_tracker.api.service.AuthenticationService;

@Controller
@RequestMapping("/verify")
public class VerificationController {
    private final AuthenticationService authenticationService;

    public VerificationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public String verifyRegisteredUsers(@RequestParam String token) {
        boolean isVerified = authenticationService.verifyRegisteredUsers(token);
        if (!isVerified)
            return "verify_failure";
        return "verify_success";
    }
}
