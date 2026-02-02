package com.airportsim.model;

public record SimulationEvent(
        long timestamp,
        EventType type,
        SimulationEventSubject payload
) {}
