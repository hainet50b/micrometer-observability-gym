package com.programacho;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationFilter;

public class UserObservationFilter implements ObservationFilter {

    @Override
    public Observation.Context map(Observation.Context context) {
        context.put("filter.key", "filter.value");

        return context;
    }
}
