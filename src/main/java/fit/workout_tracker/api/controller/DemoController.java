package fit.workout_tracker.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/demo")
    public String getDemoStr() {
        return "Hello from /demo";
    }
}
