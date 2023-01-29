package com.programacho;

import brave.Tracing;
import brave.propagation.StrictCurrentTraceContext;
import brave.sampler.Sampler;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.observation.DefaultMeterObservationHandler;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.brave.bridge.BraveBaggageManager;
import io.micrometer.tracing.brave.bridge.BraveCurrentTraceContext;
import io.micrometer.tracing.brave.bridge.BraveTracer;
import io.micrometer.tracing.handler.DefaultTracingObservationHandler;

import java.util.concurrent.TimeUnit;

public class MicrometerObservationGymApplication {

    public static void main(String[] args) {
        // Micrometer Observation API
        micrometerObservationApi();

        // with Micrometer API
        withMicrometerApi();

        // with Micrometer Tracing API
        withMicrometerTracingApi();
    }

    private static void micrometerObservationApi() {
        ObservationRegistry registry = ObservationRegistry.create();
        registry.observationConfig()
                .observationHandler(new LoggingObservationHandler())
                .observationConvention(new GlobalUserObservationConvention())
                .observationFilter(new UserObservationFilter())
                .observationPredicate((name, context) -> {
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

    private static void withMicrometerApi() {
        MeterRegistry meterRegistry = new SimpleMeterRegistry();

        ObservationRegistry observationRegistry = ObservationRegistry.create();
        observationRegistry.observationConfig().observationHandler(
                // DefaultMeterObservationHandlerでObservationRegistryにMeterRegistryを登録する。
                new DefaultMeterObservationHandler(meterRegistry)
        );

        Observation observation = Observation.start("programacho.operation", observationRegistry);

        try (Observation.Scope scope = observation.openScope()) {
            sleep(1_000);
        } finally {
            observation.stop();
        }

        printTimer(meterRegistry.find("programacho.operation").timer());
    }

    private static void withMicrometerTracingApi() {
        StrictCurrentTraceContext traceContext = StrictCurrentTraceContext.create();

        BraveCurrentTraceContext traceContextBridge = new BraveCurrentTraceContext(traceContext);

        Tracing tracing = Tracing.newBuilder()
                .currentTraceContext(traceContext)
                .traceId128Bit(true)
                .sampler(Sampler.NEVER_SAMPLE)
                .build();

        brave.Tracer tracer = tracing.tracer();

        // Zipkinにトレースデータを送信しない最低限のTracerBridgeを生成する。
        Tracer tracerBridge = new BraveTracer(tracer, traceContextBridge, new BraveBaggageManager());

        ObservationRegistry observationRegistry = ObservationRegistry.create();
        observationRegistry.observationConfig().observationHandler(
                // DefaultTracingObservationHandlerでObservationRegistryにTracerを登録する。
                new DefaultTracingObservationHandler(tracerBridge)
        );

        Observation observation = Observation.start("programacho.operation", observationRegistry);

        try (Observation.Scope scope = observation.openScope()) {
            printTraceContext(tracerBridge.currentTraceContext().context());
        } finally {
            observation.stop();
        }
    }

    private static void printTimer(Timer timer) {
        System.out.println("Count: " + timer.count());
        System.out.println("Max: " + timer.max(TimeUnit.SECONDS));
        System.out.println("Average: " + timer.mean(TimeUnit.SECONDS));
        System.out.println("Sum: " + timer.totalTime(TimeUnit.SECONDS));
    }

    private static void printTraceContext(TraceContext traceContext) {
        System.out.println("Trace ID: " + traceContext.traceId());
        System.out.println("Span ID: " + traceContext.spanId());
        System.out.println("Parent ID: " + traceContext.parentId());
        System.out.println("Sampled: " + traceContext.sampled());
    }

    private static void sleep(int timeout) {
        try {
            TimeUnit.MILLISECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
