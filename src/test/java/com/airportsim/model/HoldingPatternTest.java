package com.airportsim.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class HoldingPatternTest {
    private HoldingPattern holdingPattern;
    private EmergencyTimeComparator comparator;

    @BeforeEach
    public void setUp() {
        comparator = new EmergencyTimeComparator();
        holdingPattern = new HoldingPattern(comparator);
    }

    private Aircraft createAircraftWithStatus(EmergencyStatus status) {
        return new AircraftBuilder().withStatus(status).build();
    }

    private Aircraft createAircraftWithFuel(long fuel) {
        return new AircraftBuilder().withFuel(fuel).build();
    }

    @Nested
    @DisplayName("RemoveExpired")
    class RemoveExpired {
        @Test
        @DisplayName("should returns empty list") 
        void removeExpired_noLowFuel_noExceedMaxWait() {
            holdingPattern.add(createAircraftWithStatus(EmergencyStatus.NONE));
            holdingPattern.add(createAircraftWithStatus(EmergencyStatus.MECHANICAL));

            List<Aircraft> diverted = holdingPattern.removeExpired(10, 1000);
            
            assertTrue(diverted.isEmpty());
            assertEquals(2, holdingPattern.size());
        }

        @Test
        @DisplayName("should return diverted list of low fuel aircraft")
        void removeExpired_someLowFuel_noExceedMaxWait() {
            Aircraft aircraft1 = createAircraftWithStatus(EmergencyStatus.NONE);
            Aircraft aircraft2 = new AircraftBuilder().withFuel(10).build();

            holdingPattern.add(aircraft1);
            holdingPattern.add(aircraft2);

            List<Aircraft> diverted = holdingPattern.removeExpired(10, 1000);

            // Check if the right aircrafts are expired 
            assertTrue(diverted.contains(aircraft2));
            assertFalse(diverted.contains(aircraft1));

            assertEquals(1, holdingPattern.size());
        }


        @Test
        @DisplayName("should return diverted list of aircraft with exceede max wait")
        void removeExpired_noLowFuel_allExceedMaxWait() {
            Aircraft aircraft1 = createAircraftWithStatus(EmergencyStatus.NONE);
            Aircraft aircraft2 = createAircraftWithStatus(EmergencyStatus.NONE);

            holdingPattern.add(aircraft1);
            holdingPattern.add(aircraft2);

            List<Aircraft> diverted = holdingPattern.removeExpired(10, 1020);

            // Check if the right aircrafts are expired 
            assertTrue(diverted.contains(aircraft2));
            assertTrue(diverted.contains(aircraft1));

            assertEquals(0, holdingPattern.size());
        }
    }

    @Nested
    @DisplayName("UpdatePriority")
    class UpdatePriority {
        @Test
        @DisplayName("should returns empty list") 
        void updatePriority_ConsumesFuelAndMaintainsQueue() {
            Aircraft aircraft = createAircraftWithFuel(100);
            holdingPattern.add(aircraft);

            assertEquals(1, holdingPattern.size());
            long initialFuel = aircraft.getFuelRemaining();

            holdingPattern.updatePriority(aircraft);

            assertEquals(initialFuel - 10, aircraft.getFuelRemaining(), "Fuel should decrease by 10");
            assertEquals(1, holdingPattern.size(), "Aircraft should remain in queue");
        }

        @Test
        public void updatePriority_LeadsToRemovalWhenFuelExpires() {
            Aircraft aircraft = createAircraftWithFuel(15);
            holdingPattern.add(aircraft);
            
            // Initially not expired
            assertTrue(holdingPattern.removeExpired(10, 1000).isEmpty());
            
            // Update until fuel drops below threshold
            holdingPattern.updatePriority(aircraft); // 15 → 5
            
            // Now should be removed
            assertEquals(EmergencyStatus.FUEL_LOW, aircraft.getStatus());
            assertEquals(1, holdingPattern.removeExpired(10, 1000).size());
            assertEquals(0, holdingPattern.size());
        }       
    }
}
