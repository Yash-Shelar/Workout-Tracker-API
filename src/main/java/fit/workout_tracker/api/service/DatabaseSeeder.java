package fit.workout_tracker.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import fit.workout_tracker.api.entity.Exercise;
import fit.workout_tracker.api.entity.User;
import fit.workout_tracker.api.enums.ExerciseCategory;
import fit.workout_tracker.api.enums.MuscleGroup;
import fit.workout_tracker.api.repository.ExerciseRepository;
import fit.workout_tracker.api.repository.UserRepository;
import jakarta.transaction.Transactional;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final Logger logger = Logger.getLogger(DatabaseSeeder.class.getName());

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ExerciseRepository exerciseRepository;

    @Autowired
    public DatabaseSeeder(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        ExerciseRepository exerciseRepository) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        logger.info("Started database seeding...");
        if (userRepository.count() == 0) {
            seedTestUser();
            seedExercises();
            logger.info("Database seeding complete.");
        } else {
            logger.info("Database already seeded. skipping...");
        }
    }
    
    private void seedTestUser() {
        var testUser = new User();
        testUser.setDisplayUserName("TestUser");
        testUser.setUserEmail("test_user@email.com");
        testUser.setPassword(passwordEncoder.encode("password"));

        userRepository.save(testUser);

        logger.info("Seeded test user.");
    }

    private void seedExercises() {
        List<Exercise> allExercises = new ArrayList<>();

        // --- 1. RESISTANCE (10) ---
        allExercises.add(create("Bench Press", "Chest press with barbell", ExerciseCategory.RESISTANCE, Set.of(MuscleGroup.CHEST, MuscleGroup.ARMS)));
        allExercises.add(create("Back Squat", "Barbell squat for legs", ExerciseCategory.RESISTANCE, Set.of(MuscleGroup.LEGS)));
        allExercises.add(create("Deadlift", "Compound posterior chain pull", ExerciseCategory.RESISTANCE, Set.of(MuscleGroup.BACK, MuscleGroup.LEGS)));
        allExercises.add(create("Overhead Press", "Shoulder press standing", ExerciseCategory.RESISTANCE, Set.of(MuscleGroup.SHOULDERS, MuscleGroup.ARMS)));
        allExercises.add(create("Pull-Ups", "Vertical pull for back", ExerciseCategory.RESISTANCE, Set.of(MuscleGroup.BACK, MuscleGroup.ARMS)));
        allExercises.add(create("Barbell Row", "Horizontal pull for back", ExerciseCategory.RESISTANCE, Set.of(MuscleGroup.BACK)));
        allExercises.add(create("Dips", "Tricep and chest focus", ExerciseCategory.RESISTANCE, Set.of(MuscleGroup.CHEST, MuscleGroup.ARMS)));
        allExercises.add(create("Lunges", "Unilateral leg strength", ExerciseCategory.RESISTANCE, Set.of(MuscleGroup.LEGS)));
        allExercises.add(create("Bicep Curls", "Isolation for arms", ExerciseCategory.RESISTANCE, Set.of(MuscleGroup.ARMS)));
        allExercises.add(create("Plank", "Isometric core hold", ExerciseCategory.RESISTANCE, Set.of(MuscleGroup.CORE)));
            
        // --- 2. CARDIO_HIIT (10) ---
        allExercises.add(create("Sprints", "Max effort running intervals", ExerciseCategory.CARDIO_HIIT, Set.of(MuscleGroup.LEGS, MuscleGroup.CORE)));
        allExercises.add(create("Burpees", "Explosive full body movement", ExerciseCategory.CARDIO_HIIT, Set.of(MuscleGroup.CHEST, MuscleGroup.LEGS, MuscleGroup.CORE)));
        allExercises.add(create("Mountain Climbers", "Fast knee-to-chest in plank", ExerciseCategory.CARDIO_HIIT, Set.of(MuscleGroup.CORE, MuscleGroup.SHOULDERS)));
        allExercises.add(create("Jump Squats", "Plyometric squat jumps", ExerciseCategory.CARDIO_HIIT, Set.of(MuscleGroup.LEGS)));
        allExercises.add(create("Battle Ropes", "High-intensity arm waves", ExerciseCategory.CARDIO_HIIT, Set.of(MuscleGroup.ARMS, MuscleGroup.SHOULDERS, MuscleGroup.CORE)));
        allExercises.add(create("Box Jumps", "Explosive jump onto platform", ExerciseCategory.CARDIO_HIIT, Set.of(MuscleGroup.LEGS)));
        allExercises.add(create("Kettlebell Swings", "Rapid hip hinge movements", ExerciseCategory.CARDIO_HIIT, Set.of(MuscleGroup.BACK, MuscleGroup.LEGS, MuscleGroup.CORE)));
        allExercises.add(create("High Knees", "Running in place with high lift", ExerciseCategory.CARDIO_HIIT, Set.of(MuscleGroup.LEGS, MuscleGroup.CORE)));
        allExercises.add(create("Jumping Lunges", "Alternating plyometric lunges", ExerciseCategory.CARDIO_HIIT, Set.of(MuscleGroup.LEGS)));
        allExercises.add(create("Thrusters", "Squat into overhead press", ExerciseCategory.CARDIO_HIIT, Set.of(MuscleGroup.LEGS, MuscleGroup.SHOULDERS)));
            
        // --- 3. CARDIO_LIIT (10) ---
        allExercises.add(create("Brisk Walking", "Steady pace walking", ExerciseCategory.CARDIO_LIIT, Set.of(MuscleGroup.LEGS)));
        allExercises.add(create("Cycling", "Steady-state pedaling", ExerciseCategory.CARDIO_LIIT, Set.of(MuscleGroup.LEGS)));
        allExercises.add(create("Swimming", "Laps at a sustainable pace", ExerciseCategory.CARDIO_LIIT, Set.of(MuscleGroup.BACK, MuscleGroup.ARMS, MuscleGroup.LEGS)));
        allExercises.add(create("Elliptical", "Low impact gliding", ExerciseCategory.CARDIO_LIIT, Set.of(MuscleGroup.LEGS, MuscleGroup.ARMS)));
        allExercises.add(create("Rowing", "Consistent pace rowing", ExerciseCategory.CARDIO_LIIT, Set.of(MuscleGroup.BACK, MuscleGroup.ARMS)));
        allExercises.add(create("Stair Climber", "Steady stepping motion", ExerciseCategory.CARDIO_LIIT, Set.of(MuscleGroup.LEGS)));
        allExercises.add(create("Hiking", "Walking on uneven terrain", ExerciseCategory.CARDIO_LIIT, Set.of(MuscleGroup.LEGS, MuscleGroup.CORE)));
        allExercises.add(create("Light Jogging", "Slow recovery run", ExerciseCategory.CARDIO_LIIT, Set.of(MuscleGroup.LEGS)));
        allExercises.add(create("Power Walking", "Vigorous walking with arm swing", ExerciseCategory.CARDIO_LIIT, Set.of(MuscleGroup.LEGS, MuscleGroup.ARMS)));
        allExercises.add(create("Aqua Aerobics", "Water-based resistance cardio", ExerciseCategory.CARDIO_LIIT, Set.of(MuscleGroup.LEGS, MuscleGroup.CORE)));
            
        // --- 4. FLEXIBILITY (10) ---
        allExercises.add(create("Downward Dog", "Hamstring and back stretch", ExerciseCategory.FLEXIBILITY, Set.of(MuscleGroup.BACK, MuscleGroup.LEGS)));
        allExercises.add(create("Cobra Pose", "Spinal extension stretch", ExerciseCategory.FLEXIBILITY, Set.of(MuscleGroup.BACK, MuscleGroup.CORE)));
        allExercises.add(create("Childs Pose", "Lower back relaxation", ExerciseCategory.FLEXIBILITY, Set.of(MuscleGroup.BACK)));
        allExercises.add(create("Cat-Cow", "Spinal mobility flow", ExerciseCategory.FLEXIBILITY, Set.of(MuscleGroup.BACK, MuscleGroup.CORE)));
        allExercises.add(create("Pigeon Pose", "Deep hip opener", ExerciseCategory.FLEXIBILITY, Set.of(MuscleGroup.LEGS)));
        allExercises.add(create("Butterfly Stretch", "Inner thigh stretch", ExerciseCategory.FLEXIBILITY, Set.of(MuscleGroup.LEGS)));
        allExercises.add(create("Forward Fold", "Standing hamstring stretch", ExerciseCategory.FLEXIBILITY, Set.of(MuscleGroup.LEGS, MuscleGroup.BACK)));
        allExercises.add(create("Warrior II", "Hip and leg flexibility", ExerciseCategory.FLEXIBILITY, Set.of(MuscleGroup.LEGS, MuscleGroup.SHOULDERS)));
        allExercises.add(create("Tricep Stretch", "Overhead arm stretch", ExerciseCategory.FLEXIBILITY, Set.of(MuscleGroup.ARMS)));
        allExercises.add(create("Crossover Stretch", "Glute and lower back stretch", ExerciseCategory.FLEXIBILITY, Set.of(MuscleGroup.LEGS, MuscleGroup.BACK)));
            
        // --- 5. BALANCE (10) ---
        allExercises.add(create("Tree Pose", "Single leg balance", ExerciseCategory.BALANCE, Set.of(MuscleGroup.LEGS, MuscleGroup.CORE)));
        allExercises.add(create("Single Leg Deadlift", "Balance with hip hinge", ExerciseCategory.BALANCE, Set.of(MuscleGroup.LEGS, MuscleGroup.CORE)));
        allExercises.add(create("Bird Dog", "Core stability on all fours", ExerciseCategory.BALANCE, Set.of(MuscleGroup.CORE, MuscleGroup.BACK)));
        allExercises.add(create("Heel-to-Toe Walk", "Walking in a straight line", ExerciseCategory.BALANCE, Set.of(MuscleGroup.LEGS, MuscleGroup.CORE)));
        allExercises.add(create("Bosu Ball Squat", "Squat on unstable surface", ExerciseCategory.BALANCE, Set.of(MuscleGroup.LEGS, MuscleGroup.CORE)));
        allExercises.add(create("Dead Bug", "Supine stability exercise", ExerciseCategory.BALANCE, Set.of(MuscleGroup.CORE)));
        allExercises.add(create("Plank Leg Lift", "Balance in a plank position", ExerciseCategory.BALANCE, Set.of(MuscleGroup.CORE, MuscleGroup.LEGS)));
        allExercises.add(create("Star Pose", "Lateral limb extension balance", ExerciseCategory.BALANCE, Set.of(MuscleGroup.CORE, MuscleGroup.SHOULDERS)));
        allExercises.add(create("Tai Chi Circle", "Slow controlled movement", ExerciseCategory.BALANCE, Set.of(MuscleGroup.LEGS, MuscleGroup.CORE)));
        allExercises.add(create("Flamingo Stand", "Single leg hold with eyes closed", ExerciseCategory.BALANCE, Set.of(MuscleGroup.LEGS, MuscleGroup.CORE)));

        exerciseRepository.saveAll(allExercises);
    }

    private Exercise create(
        String name,
        String description,
        ExerciseCategory exerciseCategory,
        Set<MuscleGroup> muscleGroups) {

        return new Exercise(
            null,
            name,
            description,
            Set.of(exerciseCategory),
            muscleGroups
        );
    }
    
}
