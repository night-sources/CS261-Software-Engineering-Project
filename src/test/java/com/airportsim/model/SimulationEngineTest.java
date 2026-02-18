package com.airportsim.model;

import static org.junit.jupiter.api.Assertions.*;

import com.airportsim.viewmodel.WorldState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SimulationEngineTest {

    private SimulationEngine engine;

    @BeforeEach
    void setUp() {
        RunwayManager runwayManager = new RunwayManager();
        AircraftManager aircraftManager = new AircraftManager(runwayManager);
        StatisticsManager statisticsManager = new StatisticsManager();
        engine = new SimulationEngine(aircraftManager, runwayManager, statisticsManager);
    }

    @Nested
    @DisplayName("Tick Loop")
    class TickLoopTests {

        @Test
        @DisplayName("run() should increment currentTick over time")
        void run_incrementsCurrentTick() throws InterruptedException {
            engine.setTickIntervalMs(10); // Fast ticks for testing
            engine.setPaused(false);

            Thread simThread = new Thread(() -> engine.run());
            simThread.start();

            Thread.sleep(100);

            engine.stop();
            simThread.join(1000);

            assertTrue(
                    engine.getCurrentTick() > 0,
                    "Expected currentTick to be incremented, but was " + engine.getCurrentTick());
        }

        @Test
        @DisplayName("run() should not increment tick when paused")
        void run_doesNotIncrementWhenPaused() throws InterruptedException {
            engine.setTickIntervalMs(10);
            engine.setPaused(true); // Paused

            Thread simThread = new Thread(() -> engine.run());
            simThread.start();

            Thread.sleep(100);

            engine.stop();
            simThread.join(1000);

            assertEquals(
                    0, engine.getCurrentTick(), "Expected currentTick to remain 0 when paused");
        }

        @Test
        @DisplayName("stop() should terminate the run loop")
        void stop_terminatesRunLoop() throws InterruptedException {
            engine.setTickIntervalMs(10);
            engine.setPaused(false);

            Thread simThread = new Thread(() -> engine.run());
            simThread.start();

            assertTrue(simThread.isAlive(), "Thread should be running");

            engine.stop();
            simThread.join(1000);

            assertFalse(simThread.isAlive(), "Thread should have terminated after stop()");
        }

        @Test
        @DisplayName("run() should respect tickIntervalMs timing")
        void run_respectsTickInterval() throws InterruptedException {
            engine.setTickIntervalMs(50); // 50ms per tick = ~20 ticks per second
            engine.setPaused(false);

            Thread simThread = new Thread(() -> engine.run());
            simThread.start();

            Thread.sleep(275); // Should allow roughly 5 ticks (50ms * 5 = 250ms, plus buffer)

            engine.stop();
            simThread.join(1000);

            long ticks = engine.getCurrentTick();
            // Allow some tolerance for timing imprecision
            assertTrue(
                    ticks >= 4 && ticks <= 7,
                    "Expected ~5 ticks in 275ms at 50ms interval, but got " + ticks);
        }
    }

    @Nested
    @DisplayName("Snapshot Sharing")
    class SnapshotTests {

        @Test
        @DisplayName("getLatestSnapshot() returns null before run() is called")
        void getLatestSnapshot_returnsNullBeforeRun() {
            assertNull(engine.getLatestSnapshot());
        }

        @Test
        @DisplayName("getLatestSnapshot() returns snapshot after run() starts")
        void getLatestSnapshot_returnsSnapshotAfterRun() throws InterruptedException {
            engine.setTickIntervalMs(10);
            engine.setPaused(false);

            Thread simThread = new Thread(() -> engine.run());
            simThread.start();

            Thread.sleep(50);

            WorldState snapshot = engine.getLatestSnapshot();
            assertNotNull(snapshot, "Expected a snapshot to be available");

            engine.stop();
            simThread.join(1000);
        }

        @Test
        @DisplayName("getLatestSnapshot() tick matches simulation progress")
        void getLatestSnapshot_tickMatchesProgress() throws InterruptedException {
            engine.setTickIntervalMs(10);
            engine.setPaused(false);

            Thread simThread = new Thread(() -> engine.run());
            simThread.start();

            Thread.sleep(100);

            engine.stop();
            simThread.join(1000);

            WorldState snapshot = engine.getLatestSnapshot();
            // Snapshot tick should be close to currentTick (might be off by 1 due to timing)
            assertTrue(
                    Math.abs(snapshot.tick() - engine.getCurrentTick()) <= 1,
                    "Snapshot tick should match currentTick");
        }
    }

    @Nested
    @DisplayName("Pause and Resume")
    class PauseResumeTests {

        @Test
        @DisplayName("resuming from pause should continue incrementing ticks")
        void resume_continuesIncrementingTicks() throws InterruptedException {
            engine.setTickIntervalMs(10);
            engine.setPaused(true);

            Thread simThread = new Thread(() -> engine.run());
            simThread.start();

            Thread.sleep(50);
            assertEquals(0, engine.getCurrentTick(), "Should not tick while paused");

            engine.setPaused(false);
            Thread.sleep(100);

            engine.stop();
            simThread.join(1000);

            assertTrue(engine.getCurrentTick() > 0, "Should have ticked after resume");
        }
    }

    @Nested
    @DisplayName("Command Processing")
    class CommandProcessingTests {

        @Test
        @DisplayName("commands are processed during tick")
        void commands_processedDuringTick() throws InterruptedException {
            // Add a runway so we can change its status
            Runway runway = new Runway(1, "09L", 3000f, 90f, RunwayMode.LANDING);
            engine.getRunwayManager().addRunway(runway);

            engine.setTickIntervalMs(10);
            engine.setPaused(false);

            engine.setRunwayOpStatus(1, OperationalStatus.INSPECTION);

            Thread simThread = new Thread(() -> engine.run());
            simThread.start();

            Thread.sleep(50);

            engine.stop();
            simThread.join(1000);

            // Verify the command was processed
            assertEquals(OperationalStatus.INSPECTION, runway.getStatus());
        }
    }

    @Nested
    @DisplayName("Tick Interval Configuration")
    class TickIntervalTests {

        @Test
        @DisplayName("setTickIntervalMs(0) runs at maximum speed")
        void setTickInterval_zeroRunsMaxSpeed() throws InterruptedException {
            engine.setTickIntervalMs(0);
            engine.setPaused(false);

            Thread simThread = new Thread(() -> engine.run());
            simThread.start();

            Thread.sleep(100);

            engine.stop();
            simThread.join(1000);

            assertTrue(
                    engine.getCurrentTick() > 50,
                    "Expected many ticks at max speed, but got " + engine.getCurrentTick());
        }

        @Test
        @DisplayName("negative interval is treated as 0")
        void setTickInterval_negativeBecomesZero() {
            engine.setTickIntervalMs(-100);
            assertEquals(0, engine.getTickIntervalMs());
        }
    }
}
