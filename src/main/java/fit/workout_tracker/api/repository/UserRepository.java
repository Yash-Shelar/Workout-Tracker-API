package fit.workout_tracker.api.repository;

import fit.workout_tracker.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
        SELECT u FROM User u
        WHERE u.userEmail = :userEmail""")
    Optional<User> findByUserEmail(String userEmail);

    boolean existsByUserEmail(String userEmail);
}
