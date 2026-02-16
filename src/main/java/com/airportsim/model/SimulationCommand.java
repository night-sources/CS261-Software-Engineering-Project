package com.airportsim.model;

public sealed interface SimulationCommand permits
    RunwayStatusChangedCommand,
    EmergencyDeclaredCommand,
    RunwayModeChanged {
    public void execute(SimulationEngine engine);
}
