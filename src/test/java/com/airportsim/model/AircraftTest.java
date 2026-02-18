package com.airportsim.model;

import static org.junit.jupiter.api.Assertions.*;

import com.airportsim.viewmodel.AircraftSnapshot;
import com.airportsim.viewmodel.Snapshot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AircraftTest {

    private Aircraft aircraft;

    private static final String DEFAULT_CALLSIGN = "BA420";
    private static final String DEFAULT_OPERATOR = "British Airways";
    private static final String DEFAULT_ORIGIN = "WED";
    private static final String DEFAULT_DESTINATION = "LHR";
    private static final long DEFAULT_FUEL = 3600L; // 60 minutes
    private static final EmergencyStatus DEFAULT_STATUS = EmergencyStatus.NONE;
    private static final long DEFAULT_SCHEDULED_TIME = 1000L;
    private static final long DEFAULT_ACTUAL_TIME = 1005L;
    private static final boolean DEFAULT_INBOUND = true;

    @BeforeEach
    void setUp() {
        aircraft = createDefaultAircraft();
    }

    private Aircraft createDefaultAircraft() {
        return new Aircraft(
                DEFAULT_CALLSIGN,
                DEFAULT_OPERATOR,
                DEFAULT_ORIGIN,
                DEFAULT_DESTINATION,
                DEFAULT_FUEL,
                DEFAULT_STATUS,
                DEFAULT_SCHEDULED_TIME,
                DEFAULT_ACTUAL_TIME,
                DEFAULT_INBOUND);
    }

    private Aircraft createAircraftWithFuel(long fuel) {
        return new Aircraft(
                DEFAULT_CALLSIGN,
                DEFAULT_OPERATOR,
                DEFAULT_ORIGIN,
                DEFAULT_DESTINATION,
                fuel,
                DEFAULT_STATUS,
                DEFAULT_SCHEDULED_TIME,
                DEFAULT_ACTUAL_TIME,
                DEFAULT_INBOUND);
    }

    private Aircraft createAircraftWithStatus(EmergencyStatus status) {
        return new Aircraft(
                DEFAULT_CALLSIGN,
                DEFAULT_OPERATOR,
                DEFAULT_ORIGIN,
                DEFAULT_DESTINATION,
                DEFAULT_FUEL,
                status,
                DEFAULT_SCHEDULED_TIME,
                DEFAULT_ACTUAL_TIME,
                DEFAULT_INBOUND);
    }

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {

        @Test
        @DisplayName("should initialise all fields correctly")
        void constructor_initialisesAllFields() {
            Aircraft a =
                    new Aircraft(
                            "TEST01",
                            "Test Airline",
                            "AAA",
                            "BBB",
                            1800L,
                            EmergencyStatus.MECHANICAL,
                            500L,
                            505L,
                            false);

            assertEquals("TEST01", a.getCallsign());
            assertEquals("Test Airline", a.getOperator());
            assertEquals("AAA", a.getOrigin());
            assertEquals("BBB", a.getDestination());
            assertEquals(1800L, a.getFuelRemaining());
            assertEquals(EmergencyStatus.MECHANICAL, a.getStatus());
            assertEquals(500L, a.getScheduledTime());
            assertEquals(505L, a.getActualTime());
            assertFalse(a.isInbound());
        }

        @Test
        @DisplayName("should set default groundSpeed to 180")
        void constructor_setsDefaultGroundSpeed() {
            assertEquals(180.0, aircraft.getGroundSpeed(), 0.001);
        }

        @Test
        @DisplayName("should set default altitude to 2000")
        void constructor_setsDefaultAltitude() {
            assertEquals(2000.0f, aircraft.getAltitude(), 0.001);
        }
    }

    @Nested
    @DisplayName("Fuel Management")
    class FuelTests {

        @Test
        @DisplayName("getFuelRemaining should return current fuel level")
        void getFuelRemaining_returnsCurrentFuel() {
            assertEquals(DEFAULT_FUEL, aircraft.getFuelRemaining());
        }

        @Test
        @DisplayName("consumeFuel should reduce fuel by specified amount")
        void consumeFuel_reducesFuel() {
            aircraft.consumeFuel(100L);
            assertEquals(DEFAULT_FUEL - 100L, aircraft.getFuelRemaining());
        }

        @Test
        @DisplayName("consumeFuel with multiple calls should accumulate")
        void consumeFuel_accumulatesOverMultipleCalls() {
            aircraft.consumeFuel(100L);
            aircraft.consumeFuel(200L);
            aircraft.consumeFuel(50L);
            assertEquals(DEFAULT_FUEL - 350L, aircraft.getFuelRemaining());
        }

        @Test
        @DisplayName("consumeFuel should clamp fuel to zero, not go negative")
        void consumeFuel_clampsToZero() {
            Aircraft lowFuelAircraft = createAircraftWithFuel(100L);
            lowFuelAircraft.consumeFuel(150L);
            assertEquals(0L, lowFuelAircraft.getFuelRemaining());
        }

        @Test
        @DisplayName("consumeFuel should throw exception for negative amount")
        void consumeFuel_throwsForNegativeAmount() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> {
                        aircraft.consumeFuel(-100L);
                    });
        }
    }

    @Nested
    @DisplayName("Emergency Status")
    class EmergencyStatusTests {

        @Test
        @DisplayName("getStatus should return current emergency status")
        void getStatus_returnsCurrentStatus() {
            assertEquals(EmergencyStatus.NONE, aircraft.getStatus());
        }

        @Test
        @DisplayName("setStatus should update emergency status")
        void setStatus_updatesStatus() {
            aircraft.setStatus(EmergencyStatus.FUEL_LOW);
            assertEquals(EmergencyStatus.FUEL_LOW, aircraft.getStatus());
        }

        @Test
        @DisplayName("declareEmergency should update emergency status")
        void declareEmergency_updatesStatus() {
            aircraft.declareEmergency(EmergencyStatus.MECHANICAL);
            assertEquals(EmergencyStatus.MECHANICAL, aircraft.getStatus());
        }

        @Test
        @DisplayName("isEmergency should return false for NONE status")
        void isEmergency_returnsFalse_forNoneStatus() {
            assertFalse(aircraft.isEmergency());
        }

        @Test
        @DisplayName("isEmergency should return true for FUEL_LOW status")
        void isEmergency_returnsTrue_forFuelLowStatus() {
            Aircraft emergencyAircraft = createAircraftWithStatus(EmergencyStatus.FUEL_LOW);
            assertTrue(emergencyAircraft.isEmergency());
        }

        @Test
        @DisplayName("isEmergency should return true for MECHANICAL status")
        void isEmergency_returnsTrue_forMechanicalStatus() {
            Aircraft emergencyAircraft = createAircraftWithStatus(EmergencyStatus.MECHANICAL);
            assertTrue(emergencyAircraft.isEmergency());
        }

        @Test
        @DisplayName("isEmergency should return true for PASSENGER_HEALTH status")
        void isEmergency_returnsTrue_forPassengerHealthStatus() {
            Aircraft emergencyAircraft = createAircraftWithStatus(EmergencyStatus.PASSENGER_HEALTH);
            assertTrue(emergencyAircraft.isEmergency());
        }

        @Test
        @DisplayName("declareEmergency can clear emergency by setting to NONE")
        void declareEmergency_canClearEmergency() {
            aircraft.declareEmergency(EmergencyStatus.MECHANICAL);
            assertTrue(aircraft.isEmergency());

            aircraft.declareEmergency(EmergencyStatus.NONE);
            assertFalse(aircraft.isEmergency());
        }
    }

    @Nested
    @DisplayName("Wait Time Calculation")
    class WaitTimeTests {

        @Test
        @DisplayName("getWaitTime should calculate time since actual entry time")
        void getWaitTime_calculatesFromActualTime() {
            long currentTick = 1505L;
            assertEquals(500L, aircraft.getWaitTime(currentTick));
        }

        @Test
        @DisplayName("getWaitTime should return zero when current tick equals scheduled time")
        void getWaitTime_returnsZero_whenNoWait() {
            assertEquals(0L, aircraft.getWaitTime(DEFAULT_ACTUAL_TIME));
        }

        @Test
        @DisplayName("getWaitTime should return negative if aircraft arrived early")
        void getWaitTime_returnsNegative_whenEarly() {
            long currentTick = 800L;
            assertEquals(-205L, aircraft.getWaitTime(currentTick));
        }
    }

    @Nested
    @DisplayName("Snapshot Generation")
    class SnapshotTests {

        @Test
        @DisplayName("getSnapshot should return non-null snapshot")
        void getSnapshot_returnsNonNull() {
            Snapshot snapshot = aircraft.getSnapshot();
            assertNotNull(snapshot);
        }

        @Test
        @DisplayName("getSnapshot should return AircraftSnapshot type")
        void getSnapshot_returnsCorrectType() {
            Snapshot snapshot = aircraft.getSnapshot();
            assertInstanceOf(AircraftSnapshot.class, snapshot);
        }

        @Test
        @DisplayName("getSnapshot should capture current state")
        void getSnapshot_capturesCurrentState() {
            aircraft.consumeFuel(500L);
            aircraft.declareEmergency(EmergencyStatus.FUEL_LOW);

            AircraftSnapshot snapshot = (AircraftSnapshot) aircraft.getSnapshot();

            assertEquals(DEFAULT_CALLSIGN, snapshot.callsign());
            assertEquals(DEFAULT_FUEL - 500L, snapshot.fuelRemaining());
            assertEquals(EmergencyStatus.FUEL_LOW, snapshot.status());
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("should handle zero fuel at construction")
        void constructor_handlesZeroFuel() {
            Aircraft zeroFuelAircraft = createAircraftWithFuel(0L);
            assertEquals(0L, zeroFuelAircraft.getFuelRemaining());
        }

        @Test
        @DisplayName("should handle maximum long value for fuel")
        void constructor_handlesMaxFuel() {
            Aircraft maxFuelAircraft = createAircraftWithFuel(Long.MAX_VALUE);
            assertEquals(Long.MAX_VALUE, maxFuelAircraft.getFuelRemaining());
        }
    }
}
