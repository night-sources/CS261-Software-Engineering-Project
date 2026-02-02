package com.airportsim.viewmodel;

import com.airportsim.model.EmergencyStatus;

public record AircraftSnapshot(
        String callsign,
        long fuelRemaining,
        EmergencyStatus status,
        long scheduledTime
) implements Snapshot {}
