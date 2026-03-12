package fit.workout_tracker.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fit.workout_tracker.api.dto.UpdateWorkoutExerciseDto;
import fit.workout_tracker.api.dto.WorkoutDto;
import fit.workout_tracker.api.dto.WorkoutExerciseDto;
import fit.workout_tracker.api.dto.response.WorkoutResponse;
import fit.workout_tracker.api.entity.User;
import fit.workout_tracker.api.service.WorkoutService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;
    
    @Autowired
    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createWorkout(
        @Valid @RequestBody WorkoutDto workoutDto,
        @AuthenticationPrincipal User authenticatedUser
    ) {
        boolean result = workoutService.createWorkout(workoutDto, authenticatedUser);
        if (!result)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<WorkoutResponse>> getUserWorkouts(
        @AuthenticationPrincipal User authenticatedUser
    ) {
        var workouts = workoutService.findAllUserWorkouts(authenticatedUser);
        return ResponseEntity.ok(workouts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutResponse> getWorkoutById(
        @PathVariable Long id,
        @AuthenticationPrincipal User authenticatedUser
    ) {
        var workout = workoutService.findWorkoutById(id, authenticatedUser);
        return ResponseEntity.ok(workout);
    }

    @PutMapping("/{id}/update-name")
    public ResponseEntity<?> updateWorkoutName(
        @PathVariable Long id,
        @RequestParam String name,
        @AuthenticationPrincipal User authenticatedUser
    ) {
        workoutService.updateWorkoutName(id, name, authenticatedUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/update-description")
    public ResponseEntity<?> updateWorkoutDescription(
        @PathVariable Long id,
        @RequestParam String description,
        @AuthenticationPrincipal User authenticatedUser
    ) {
        workoutService.updateWorkoutDescription(id, description, authenticatedUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/add-exercise")
    public ResponseEntity<?> updateWorkoutAddExercise(
        @PathVariable Long id,
        @Valid @RequestBody WorkoutExerciseDto workoutExerciseDto,
        @AuthenticationPrincipal User authenticatedUser
    ) {
        workoutService.updateWorkoutAddExercise(
            id, workoutExerciseDto, authenticatedUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/update-exercise")
    public ResponseEntity<?> updateWorkoutExercise(
        @PathVariable Long id,
        @Valid @RequestBody UpdateWorkoutExerciseDto workoutExerciseDto,
        @AuthenticationPrincipal User authenticatedUser
    ) {
        workoutService.updateWorkoutExercise(id, workoutExerciseDto, authenticatedUser);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/delete-exercise")
    public ResponseEntity<?> deleteWorkoutExercise(
        @PathVariable Long id,
        @RequestParam(name = "workout_exercise_id") Long workoutExerciseId,
        @AuthenticationPrincipal User authenticatedUser
    ) {
        workoutService.deleteWorkoutExercise(id, workoutExerciseId, authenticatedUser);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteWorkout(
        @PathVariable Long id,
        @AuthenticationPrincipal User authenticatedUser
    ) {
        workoutService.deleteWorkout(id, authenticatedUser);
        return ResponseEntity.ok().build();
    }
}
