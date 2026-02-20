package fit.workout_tracker.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fit.workout_tracker.api.entity.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    
    @Modifying
    @Transactional
    @Query("""
    DELETE FROM RefreshToken rt
    WHERE rt.token = :refreshToken""")
    void deleteByToken(String refreshToken);

    @Query("""
    SELECT rt FROM RefreshToken rt
    WHERE rt.token = :refreshToken""")
    Optional<RefreshToken> findByToken(String refreshToken);
}
