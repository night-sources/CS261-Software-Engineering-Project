package com.airportsim.model;

import java.util.Comparator;

public class EmergencyTimeComparator implements Comparator<Aircraft> {
    @Override
    public int compare(Aircraft left, Aircraft right) {
        // Priority order (high to low): Fuel low > mechanical > passenger health > none
        // Same emergency means prioritise by longer wait time
        int leftPriority = getEmergencyPriority(left.getStatus());
        int rightPriority = getEmergencyPriority(right.getStatus());

        if (leftPriority != rightPriority) {
            return Integer.compare(rightPriority, leftPriority);
        }

        return Long.compare(left.getActualTime(), right.getActualTime());
    }

    private int getEmergencyPriority(EmergencyStatus status) {
        return switch (status) {
            case FUEL_LOW -> 4;
            case MECHANICAL -> 3;
            case PASSENGER_HEALTH -> 2;
            case NONE -> 1;
        };
    }
}
