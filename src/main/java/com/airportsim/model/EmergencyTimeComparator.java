package com.airportsim.model;

import java.util.Comparator;

public class EmergencyTimeComparator implements Comparator<Aircraft> {
    @Override
    public int compare(Aircraft aircraftLeft, Aircraft aircraftRight) {
        // Priority: low fuel > mechanical failure > passenger health > none
        EmergencyStatus left = aircraftLeft.getStatus();
        EmergencyStatus right = aircraftRight.getStatus();

        if (left == right) return 0;
        if (left == EmergencyStatus.FUEL_LOW) return 1;
        if (right == EmergencyStatus.FUEL_LOW) return -1;
        if (left == EmergencyStatus.NONE) return -1;
        if (right == EmergencyStatus.NONE) return 1;
        if (left == EmergencyStatus.MECHANICAL) return 1;
        return -1;
    }
}
