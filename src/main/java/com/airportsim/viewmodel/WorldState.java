package com.airportsim.viewmodel;

public record WorldState(
        long tick,
        AircraftsSnapshot aircraft,
        RunwaysSnapshot runways,
        StatisticsSnapshot statistics
) implements Snapshot {}
