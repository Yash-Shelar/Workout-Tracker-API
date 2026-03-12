package fit.workout_tracker.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fit.workout_tracker.api.dto.UpdateWorkoutExerciseDto;
import fit.workout_tracker.api.dto.WorkoutDto;
import fit.workout_tracker.api.dto.WorkoutExerciseDto;
import fit.workout_tracker.api.dto.response.WorkoutResponse;
import fit.workout_tracker.api.entity.User;
import fit.workout_tracker.api.entity.Workout;
import fit.workout_tracker.api.entity.WorkoutExercise;
import fit.workout_tracker.api.error.exception.ExerciseNotFoundException;
import fit.workout_tracker.api.error.exception.WorkoutExerciseNotFound;
import fit.workout_tracker.api.error.exception.WorkoutExerciseOrderException;
import fit.workout_tracker.api.error.exception.WorkoutNotFoundException;
import fit.workout_tracker.api.repository.ExerciseRepository;
import fit.workout_tracker.api.repository.ScheduledWorkoutRepository;
import fit.workout_tracker.api.repository.WorkoutExerciseRepository;
import fit.workout_tracker.api.repository.WorkoutRepository;

@Service
public class WorkoutService {

    private final ExerciseRepository exerciseRepository;
    private final WorkoutRepository workoutRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final ScheduledWorkoutRepository scheduledWorkoutRepository;
    
    @Autowired
    public WorkoutService(
        ExerciseRepository exerciseRepository,
        WorkoutRepository workoutRepository,
        WorkoutExerciseRepository workoutExerciseRepository,
        ScheduledWorkoutRepository scheduledWorkoutRepository
    ) {

        this.exerciseRepository = exerciseRepository;
        this.workoutRepository = workoutRepository;
        this.workoutExerciseRepository = workoutExerciseRepository;
        this.scheduledWorkoutRepository = scheduledWorkoutRepository;
    }

    public boolean createWorkout(WorkoutDto workoutDto, User user) {        
        var workoutOpt = createWorkoutFromDto(workoutDto);
        if (workoutOpt.isEmpty())
            return false;
        
        var workout = workoutOpt.get();
        workout.setUser(user);

        workoutRepository.save(workout);
        
        return true;
    }

    public List<WorkoutResponse> findAllUserWorkouts(User user) {
        return workoutRepository.findAllByUserEager(user)
            .stream()
            .map(WorkoutResponse::from)
            .toList();
    }

    public WorkoutResponse findWorkoutById(Long id, User user) {
        var workout = workoutRepository.findByIdAndUserEager(id, user)
            .orElseThrow(() -> new WorkoutNotFoundException(
                "workout not found for current user"
            ));
        return WorkoutResponse.from(workout);
    }

    public void updateWorkoutName(Long id, String name, User user) {
        var workout = workoutRepository.findByIdAndUser(id, user)
            .orElseThrow(() -> new WorkoutNotFoundException(
                "workout not found for current user"
            ));

        workout.setName(name);
        workout = workoutRepository.save(workout);
    }

    public void updateWorkoutDescription(Long id, String description, User user) {
        var workout = workoutRepository.findByIdAndUser(id, user)
            .orElseThrow(() -> new WorkoutNotFoundException(
                "workout not found for current user"
            ));
        
        workout.setDescription(description);
        workout = workoutRepository.save(workout);
    }

    public void updateWorkoutAddExercise(
        Long id,
        WorkoutExerciseDto dto,
        User user
    ) {  
        var workout = workoutRepository.findByIdAndUser(id, user)
            .orElseThrow(() -> new WorkoutNotFoundException(
                "workout not found for current user"
            ));
           
        if (workoutExerciseRepository.existByExerciseOrder(dto.exerciseOrder(), workout)) {
            throw new WorkoutExerciseOrderException(
                "Another workout exercise with exercise order already exists"
            );
        }

        var exercise = exerciseRepository.findById(dto.exerciseId())
            .orElseThrow(() -> new ExerciseNotFoundException(
                "Exercise not found"
            ));

        var workoutExercise = new WorkoutExercise();
        workoutExercise.setExercise(exercise);
        workoutExercise.setExerciseOrder(dto.exerciseOrder());
        workoutExercise.setReps(dto.reps());
        workoutExercise.setSets(dto.sets());
        workoutExercise.setWeightKg(dto.weightKg());
        workoutExercise.setWorkout(workout);

        workoutExerciseRepository.save(workoutExercise);
    }

