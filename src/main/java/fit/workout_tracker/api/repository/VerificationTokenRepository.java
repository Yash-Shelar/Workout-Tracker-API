package fit.workout_tracker.api.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fit.workout_tracker.api.entity.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {

    @Query("""
        SELECT vt FROM VerificationToken vt
        WHERE vt.expiryDate < :now""")
    List<VerificationToken> findAllExpired(Instant now);
}
