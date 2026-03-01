package com.airportsim.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TakeOffQueueTest {
    private TakeoffQueue takeoffQueue;

    @BeforeEach
    public void setUp() {
        takeoffQueue = new TakeoffQueue();
    }

    private Aircraft createAircraftWithActualTime(long actualTime) {
        return new AircraftBuilder().withActualTime(actualTime).build();
    }

    @Nested
    @DisplayName("RemoveExpired")
    class RemoveExpired {
        @Test
        @DisplayName("should return empty list")
        void removeExpired_noExceedMaxWait() {
            takeoffQueue.add(createAircraftWithActualTime(950));
            takeoffQueue.add(createAircraftWithActualTime(1000));

            List<Aircraft> cancelled = takeoffQueue.removeExpired(10, 1050);

            assertTrue(cancelled.isEmpty());
            assertEquals(2, takeoffQueue.size());
        }

        @Test 
        @DisplayName("should return cancelled list of aircrafts")
        void removeExpired_someExceedMaxWait() {
            Aircraft aircraft1 = createAircraftWithActualTime(1000);
            Aircraft aircraft2 = createAircraftWithActualTime(800);

            takeoffQueue.add(aircraft1);
            takeoffQueue.add(aircraft2);

            List<Aircraft> cancelled = takeoffQueue.removeExpired(10, 1000);

            // Check if the correct aircrafts are expired
            assertTrue(cancelled.contains(aircraft2));
            assertFalse(cancelled.contains(aircraft1));

            assertEquals(1, takeoffQueue.size());
        }
    }
}
