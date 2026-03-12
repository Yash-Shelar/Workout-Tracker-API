package fit.workout_tracker.api.cron;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import fit.workout_tracker.api.entity.User;
import fit.workout_tracker.api.entity.VerificationToken;
import fit.workout_tracker.api.repository.UserRepository;
import fit.workout_tracker.api.repository.VerificationTokenRepository;

@Component
public class CronJobs {
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    public CronJobs(UserRepository userRepository, VerificationTokenRepository verificationTokenRepository) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS)
    public void cleanUpExpiredToken() {
        var vts = verificationTokenRepository.findAllExpired(Instant.now());
        var userIds = vts.stream()
            .map(VerificationToken::getUser)
            .map(User::getId)
            .toList();
                
        verificationTokenRepository.deleteAll(vts);
        userRepository.deleteAllById(userIds);
    }
}
