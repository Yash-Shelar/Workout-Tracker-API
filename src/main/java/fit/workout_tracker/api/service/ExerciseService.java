package fit.workout_tracker.api.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fit.workout_tracker.api.dto.ExerciseDto;
import fit.workout_tracker.api.enums.ExerciseCategory;
import fit.workout_tracker.api.enums.MuscleGroup;
import fit.workout_tracker.api.error.exception.ExerciseNotFoundException;
import fit.workout_tracker.api.repository.ExerciseRepository;

@Service
public class ExerciseService {
    
    private final ExerciseRepository exerciseRepository;

    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public List<ExerciseDto> getExercisesByPagingAndSorting(Pageable pageable) {
        return exerciseRepository.findAllPageable(pageable)
            .stream()
            .map(ExerciseDto::from)
            .toList();
    }

    public ExerciseDto getExerciseById(long id) {
        var exercise = exerciseRepository.findByIdEager(id)
            .orElseThrow(() -> new ExerciseNotFoundException(String.format(
                "exercise with id: %d not found.",
                id
            )));
        return ExerciseDto.from(exercise);
    }

    public List<ExerciseDto> getExerciseByCategory(ExerciseCategory category) {
        return exerciseRepository.findByCategory(category)
            .stream()
            .map(ExerciseDto::from)
            .toList();
    }

    public List<ExerciseDto> getExerciseByMuscleGroup(MuscleGroup muscleGroup) {
        return exerciseRepository.findByMuscleGroup(muscleGroup)
            .stream()
            .map(ExerciseDto::from)
            .toList();
    }
}
