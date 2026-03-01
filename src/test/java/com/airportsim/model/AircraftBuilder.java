package com.airportsim.model;

public class AircraftBuilder {
    // All the default values
    private String callsign = "BA420";
    private String operator = "British Airways";
    private String origin = "WED";
    private String destination = "LHR";
    private long fuel = 3600L; // 60 minutes
    private EmergencyStatus status = EmergencyStatus.NONE;
    private long scheduledTime = 1000L;
    private long actualTime = 1005L;
    private boolean inbound = true;  

    public AircraftBuilder withActualTime(long actualTime) {
        this.actualTime = actualTime;
        return this;
    }

    public AircraftBuilder withStatus(EmergencyStatus status) {
        this.status = status;
        return this;
    }

    public AircraftBuilder withFuel(long fuel) {
        this.status = EmergencyStatus.FUEL_LOW;
        this.fuel = fuel;
        return this;
    }

    public AircraftBuilder withInbound(boolean inbound) {
        this.inbound = inbound;
        return this;
    }

    public Aircraft build() {
        return new Aircraft(callsign, operator, origin, destination, fuel, status, scheduledTime, actualTime, inbound);
    }
}
