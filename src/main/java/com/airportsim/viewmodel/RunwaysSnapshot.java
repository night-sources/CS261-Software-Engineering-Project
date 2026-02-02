package com.airportsim.viewmodel;

import java.util.List;

public record RunwaysSnapshot(List<RunwaySnapshot> runways) implements Snapshot {
    public RunwaysSnapshot {
        runways = List.copyOf(runways);
    }
}
