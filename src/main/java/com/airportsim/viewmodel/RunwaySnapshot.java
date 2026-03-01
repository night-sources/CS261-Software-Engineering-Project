package com.airportsim.viewmodel;

import com.airportsim.model.OperationalStatus;
import com.airportsim.model.RunwayMode;

public record RunwaySnapshot(
        long id,
        String runwayNumber,
        float length,
        float bearing, 
        RunwayMode mode, 
        OperationalStatus status, 
        String occupiedBy,
        long occupiedUntil)
        implements Snapshot {}
