package com.airportsim.viewmodel;

import com.airportsim.model.EmergencyStatus;

public record AircraftSnapshot(
        long id,
        String callsign,
        String operator,
        String origin,
        String destination,
        double groundSpeed,
        float altitude,
        long fuelRemaining,
        EmergencyStatus status,
        long scheduledTime,
        long actualTime,
        boolean inbound)
        implements Snapshot {}
