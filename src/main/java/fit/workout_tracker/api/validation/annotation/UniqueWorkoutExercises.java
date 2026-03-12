package fit.workout_tracker.api.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import fit.workout_tracker.api.validation.validator.UniqueWorkoutExerciseDtoValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueWorkoutExerciseDtoValidator.class)
public @interface UniqueWorkoutExercises {

    String message() default "Elements in the list must be unique.";
    
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
