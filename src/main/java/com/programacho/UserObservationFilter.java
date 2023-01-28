package com.programacho;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationFilter;

public class UserObservationFilter implements ObservationFilter {

    @Override
    public Observation.Context map(Observation.Context context) {
        context.put("user.key", "user.value");

        return context;
    }
}
