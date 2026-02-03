package com.airportsim.viewmodel;

public record StatisticsSnapshot(double totalDelay, int totalDiversions, int cancellations
        // ...
        ) implements Snapshot {}
