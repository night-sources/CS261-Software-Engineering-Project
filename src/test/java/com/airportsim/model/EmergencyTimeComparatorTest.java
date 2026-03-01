package com.airportsim.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class EmergencyTimeComparatorTest {
    private EmergencyTimeComparator comparator;
    private int result;

    @BeforeEach
    public void setUp() {
        comparator = new EmergencyTimeComparator();
    }

    private Aircraft createAircraftWithStatus(EmergencyStatus status) {
        return new AircraftBuilder().withStatus(status).build();
    }

    private Aircraft createAircraftWithActualTime(long actualTime) {
        return new AircraftBuilder().withActualTime(actualTime).build();
    }

    @Nested
    @DisplayName("ReturnsNegative")
    class ReturnsPositive {
        @Test
        void returnsPositive_FuelMech() {
            Aircraft aircraft1 = createAircraftWithStatus(EmergencyStatus.FUEL_LOW);
            Aircraft aircraft2 = createAircraftWithStatus(EmergencyStatus.MECHANICAL);

            result = comparator.compare(aircraft1, aircraft2);
            assertTrue(result < 0);
        }

        @Test
        void returnsPositive_FuelHealth() {
            Aircraft aircraft1 = createAircraftWithStatus(EmergencyStatus.FUEL_LOW);
            Aircraft aircraft2 = createAircraftWithStatus(EmergencyStatus.PASSENGER_HEALTH);

            result = comparator.compare(aircraft1, aircraft2);
            assertTrue(result < 0);
        }
                @Test
        void returnsPositive_MechHealth() {
            Aircraft aircraft1 = createAircraftWithStatus(EmergencyStatus.MECHANICAL);
            Aircraft aircraft2 = createAircraftWithStatus(EmergencyStatus.PASSENGER_HEALTH);

            result = comparator.compare(aircraft1, aircraft2);
            assertTrue(result < 0);
        }
                @Test
        void returnsPositive_FuelNone() {
            Aircraft aircraft1 = createAircraftWithStatus(EmergencyStatus.FUEL_LOW);
            Aircraft aircraft2 = createAircraftWithStatus(EmergencyStatus.NONE);

            result = comparator.compare(aircraft1, aircraft2);
            assertTrue(result < 0);
        }

    }

    @Nested
    @DisplayName("ReturnsPositive")
    class ReturnsNegative {
        @Test
        void returnsNegative_MechFuel() {
            Aircraft aircraft1 = createAircraftWithStatus(EmergencyStatus.FUEL_LOW);
            Aircraft aircraft2 = createAircraftWithStatus(EmergencyStatus.MECHANICAL);

            result = comparator.compare(aircraft2, aircraft1);
            assertTrue(result > 0);
        }

        @Test
        void returnsNegative_HealthFuel() {
            Aircraft aircraft1 = createAircraftWithStatus(EmergencyStatus.PASSENGER_HEALTH);
            Aircraft aircraft2 = createAircraftWithStatus(EmergencyStatus.FUEL_LOW);

            result = comparator.compare(aircraft1, aircraft2);
            assertTrue(result > 0);
        }

        @Test
        void returnsNegative_HealthMech() {
            Aircraft aircraft1 = createAircraftWithStatus(EmergencyStatus.PASSENGER_HEALTH);
            Aircraft aircraft2 = createAircraftWithStatus(EmergencyStatus.MECHANICAL);

            result = comparator.compare(aircraft1, aircraft2);
            assertTrue(result > 0);
        }

        @Test
        void returnsNegative_NoneFuel() {
            Aircraft aircraft1 = createAircraftWithStatus(EmergencyStatus.NONE);
            Aircraft aircraft2 = createAircraftWithStatus(EmergencyStatus.FUEL_LOW);

            result = comparator.compare(aircraft1, aircraft2);
            assertTrue(result > 0);
        }
    }

    @Nested
    @DisplayName("Same EmergencyStatus")
    class SameStatusTest {

        @Test
        @DisplayName("should returns zero")
        void sameStatus_returnsZero() {
            Aircraft aircraft1 = createAircraftWithStatus(EmergencyStatus.NONE);
            Aircraft aircraft2 = createAircraftWithStatus(EmergencyStatus.NONE);

            result = comparator.compare(aircraft1, aircraft2);
            assertTrue(result == 0);
        }

        @Test
        @DisplayName("should return negative")
        void sameStatus_leftArriveEarlier() {
            Aircraft aircraft1 = createAircraftWithActualTime(1000);
            Aircraft aircraft2 = createAircraftWithActualTime(1010);

            result = comparator.compare(aircraft1, aircraft2);
            assertTrue(result < 0);
        }

        @Test
        @DisplayName("should return positive")
        void sameStatus_leftArriveLater() {
            Aircraft aircraft1 = createAircraftWithActualTime(1020);
            Aircraft aircraft2 = createAircraftWithActualTime(1010);

            result = comparator.compare(aircraft1, aircraft2);
            assertTrue(result > 0);
        }

        @Test
        @DisplayName("should return zero")
        void sameStatus_bothArriveAtSameTime() {
            Aircraft aircraft1 = createAircraftWithActualTime(1000);
            Aircraft aircraft2 = createAircraftWithActualTime(1000);

            result = comparator.compare(aircraft1, aircraft2);
            assertTrue(result == 0);
        }
    }
}
