package fit.workout_tracker.api.service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fit.workout_tracker.api.dto.ScheduleWorkoutDto;
import fit.workout_tracker.api.dto.ScheduledWorkoutResponse;
import fit.workout_tracker.api.entity.ScheduledWorkout;
import fit.workout_tracker.api.entity.User;
import fit.workout_tracker.api.enums.WorkoutStatus;
import fit.workout_tracker.api.error.exception.ScheduleWorkoutException;
import fit.workout_tracker.api.error.exception.ScheduledWorkoutNotFound;
import fit.workout_tracker.api.error.exception.ScheduledWorkoutRescheduleException;
import fit.workout_tracker.api.error.exception.WorkoutNotFoundException;
import fit.workout_tracker.api.repository.ScheduledWorkoutRepository;
import fit.workout_tracker.api.repository.WorkoutRepository;

@Service
public class ScheduledWorkoutService {
    
    private final ScheduledWorkoutRepository scheduledWorkoutRepository;
    private final WorkoutRepository workoutRepository;

    @Autowired
    public ScheduledWorkoutService(
        ScheduledWorkoutRepository scheduledWorkoutRepository,
        WorkoutRepository workoutRepository
    ) {
        this.scheduledWorkoutRepository = scheduledWorkoutRepository;
        this.workoutRepository = workoutRepository;
    }

    public void scheduleWorkout(ScheduleWorkoutDto dto, User user) {
        var workout = workoutRepository.findByIdAndUser(dto.workoutId(), user)
            .orElseThrow(() -> new WorkoutNotFoundException(
                "Workout not found for current user"
            ));

        var scheduleFor = Instant.from(dto.scheduleFor());
        if (scheduleFor.isBefore(Instant.now())) {
            throw new ScheduleWorkoutException(
                "Workout cannot be scheduled for past time");
        }

        var scheduledWorkout = new ScheduledWorkout();
        scheduledWorkout.setWorkout(workout);
        scheduledWorkout.setUser(user);
        scheduledWorkout.setScheduledFor(scheduleFor);
        scheduledWorkout.setStatus(WorkoutStatus.SCHEDULED);
        scheduledWorkout.setUserNotes(dto.userNotes());

        scheduledWorkoutRepository.save(scheduledWorkout);
    }

    public List<ScheduledWorkoutResponse> getAllUserScheduledWorkout(User user, Pageable pageable) {
        var scheduledWorkouts = scheduledWorkoutRepository.findByUser(user, pageable);
        return scheduledWorkouts
            .stream()
            .map(mapScheduledWorkoutToResponse())
            .toList();
    }

    public ScheduledWorkoutResponse getByIdForUser(Long id, User user) {
        var sw = findScheduledWorkoutForUserById(id, user);
        return mapScheduledWorkoutToResponse().apply(sw);
    }

    public List<ScheduledWorkoutResponse> getByStatusPagedAndSorted(
        WorkoutStatus status,
        Pageable pageable,
        User user
    ) {
        var scheduledWorkouts = scheduledWorkoutRepository.findAllByUserAndStatusPageable(
            status, user, pageable);

        return scheduledWorkouts
            .stream()
            .map(mapScheduledWorkoutToResponse())
            .toList();
    }

    private Function<ScheduledWorkout, ScheduledWorkoutResponse> mapScheduledWorkoutToResponse() {
        return sw -> new ScheduledWorkoutResponse(
            sw.getId(),
            sw.getWorkout().getId(),
            sw.getScheduledFor(),
            sw.getCompletedAt(),
            sw.getStatus(),
            sw.getUserNotes()
        );
    }

    @Transactional
    public void updateScheduledWorkoutStatus(
        Long scheduledWorkoutId,
        WorkoutStatus status,
        User user
    ) {

        var scheduledWorkout = findScheduledWorkoutForUserById(scheduledWorkoutId, user);

        scheduledWorkout.setStatus(status);
        if (status == WorkoutStatus.COMPLETED)
            scheduledWorkout.setCompletedAt(Instant.now());
    }

    @Transactional
    public void updateScheduledWorkoutReschedule(
        Long scheduledWorkoutId,
        OffsetDateTime scheduleFor,
        User user
    ) {
        var scheduledWorkout = findScheduledWorkoutForUserById(scheduledWorkoutId, user);

        var scheduleForInstant = Instant.from(scheduleFor);

        if (scheduleForInstant.isBefore(Instant.now()))
            throw new ScheduledWorkoutRescheduleException(
                "Invalid schedule time should be a future time"
            );
        
        scheduledWorkout.setScheduledFor(scheduleForInstant);
    }

    @Transactional
    public void updateScheduledWorkoutUserNotes(
        Long scheduledWorkoutId,
        String userNotes,
        User user
    ) {
        var scheduledWorkout = findScheduledWorkoutForUserById(scheduledWorkoutId, user);
        scheduledWorkout.setUserNotes(userNotes);
    }

    public void deleteScheduledWorkoutForUserById(Long id, User user) {
        var scheduledWorkout = findScheduledWorkoutForUserById(id, user);
        scheduledWorkoutRepository.delete(scheduledWorkout);
    }

    private ScheduledWorkout findScheduledWorkoutForUserById(Long id, User user) {
        return scheduledWorkoutRepository
            .findByIdAndUser(id, user)
            .orElseThrow(() -> new ScheduledWorkoutNotFound(
                "Scheduled workout not found for user"
            ));   
    }
}
