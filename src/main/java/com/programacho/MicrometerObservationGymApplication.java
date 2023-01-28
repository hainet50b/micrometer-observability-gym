package com.programacho;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;

public class MicrometerObservationGymApplication {
    public static void main(String[] args) {
        ObservationRegistry registry = ObservationRegistry.create();
        registry.observationConfig().observationHandler(new LoggingObservationHandler());
        registry.observationConfig().observationConvention(new GlobalUserObservationConvention());
        registry.observationConfig().observationPredicate((name, context) -> {
            return name.contains("programacho");
        });

        Observation.Context context = new UserContext("hainet50b", "Haine Takano").put("programacho.key", "programacho.value");

        Observation observation = Observation.start("programacho.operation", () -> context, registry);

        observation.lowCardinalityKeyValue("low.key", "low.value");
        observation.highCardinalityKeyValue("high.key", "high.value");

        try (Observation.Scope scope = observation.openScope()) {
            observation.event(Observation.Event.of("programacho.event"));
            observation.error(new RuntimeException("programacho.error"));
        } finally {
            observation.stop();
        }
    }
}
