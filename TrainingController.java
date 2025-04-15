package com.example.mock.controller;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/training-data")
@CrossOrigin(origins = "*")
public class TrainingController {

    @GetMapping
    public Map<String, Object> getTrainingData(
            @RequestParam String token,
            @RequestParam String start,
            @RequestParam String end
    ) {
        // Fake data simulation
        List<String> dates = Arrays.asList("2025-04-01", "2025-04-02", "2025-04-03", "2025-04-04", "2025-04-05", "2025-04-06", "2025-04-07");
        List<Double> acrValues = Arrays.asList(0.9, 1.1, 1.2, 1.4, 1.6, 1.3, 1.5);
        List<Double> weeklyLoad = Arrays.asList(300.0, 320.0, 280.0, 310.0, 350.0, 330.0, 300.0);

        Map<String, Object> response = new HashMap<>();

        Map<String, Object> acr = new HashMap<>();
        acr.put("dates", dates);
        acr.put("values", acrValues);

        Map<String, Object> load = new HashMap<>();
        load.put("dates", dates);
        load.put("values", weeklyLoad);

        response.put("acr", acr);
        response.put("weeklyLoad", load);
        response.put("restDays", 1);

        return response;
    }
}
