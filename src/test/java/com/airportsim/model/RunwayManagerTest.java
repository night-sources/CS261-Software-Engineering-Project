package com.airportsim.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import com.airportsim.viewmodel.RunwaySnapshot;
import com.airportsim.viewmodel.RunwaysSnapshot;
import com.airportsim.viewmodel.Snapshot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class RunwayManagerTest {
    private RunwayManager runwayManager;
    private Runway runway1;
    private long id;
    private Runway runway2;
    private Runway runway3;
    private Aircraft inboundAircraft;
    private Aircraft outboundAircraft;

    private static final long CURRENT_TICK = 1000L;

    // Default values for runway
    private static final String DEFAULT_NUMBER = "10";
    private static final float DEFAULT_LENGTH = 2438F;
    private static final float DEFAULT_BEARING = 100F; 

    private Aircraft createAircraftWithInboundBool(boolean inbound) {
        return new AircraftBuilder().withInbound(inbound).build();
    }

    private Runway createRunwayWithMode(RunwayMode mode) {
        return new Runway(
                DEFAULT_NUMBER,
                DEFAULT_LENGTH,
                DEFAULT_BEARING,
                mode);
    }

    @BeforeEach
    void setUp() {
        runwayManager = new RunwayManager();

        // Create test runways
        runway1 = createRunwayWithMode(RunwayMode.LANDING);
        runway2 = createRunwayWithMode(RunwayMode.TAKEOFF);
        runway3 = createRunwayWithMode(RunwayMode.MIXED);

        // Get the id of runway1
        id = runway1.getId();

        // Add runways to manager
        runwayManager.addRunway(runway1);
        runwayManager.addRunway(runway2);
        runwayManager.addRunway(runway3);

        // Create test aircraft
        inboundAircraft = createAircraftWithInboundBool(true);
        outboundAircraft = createAircraftWithInboundBool(false);
    }

    @Nested 
    @DisplayName("Adding Runways")
    class AddTests {

        @Test
        @DisplayName("should reject null runways ")
        void add_handlesNullRunways() {
            assertThrows(IllegalArgumentException.class, () -> runwayManager.addRunway(null));
        }

        @Test 
        @DisplayName("should add the correct runways")
        void add_checkAddedRunways() {
            List<Runway> runways = runwayManager.getRunways();
            assertTrue(runways.containsAll(List.of(runway1, runway2, runway3)));
        }

    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        
        @Test
        @DisplayName("default constructor should create empty runway list")
        void defaultConstructor_createsEmptyList() {
            RunwayManager emptyManager = new RunwayManager();
            assertTrue(emptyManager.getRunways().isEmpty());
        }
        
        @Test
        @DisplayName("parameterized constructor should create copy of provided runways")
        void parameterizedConstructor_createsCopyOfRunways() {
            List<Runway> originalList = new ArrayList<>();
            originalList.add(runway1);
            originalList.add(runway2);
            
            RunwayManager manager = new RunwayManager(originalList);
            
            assertEquals(2, manager.getRunways().size());
            assertFalse(manager.getRunways() == originalList); // Should be a different list
        }
    }


    @Nested
    @DisplayName("Allocation Management")
    class AllocationTests {

        @Test
        @DisplayName("should allocate landing aircraft to available LANDING runway")
        void allocateLandingAircraft_toLandingRunway() {
            boolean allocated = runwayManager.allocateRunway(inboundAircraft, CURRENT_TICK);
            
            assertTrue(allocated);
            assertFalse(runway1.isAvailable()); // Runway 1 should be occupied
            assertTrue(runway2.isAvailable());  // Runway 2 should still be available
            assertTrue(runway3.isAvailable());  // Runway 3 should still be available
        }
        
        @Test
        @DisplayName("should allocate takeoff aircraft to available TAKEOFF runway")
        void allocateTakeoffAircraft_toTakeoffRunway() {
            boolean allocated = runwayManager.allocateRunway(outboundAircraft, CURRENT_TICK);
            
            assertTrue(allocated);
            assertTrue(runway1.isAvailable());  // Runway 1 should still be available
            assertFalse(runway2.isAvailable()); // Runway 2 should be occupied
            assertTrue(runway3.isAvailable());  // Runway 3 should still be available
        }
        
        @Test
        @DisplayName("should allocate to MIXED runway when preferred mode unavailable")
        void allocateToMixedRunway_whenPreferredUnavailable() {
            // Occupy the preferred runways
            runway1.occupy(inboundAircraft, CURRENT_TICK); // LANDING runway occupied
            runway2.occupy(outboundAircraft, CURRENT_TICK); // TAKEOFF runway occupied
            
            boolean allocated = runwayManager.allocateRunway(inboundAircraft, CURRENT_TICK + 1);
            
            assertTrue(allocated);
            assertFalse(runway3.isAvailable()); // MIXED runway should be occupied
        }
        
        @Test
        @DisplayName("should fail allocation when no runways available")
        void failAllocation_whenNoRunwaysAvailable() {
            // Occupy all runways
            runway1.occupy(inboundAircraft, CURRENT_TICK);
            runway2.occupy(inboundAircraft, CURRENT_TICK);
            runway3.occupy(inboundAircraft, CURRENT_TICK);
            
            boolean allocated = runwayManager.allocateRunway(inboundAircraft, CURRENT_TICK + 1);
            
            assertFalse(allocated);
        }
        
        @Test
        @DisplayName("should not allocate to non-operational runway")
        void notAllocateToNonOperationalRunway() {
            runwayManager.setRunwayStatus(id, OperationalStatus.INSPECTION);
            
            boolean allocated = runwayManager.allocateRunway(inboundAircraft, CURRENT_TICK);
            
            // Should allocate to runway 3 (MIXED) instead of closed runway 1
            assertTrue(allocated);

            // Runway1 is unavailable and are not occupied by any aircraft
            assertFalse(runway1.isAvailable());
            assertNull(runway1.getOccupiedBy()) ;

            assertFalse(runway3.isAvailable()); // MIXED runway should be occupied
        }
        
        @Test
        @DisplayName("should allocate to next available runway when first choice is occupied")
        void allocateToNextAvailable_whenFirstChoiceOccupied() {
            // Occupy runway1 (LANDING)
            runway1.occupy(inboundAircraft, CURRENT_TICK);
            
            boolean allocated = runwayManager.allocateRunway(inboundAircraft, CURRENT_TICK + 1);
            
            // Should allocate to runway3 (MIXED) since runway2 is TAKEOFF (wrong mode)
            assertTrue(allocated);
            assertFalse(runway1.isAvailable());
            assertTrue(runway2.isAvailable());
            assertFalse(runway3.isAvailable());
        }
    }

    @Nested
    @DisplayName("Runway Status")
    class RunwayStatusTests {

        @Test
        @DisplayName("should update runway status by ID")
        void updateRunwayStatus_byId() {
            runwayManager.setRunwayStatus(id, OperationalStatus.EQUIPMENT_FAILURE);
            
            assertEquals(OperationalStatus.EQUIPMENT_FAILURE, runway1.getStatus());
            assertEquals(OperationalStatus.AVAILABLE, runway2.getStatus());
        }
        
        @Test
        @DisplayName("should handle updating non-existent runway ID")
        void updateStatus_nonExistentId() {
            runwayManager.setRunwayStatus(999, OperationalStatus.EQUIPMENT_FAILURE);
            
            // All runways should remain unchanged
            assertEquals(OperationalStatus.AVAILABLE, runway1.getStatus());
            assertEquals(OperationalStatus.AVAILABLE, runway2.getStatus());
            assertEquals(OperationalStatus.AVAILABLE, runway3.getStatus());
        }
        
        @Test
        @DisplayName("should maintain status after multiple updates")
        void maintainStatus_afterMultipleUpdates() {
            runwayManager.setRunwayStatus(id, OperationalStatus.INSPECTION);
            runwayManager.setRunwayStatus(id, OperationalStatus.SNOW_CLEARANCE);
            
            assertEquals(OperationalStatus.SNOW_CLEARANCE, runway1.getStatus());
        }
    }

    @Nested 
    @DisplayName("Runway Mode")
    class RunwayModeTests {
        
        @Test
        @DisplayName("should update runway mode by ID")
        void updateRunwayMode_byId() {
            runwayManager.setRunwayMode(id, RunwayMode.MIXED);
            
            assertEquals(RunwayMode.MIXED, runway1.getMode());
            assertEquals(RunwayMode.TAKEOFF, runway2.getMode());
        }
        
        @Test
        @DisplayName("should handle updating non-existent runway ID")
        void updateMode_nonExistentId() {
            runwayManager.setRunwayMode(999, RunwayMode.MIXED);
            
            // All runways should remain unchanged
            assertEquals(RunwayMode.LANDING, runway1.getMode());
            assertEquals(RunwayMode.TAKEOFF, runway2.getMode());
            assertEquals(RunwayMode.MIXED, runway3.getMode());
        }
        
        @Test
        @DisplayName("should affect allocation after mode change")
        void affectAllocation_afterModeChange() {
            // Change runway1 from LANDING to TAKEOFF
            runwayManager.setRunwayMode(id, RunwayMode.TAKEOFF);
            
            boolean allocated = runwayManager.allocateRunway(inboundAircraft, CURRENT_TICK);
            
            // Should now allocate to runway3 (MIXED) since runway1 is TAKEOFF
            assertTrue(allocated);
            assertTrue(runway1.isAvailable());
            assertTrue(runway2.isAvailable());
            assertFalse(runway3.isAvailable());
        }
    }

    @Nested
    @DisplayName("Tick Management")
    class TickTests {

        @Test
        @DisplayName("should call tick() on every runway")
        void tick_callsEachRunway() {
            runway1.occupy(inboundAircraft, CURRENT_TICK);
            runway2.occupy(outboundAircraft, CURRENT_TICK + 5);

            runwayManager.tick(runway1.getOccupiedUntil());
            runwayManager.tick(runway2.getOccupiedUntil());

            assertNull(runway1.getOccupiedBy());
            assertNull(runway2.getOccupiedBy());
        }

        @Test
        @DisplayName("should pass the correct tick value to each runway")
        void tick_passesCorrectValue() {
            runway1.occupy(inboundAircraft, CURRENT_TICK);
            
            long finish = runway1.getOccupiedUntil();

            // Before finish, -> still occupied
            runwayManager.tick(finish - 1);
            assertNotNull(runway1.getOccupiedBy());
            
            // At finish -> becomes free
            runwayManager.tick(finish);
            assertNull(runway1.getOccupiedBy());
        }

        @Test
        @DisplayName("should do nothing when runway list is empty")
        void tick_emptyList() {
            RunwayManager empty = new RunwayManager();

            // Method should not throw any error
            empty.tick(CURRENT_TICK);
        }
    }


    @Nested
    @DisplayName("Snapshot Generation")
    class SnapshotTests {

        @Test
        @DisplayName("getSnapshot should return non-null snapshot")
        void getSnapshot_returnsNonNull() {
            Snapshot snapshot = runwayManager.getSnapshot();
            assertNotNull(snapshot);
        }

        @Test
        @DisplayName("getSnapshot should return RunwaySnapshot type")
        void getSnapshot_returnsCorrectType() {
            Snapshot snapshot = runwayManager.getSnapshot();
            assertInstanceOf(RunwaysSnapshot.class, snapshot);
        }

        @Test
        @DisplayName("getSnapshot should capture current state")
        void getSnapshot_capturesCurrentState() {
            // Add some runways and occupy one
            runwayManager.allocateRunway(inboundAircraft, CURRENT_TICK);

            RunwaysSnapshot snapshot = (RunwaysSnapshot) runwayManager.getSnapshot();

            // Verify snapshot contains all runways
            assertEquals(3, snapshot.runways().size());

            // Verify each runway snapshot is not null
            for (RunwaySnapshot runwaySnapshot : snapshot.runways()) {
                assertNotNull(runwaySnapshot);
            }
        }

        @Test
        @DisplayName("Empty manager should return snapshot with empty list")
        void getSnapshot_returnsEmptySnapshot() {
            RunwayManager emptyManager = new RunwayManager();
            RunwaysSnapshot snapshot = (RunwaysSnapshot) emptyManager.getSnapshot();
            
            assertNotNull(snapshot);
            assertTrue(snapshot.runways().isEmpty());
        }
    }
}
