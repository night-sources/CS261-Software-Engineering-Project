package com.airportsim.viewmodel;

import com.airportsim.model.EmergencyStatus;

public record AircraftSnapshot(
        String callsign,
        String operator,
        String origin,
        String destination,
        long fuelRemaining,
        EmergencyStatus status,
        long scheduledTime,
        long actualTime,
        boolean inbound)
        implements Snapshot {}
