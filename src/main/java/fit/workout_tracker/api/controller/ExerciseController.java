package fit.workout_tracker.api.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fit.workout_tracker.api.dto.ExerciseDto;
import fit.workout_tracker.api.enums.ExerciseCategory;
import fit.workout_tracker.api.enums.MuscleGroup;
import fit.workout_tracker.api.service.ExerciseService;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;
    
    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping
    public ResponseEntity<List<ExerciseDto>> getAll(
        @ParameterObject Pageable pageable
    ) {
        var exercises = exerciseService.getExercisesByPagingAndSorting(pageable);
        return ResponseEntity.ok(exercises);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseDto> getById(@PathVariable long id) {
        var exercise = exerciseService.getExerciseById(id);
        return ResponseEntity.ok(exercise);
    }

    @GetMapping("/by-category")
    public ResponseEntity<List<ExerciseDto>> getByCategory(
        @RequestParam ExerciseCategory exerciseCategory
    ) {
        var exercises = exerciseService.getExerciseByCategory(exerciseCategory);
        return ResponseEntity.ok(exercises);
    }

    @GetMapping("/by-muscle-group")
    public ResponseEntity<List<ExerciseDto>> getByMuscleGroup(
        @RequestParam MuscleGroup muscleGroup
    ) {
        var exercises = exerciseService.getExerciseByMuscleGroup(muscleGroup);
        return ResponseEntity.ok(exercises);
    }
}
