package fit.workout_tracker.api.controller;

import java.time.OffsetDateTime;
import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
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

import fit.workout_tracker.api.dto.ScheduleWorkoutDto;
import fit.workout_tracker.api.dto.ScheduledWorkoutResponse;
import fit.workout_tracker.api.entity.User;
import fit.workout_tracker.api.enums.WorkoutStatus;
import fit.workout_tracker.api.service.ScheduledWorkoutService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/scheduled-workouts")
public class ScheduledWorkoutController {
    
    private final ScheduledWorkoutService scheduledWorkoutService;

    public ScheduledWorkoutController(
        ScheduledWorkoutService scheduledWorkoutService
    ) {
        this.scheduledWorkoutService = scheduledWorkoutService;
    }

    @PostMapping("/schedule")
    public ResponseEntity<?> scheduleWorkout(
        @Valid @RequestBody ScheduleWorkoutDto dto,
        @AuthenticationPrincipal User user
    ) {
        scheduledWorkoutService.scheduleWorkout(dto, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ScheduledWorkoutResponse>> getAllScheduledWorkoutForUser(
        @ParameterObject Pageable pageable,
        @AuthenticationPrincipal User user
    ) {
        var scheduledWorkouts = scheduledWorkoutService.getAllUserScheduledWorkout(user, pageable);
        return ResponseEntity.ok(scheduledWorkouts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduledWorkoutResponse> getScheduledWorkoutById(
        @PathVariable Long id,
        @AuthenticationPrincipal User user
    ) {
        var scheduledWorkout = scheduledWorkoutService.getByIdForUser(id, user);
        return ResponseEntity.ok(scheduledWorkout);
    }

    @GetMapping("/by-status")
    public ResponseEntity<?> getScheduledWorkoutByStatus(
        @RequestParam WorkoutStatus workoutStatus,
        @ParameterObject Pageable pageable,
        @AuthenticationPrincipal User user
    ) {
        var scheduledWorkoutResponses = scheduledWorkoutService.getByStatusPagedAndSorted(
                workoutStatus, pageable, user);
        return ResponseEntity.ok(scheduledWorkoutResponses);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateScheduledWorkoutStatus(
        @PathVariable Long id,
        @RequestParam WorkoutStatus workoutStatus,
        @AuthenticationPrincipal User user
    ) {
        scheduledWorkoutService.updateScheduledWorkoutStatus(id, workoutStatus, user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reschedule")
    public ResponseEntity<?> updateScheduledWorkoutReschedule(
        @PathVariable Long id,
        @RequestParam OffsetDateTime scheduleFor,
        @AuthenticationPrincipal User user
    ) {
        scheduledWorkoutService.updateScheduledWorkoutReschedule(id, scheduleFor, user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/user-notes")
    public ResponseEntity<?> updateScheduledWorkoutUserNotes(
        @PathVariable Long id,
        @RequestParam String userNotes,
        @AuthenticationPrincipal User user
    ) {
        scheduledWorkoutService.updateScheduledWorkoutUserNotes(
            id, userNotes, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteScheduledWorkoutForUser(
        @PathVariable Long id,
        @AuthenticationPrincipal User user
    ) {
        scheduledWorkoutService.deleteScheduledWorkoutForUserById(id, user);
        return ResponseEntity.ok().build();
    }
}
