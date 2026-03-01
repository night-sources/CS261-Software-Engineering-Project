package com.airportsim.model;

import static org.junit.jupiter.api.Assertions.*;

import com.airportsim.viewmodel.RunwaySnapshot;
import com.airportsim.viewmodel.Snapshot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class RunwayTest {
    private Runway runway;

    private static final long CURRENT_TICK = 1000L;

    private static final String DEFAULT_NUMBER = "10";
    private static final float DEFAULT_LENGTH = 2438F;
    private static final float DEFAULT_BEARING = 100F; 
    private static final RunwayMode DEFAULT_MODE = RunwayMode.LANDING;

    @BeforeEach
    void setUp() {
        runway = createDefaultRunway();
    }

    private Aircraft createDefaultAircraft() {
        return new AircraftBuilder().build();
    }

    private Runway createDefaultRunway() {
        return new Runway(
            DEFAULT_NUMBER, 
            DEFAULT_LENGTH, 
            DEFAULT_BEARING, 
            DEFAULT_MODE);
    }

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {

        @Test
        @DisplayName("should initialise all fields correctly")
        void constructor_initialisesAllFields() {
            assertEquals("10", runway.getRunwayNumber());
            assertEquals(2438F, runway.getLength());
            assertEquals(100F, runway.getBearing());
            assertEquals(RunwayMode.LANDING, runway.getMode());
        }

    }

    @Nested
    @DisplayName("Availibility")
    class AvailibilityTests {

        @Test
        @DisplayName("should return true when available")
        void availibility_true() {
            assertTrue(runway.isAvailable());
        }

        @Test
        @DisplayName("should return false when Operational status is not available")
        void availibility_falseStatus() {
            runway.setStatus(OperationalStatus.EQUIPMENT_FAILURE);
            assertTrue(!runway.isAvailable());
        }

        @Test
        @DisplayName("should return false when runway is occupied")
        void availibility_falseOccupied() {
            Aircraft a = createDefaultAircraft();
            runway.occupy(a, CURRENT_TICK);
            assertTrue(!runway.isAvailable());
        }
    }

    @Nested
    @DisplayName("Occupiance Management")  
    class OccupyTests {
        
        @Test
        @DisplayName("should occupy the correct aircraft")
        void occupy_rightAircraft() {
            Aircraft a = createDefaultAircraft();

            runway.occupy(a, CURRENT_TICK);

            assertNotNull(runway);
            assertEquals(a, runway.getOccupiedBy());
        }

        @Test
        @DisplayName("should return the correct occupiedUntil value")
        void occupy_rightOccupiedUntil() {
            Aircraft a = createDefaultAircraft();

            runway.occupy(a, CURRENT_TICK);

            long expected = CURRENT_TICK + (long) (runway.getLength() / a.getGroundSpeed());
            assertEquals(expected, runway.getOccupiedUntil());
        }

        @Test
        @DisplayName("calling occupy() twice overwrites previous values")
        void occupy_overwritesPrevious() {
            Aircraft a1 = createDefaultAircraft();
            Aircraft a2 = createDefaultAircraft();

            runway.occupy(a1, CURRENT_TICK);
            long firstUntil = runway.getOccupiedUntil();

            runway.occupy(a2, CURRENT_TICK + 10);
            long secondUntil = runway.getOccupiedUntil();

            assertEquals(a2, runway.getOccupiedBy());
            assertNotEquals(firstUntil, secondUntil);
        }

        @Test 
        @DisplayName("should handle null aircraft without crashing") 
        void occupy_nullAircraftAllowed() {
            runway.occupy(null, CURRENT_TICK);

            assertNull(runway.getOccupiedBy());
        }

        @Test
        @DisplayName("calling occupy() twice with the same tick should produce same occupiedUntil")
        void occupy_differentRunway_sameTick() {
            Aircraft a1 = createDefaultAircraft();
            Aircraft a2 = createDefaultAircraft();
            Runway runway2 = createDefaultRunway();

            runway.occupy(a1, CURRENT_TICK);
            runway2.occupy(a2, CURRENT_TICK);

            assertEquals(runway.getOccupiedUntil(), runway2.getOccupiedUntil());
        }
    }

    @Nested
    @DisplayName("Tick Management")
    class TickTests {

        @Test
        @DisplayName("should clear aircraft when currentTick >= occupiedUntil")
        void tick_clearsWhenTimePassed() {
            Aircraft a = createDefaultAircraft();
            runway.occupy(a, CURRENT_TICK);

            runway.tick(runway.getOccupiedUntil() + 10);

            assertNull(runway.getOccupiedBy());
            assertEquals(0, runway.getOccupiedUntil());
        }

        @Test
        @DisplayName("should not clear aircraft when currentTick < occupiedUntil")
        void tick_doesNotClearTooEarly() {
            Aircraft a = createDefaultAircraft();
            runway.occupy(a, CURRENT_TICK);

            long before = runway.getOccupiedUntil();
            runway.tick(before - 10);

            assertNotNull(runway.getOccupiedBy());
            assertEquals(before, runway.getOccupiedUntil());
        }

        @Test
        @DisplayName("should do nothing when runway is already empty")
        void tick_noOpWhenEmpty() { 
            runway.tick(CURRENT_TICK);

            assertNull(runway.getOccupiedBy());
            assertEquals(0, runway.getOccupiedUntil());
        }

        @Test
        @DisplayName("should clear aircraft exactly at occupiedUntil")
        void tick_clearsAtBoundary() { 
            Aircraft a = createDefaultAircraft();
            runway.occupy(a, CURRENT_TICK);

            runway.tick(runway.getOccupiedUntil());

            assertNull(runway.getOccupiedBy());
            assertEquals(0, runway.getOccupiedUntil());
         }

        @Test
        @DisplayName("should remain stable after multiple ticks")
        void tick_idempotentAfterClearing() { 
            Aircraft a = createDefaultAircraft();
            runway.occupy(a, CURRENT_TICK);

            long clearTick = runway.getOccupiedUntil();
            runway.tick(clearTick);
            runway.tick(clearTick + 100);

            assertNull(runway.getOccupiedBy());
            assertEquals(0, runway.getOccupiedUntil());
        }

        @Test
        @DisplayName("should handle large tick values safely")
        void tick_handlesLargeValues() { 
            Aircraft a = createDefaultAircraft();
            runway.occupy(a, 0);
            
            runway.tick(Long.MAX_VALUE);

            assertNull(runway.getOccupiedBy());
            assertEquals(0, runway.getOccupiedUntil());
         }
    }

    @Nested
    @DisplayName("Snapshot Generation")
    class SnapshotTests {

        @Test
        @DisplayName("getSnapshot should return non-null snapshot")
        void getSnapshot_returnsNonNull() {
            Snapshot snapshot = runway.getSnapshot();
            assertNotNull(snapshot);
        }

        @Test
        @DisplayName("getSnapshot should return AircraftSnapshot type")
        void getSnapshot_returnsCorrectType() {
            Snapshot snapshot = runway.getSnapshot();
            assertInstanceOf(RunwaySnapshot.class, snapshot);
        }

        @Test
        @DisplayName("getSnapshot should capture current state")
        void getSnapshot_capturesCurrentState() {
            runway.setStatus(OperationalStatus.EQUIPMENT_FAILURE);

            RunwaySnapshot snapshot = (RunwaySnapshot) runway.getSnapshot();

            assertEquals(DEFAULT_NUMBER, snapshot.runwayNumber());
            assertEquals(DEFAULT_BEARING, snapshot.bearing());
            assertEquals(OperationalStatus.EQUIPMENT_FAILURE, snapshot.status());
        }
    }

    
}
