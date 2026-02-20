package fit.workout_tracker.api.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fit.workout_tracker.api.entity.RefreshToken;
import fit.workout_tracker.api.entity.User;
import fit.workout_tracker.api.repository.RefreshTokenRepository;
import lombok.Getter;

@Service
public class RefreshTokenService {

    @Value("${security.refresh-token.expiration-days}")
    @Getter
    private long refreshTokenExpiryInDays;

    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenService(
        RefreshTokenRepository refreshTokenRepository) {

        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String generateRefreshToken(User user) {
        String refreshToken = UUID.randomUUID().toString();

        var rtEntity = new RefreshToken();
        rtEntity.setToken(hashToken(refreshToken));
        rtEntity.setCreatedAt(Instant.now());
        rtEntity.setExpiresAt(Instant.now().plus(
            refreshTokenExpiryInDays, ChronoUnit.DAYS));
        rtEntity.setUser(user);

        refreshTokenRepository.save(rtEntity);

        return refreshToken;
    }

    public void deleteByToken(String refreshToken) {
        String hashedToken = hashToken(refreshToken);

        refreshTokenRepository.deleteByToken(hashedToken);
    }

    public RefreshToken findRefreshTokenByToken(String refreshToken) {
        return refreshTokenRepository.findByToken(hashToken(refreshToken))
            .orElseThrow();
    }

    public boolean isRefreshTokenValid(String refreshToken) {
        var rtEntityOpt = refreshTokenRepository.findByToken(hashToken(refreshToken));
        if (rtEntityOpt.isEmpty())
            return false;

        var rtEntity = rtEntityOpt.get();
        if (Instant.now().isAfter(rtEntity.getExpiresAt()))
            return false;
        if (rtEntity.isRevoked())
            return false;

        return true;
    }

    public String rotateRefreshToken(String refreshToken) {
        var rtEntity = refreshTokenRepository.findByToken(hashToken(refreshToken))
            .orElseThrow();
        
        String token = UUID.randomUUID().toString();
        rtEntity.setToken(hashToken(token));
        rtEntity.setCreatedAt(Instant.now());
        rtEntity.setCreatedAt(Instant.now().plus(
            refreshTokenExpiryInDays, ChronoUnit.DAYS));
        rtEntity.setRevoked(false);

        refreshTokenRepository.save(rtEntity);
        return token;
    }

    public String hashToken(String refreshToken) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        byte[] hash = digest.digest(refreshToken.getBytes(StandardCharsets.UTF_8));
        String encodedHash = Base64.getEncoder().encodeToString(hash);
        return encodedHash;
    }
}
