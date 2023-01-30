package com.programacho;

import io.micrometer.common.KeyValue;
import io.micrometer.common.KeyValues;
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

    @Override
    public KeyValues getLowCardinalityKeyValues(UserContext context) {
        context.addLowCardinalityKeyValue(KeyValue.of("convention-key", "convention-value"));

        return context.getLowCardinalityKeyValues();
    }

    @Override
    public KeyValues getHighCardinalityKeyValues(UserContext context) {
        context.addHighCardinalityKeyValue(KeyValue.of("convention-key", "convention-value"));

        return context.getHighCardinalityKeyValues();
    }
}
