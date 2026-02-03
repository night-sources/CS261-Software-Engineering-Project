package com.airportsim.viewmodel;

import java.util.List;

public record AircraftsSnapshot(List<AircraftSnapshot> activeAircraft) implements Snapshot {
    public AircraftsSnapshot {
        activeAircraft = List.copyOf(activeAircraft);
    }
}