    public void updateWorkoutExercise(
        Long workoutId,
        UpdateWorkoutExerciseDto dto,
        User user
    ) {

        var workout = workoutRepository.findByIdAndUserEager(workoutId, user)
            .orElseThrow(() -> new WorkoutNotFoundException(
                "workout not found for current user"));
        
        var workoutExercise = workout.getWorkoutExercises().stream()
            .filter(we -> we.getId() == dto.workoutExerciseId())
            .findFirst()
            .orElseThrow(() -> new WorkoutExerciseNotFound(
                "Workout exercise not found for user workout"));
        
        if (dto.exerciseId() != null 
        && dto.exerciseId() != workoutExercise.getExercise().getId()) {
            var exercise = exerciseRepository.findById(dto.exerciseId())
                .orElseThrow(() -> new ExerciseNotFoundException("Exercise not found"));

            workoutExercise.setExercise(exercise);
        }

        if (dto.exerciseOrder() != null) {
            boolean repeatedOrder = workout.getWorkoutExercises()
                .stream()
                .anyMatch(we -> 
                    we.getExerciseOrder() == dto.exerciseOrder()
                    && we.getId() != dto.workoutExerciseId());

            if (repeatedOrder) {
                throw new WorkoutExerciseOrderException(
                    "Another workout exercise with exercise order already exists"
                );
            }
            workoutExercise.setExerciseOrder(dto.exerciseOrder());
        }

        if (dto.sets() != null)
            workoutExercise.setSets(dto.sets());
        if (dto.reps() != null)
            workoutExercise.setReps(dto.reps());
        if (dto.weightKg() != null)
            workoutExercise.setWeightKg(dto.weightKg());

        workoutExerciseRepository.save(workoutExercise);
    }

    @Transactional
    public void deleteWorkoutExercise(Long id, Long workoutExerciseId, User user) {
        var workout = workoutRepository.findByIdAndUserEager(id, user)
            .orElseThrow(() -> new WorkoutNotFoundException(
                "workout not found for current user"));
        
        boolean removed = workout
            .getWorkoutExercises()
            .removeIf(we -> we.getId() == workoutExerciseId);
        
        if (!removed) {
            throw new WorkoutExerciseOrderException(
                "Failed to remove workout exercise");
        }
    }

    @Transactional
    public void deleteWorkout(Long id, User user) {
        var workout = workoutRepository.findByIdAndUser(id, user)
            .orElseThrow(() -> new WorkoutNotFoundException(
                "workout with not found for user"
            ));

        workoutExerciseRepository.deleteWorkoutExercisesForWorkout(workout);
        scheduledWorkoutRepository.deleteScheduledWorkoutsForWorkout(workout);
        
        workoutRepository.delete(workout);
    }

    private Optional<Workout> createWorkoutFromDto(WorkoutDto workoutDto) {
        if (workoutDto == null)
            return Optional.empty();
        var workout = new Workout();
        workout.setName(workoutDto.name());
        workout.setDescription(workoutDto.description());
        
        var workoutExerciseDtos = workoutDto.exercises();

        var exerciseIds = workoutExerciseDtos.stream()
            .map(dto -> dto.exerciseId())
            .toList();

        var exerciseMap = exerciseRepository.findAllById(exerciseIds)
            .stream()
            .collect(Collectors.toMap(
                exercise -> exercise.getId(), 
                Function.identity()
            ));

        if (exerciseMap.size() < exerciseIds.size())
            return Optional.empty();

        var workoutExercises = workoutExerciseDtos.stream()
            .map(dto -> {
                var workoutExercise = new WorkoutExercise();
                workoutExercise.setExerciseOrder(dto.exerciseOrder());
                workoutExercise.setSets(dto.sets());
                workoutExercise.setReps(dto.reps());
                workoutExercise.setWeightKg(dto.weightKg());
                workoutExercise.setExercise(exerciseMap.get(dto.exerciseId()));
                workoutExercise.setWorkout(workout);
                
                return workoutExercise;
            })
            .collect(Collectors.toCollection(ArrayList::new));

        workout.setWorkoutExercises(workoutExercises);

        return Optional.of(workout);
    }
}
