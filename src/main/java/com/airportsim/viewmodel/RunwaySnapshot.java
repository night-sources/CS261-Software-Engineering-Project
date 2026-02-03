package com.airportsim.viewmodel;

import com.airportsim.model.OperationalStatus;
import com.airportsim.model.RunwayMode;

public record RunwaySnapshot(int id, RunwayMode mode, OperationalStatus status, String occupiedBy)
        implements Snapshot {}
