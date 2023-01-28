package com.programacho;

import io.micrometer.observation.GlobalObservationConvention;
import io.micrometer.observation.Observation;

public class GlobalUserObservationConvention implements GlobalObservationConvention<UserContext> {

    @Override
    public boolean supportsContext(Observation.Context context) {
        return context instanceof UserContext;
    }

    @Override
    public String getName() {
        return "global.user.operation";
    }
}
