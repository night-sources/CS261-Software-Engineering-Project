package com.airportsim.model;

public sealed interface SimulationCommand
        permits RunwayOpStatusChangedCommand, EmergencyDeclaredCommand, RunwayModeChangedCommand {
    public void execute(SimulationEngine engine);
}
