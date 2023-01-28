package com.programacho;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;

public class LoggingObservationHandler implements ObservationHandler<UserContext> {
    @Override
    public void onStart(UserContext context) {
        System.out.println("onStart: " + (UserContext) context);
    }

    @Override
    public void onError(UserContext context) {
        System.out.println("onError: " + context);
    }

    @Override
    public void onEvent(final Observation.Event event, UserContext context) {
        System.out.println("onEvent: " + context);
        System.out.println("Event: " + event);
    }

    @Override
    public void onScopeOpened(UserContext context) {
        System.out.println("onScopeOpened: " + context);
    }

    @Override
    public void onScopeClosed(UserContext context) {
        System.out.println("onScopeClosed: " + context);
    }

    @Override
    public void onStop(UserContext context) {
        System.out.println("onStop: " + context);
    }

    @Override
    public boolean supportsContext(final Observation.Context context) {
        return true;
    }
}
