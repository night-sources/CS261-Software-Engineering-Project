package com.airportsim.model;

public record EmergencyDeclaredCommand(String callsign, EmergencyStatus emergency)
        implements SimulationCommand {
    public void execute(SimulationEngine engine) {
        engine.getAircraftManager().setEmergencyStatus(callsign, emergency);
    }
}
