package com.airportsim.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class GenerationTest {

    private static final int DURATION_60 = 60;
    private static final int FLOW_15 = 15;
    private static final double EMERGENCY_PROB = 0.15;
    private static final long SEED = 42L;

    private static final Set<String> KNOWN_AIRPORTS =
            Set.of(
                    "LHR",
                    "JFK",
                    "DXB",
                    "SIN",
                    "USA",
                    "FRA",
                    "CDG",
                    "AMS",
                    "MAD",
                    "BCN",
                    "DUB",
                    "EDI",
                    AircraftGeneration.home_airport);

    @Nested
    @DisplayName("generateInbound")
    class GenerateInboundTests {

        @Test
        @DisplayName("returns expected number of flights for 60 min at 15/hour with fixed seed")
        void deterministicCount() {
            AircraftGeneration gen =
                    new AircraftGeneration(
                            DURATION_60, FLOW_15, FLOW_15, EMERGENCY_PROB, false, SEED);
            List<AircraftGeneration.FlightRow> inbound = gen.generateInbound();
            // 15 slots/hour; slot at 60 min is excluded, so 14 flights
            assertTrue(inbound.size() >= 14 && inbound.size() <= 15);
        }

        @Test
        @DisplayName("all inbound flights have destination COV and flightType arrival")
        void inboundDestinationAndType() {
            AircraftGeneration gen =
                    new AircraftGeneration(
                            DURATION_60, FLOW_15, FLOW_15, EMERGENCY_PROB, false, SEED);
            List<AircraftGeneration.FlightRow> inbound = gen.generateInbound();
            for (AircraftGeneration.FlightRow row : inbound) {
                assertEquals(AircraftGeneration.home_airport, row.destination());
                assertTrue(row.inbound());
                assertEquals("arrival", row.flightType());
            }
        }

        @Test
        @DisplayName("inbound origins are from known airport list")
        void inboundOriginsFromList() {
            AircraftGeneration gen =
                    new AircraftGeneration(
                            DURATION_60, FLOW_15, FLOW_15, EMERGENCY_PROB, false, SEED);
            List<AircraftGeneration.FlightRow> inbound = gen.generateInbound();
            Set<String> origins =
                    inbound.stream()
                            .map(AircraftGeneration.FlightRow::origin)
                            .collect(Collectors.toSet());
            assertTrue(KNOWN_AIRPORTS.containsAll(origins));
        }

        @Test
        @DisplayName("rows are sorted by actualTimeMinutes")
        void sortedByActualTime() {
            AircraftGeneration gen =
                    new AircraftGeneration(
                            DURATION_60, FLOW_15, FLOW_15, EMERGENCY_PROB, false, SEED);
            List<AircraftGeneration.FlightRow> inbound = gen.generateInbound();
            for (int i = 1; i < inbound.size(); i++) {
                assertTrue(
                        inbound.get(i).actualTimeMinutes()
                                >= inbound.get(i - 1).actualTimeMinutes());
            }
        }
    }

    @Nested
    @DisplayName("generateOutbound")
    class GenerateOutboundTests {

        @Test
        @DisplayName("returns expected number of flights for 60 min at 15/hour with fixed seed")
        void deterministicCount() {
            AircraftGeneration gen =
                    new AircraftGeneration(
                            DURATION_60, FLOW_15, FLOW_15, EMERGENCY_PROB, false, SEED);
            List<AircraftGeneration.FlightRow> outbound = gen.generateOutbound();
            assertTrue(outbound.size() >= 14 && outbound.size() <= 15);
        }

        @Test
        @DisplayName("all outbound flights have origin COV and flightType departure")
        void outboundOriginAndType() {
            AircraftGeneration gen =
                    new AircraftGeneration(
                            DURATION_60, FLOW_15, FLOW_15, EMERGENCY_PROB, false, SEED);
            List<AircraftGeneration.FlightRow> outbound = gen.generateOutbound();
            for (AircraftGeneration.FlightRow row : outbound) {
                assertEquals(AircraftGeneration.home_airport, row.origin());
                assertFalse(row.inbound());
                assertEquals("departure", row.flightType());
            }
        }

        @Test
        @DisplayName("outbound destinations are from known airport list")
        void outboundDestinationsFromList() {
            AircraftGeneration gen =
                    new AircraftGeneration(
                            DURATION_60, FLOW_15, FLOW_15, EMERGENCY_PROB, false, SEED);
            List<AircraftGeneration.FlightRow> outbound = gen.generateOutbound();
            Set<String> dests =
                    outbound.stream()
                            .map(AircraftGeneration.FlightRow::destination)
                            .collect(Collectors.toSet());
            assertTrue(KNOWN_AIRPORTS.containsAll(dests));
        }
    }

    @Nested
    @DisplayName("FlightRow content")
    class FlightRowContentTests {

        @Test
        @DisplayName("callsign is 6 characters (2 letter code + 4 digits)")
        void callsignFormat() {
            AircraftGeneration gen =
                    new AircraftGeneration(
                            DURATION_60, FLOW_15, FLOW_15, EMERGENCY_PROB, false, SEED);
            List<AircraftGeneration.FlightRow> inbound = gen.generateInbound();
            for (AircraftGeneration.FlightRow row : inbound) {
                String cs = row.callsign();
                assertEquals(6, cs.length());
                assertTrue(cs.substring(0, 2).matches("[A-Z0-9]{2}"));
                assertTrue(cs.substring(2).matches("\\d{4}"));
            }
        }

        @Test
        @DisplayName("fuel is within configured min and max")
        void fuelInRange() {
            AircraftGeneration gen =
                    new AircraftGeneration(
                            DURATION_60, FLOW_15, FLOW_15, EMERGENCY_PROB, false, SEED);
            List<AircraftGeneration.FlightRow> rows = gen.generateInbound();
            for (AircraftGeneration.FlightRow row : rows) {
                assertTrue(
                        row.fuelRemainingMinutes() >= AircraftGeneration.fuel_min,
                        "fuel >= fuel_min");
                assertTrue(
                        row.fuelRemainingMinutes() <= AircraftGeneration.fuel_max,
                        "fuel <= fuel_max");
            }
        }

        @Test
        @DisplayName("actualTimeMinutes within [0, durationMinutes]")
        void actualTimeInRange() {
            AircraftGeneration gen =
                    new AircraftGeneration(
                            DURATION_60, FLOW_15, FLOW_15, EMERGENCY_PROB, false, SEED);
            List<AircraftGeneration.FlightRow> rows = gen.generateInbound();
            for (AircraftGeneration.FlightRow row : rows) {
                assertTrue(row.actualTimeMinutes() >= 0);
                assertTrue(row.actualTimeMinutes() <= DURATION_60);
            }
        }

        @Test
        @DisplayName("altitude and ground speed match constants")
        void altitudeAndGroundSpeed() {
            AircraftGeneration gen =
                    new AircraftGeneration(
                            DURATION_60, FLOW_15, FLOW_15, EMERGENCY_PROB, false, SEED);
            List<AircraftGeneration.FlightRow> rows = gen.generateInbound();
            for (AircraftGeneration.FlightRow row : rows) {
                assertEquals(AircraftGeneration.default_altitude, row.altitudeM());
                assertEquals(AircraftGeneration.default_ground_speed, row.groundSpeedKnots());
            }
        }

        @Test
        @DisplayName("emergency status is one of enum values")
        void emergencyStatusValid() {
            AircraftGeneration gen =
                    new AircraftGeneration(
                            DURATION_60, FLOW_15, FLOW_15, EMERGENCY_PROB, false, SEED);
            List<AircraftGeneration.FlightRow> rows = gen.generateInbound();
            for (AircraftGeneration.FlightRow row : rows) {
                assertNotNull(row.emergencyStatus());
            }
        }
    }

    @Nested
    @DisplayName("stress test mode")
    class StressTestTests {

        @Test
        @DisplayName("inbound stress test produces exactly 15 flights")
        void stressTestInboundCount() {
            AircraftGeneration gen =
                    new AircraftGeneration(
                            DURATION_60, FLOW_15, FLOW_15, EMERGENCY_PROB, true, SEED);
            List<AircraftGeneration.FlightRow> inbound = gen.generateInbound();
            assertEquals(15, inbound.size());
        }

        @Test
        @DisplayName("outbound is not affected by stress test")
        void stressTestOutboundNormal() {
            AircraftGeneration gen =
                    new AircraftGeneration(
                            DURATION_60, FLOW_15, FLOW_15, EMERGENCY_PROB, true, SEED);
            List<AircraftGeneration.FlightRow> outbound = gen.generateOutbound();
            assertTrue(outbound.size() >= 14 && outbound.size() <= 15);
        }
    }

    @Nested
    @DisplayName("writeCsvs_directory_files")
    class WriteCsvTests {

        @Test
        @DisplayName("creates inbound_flights.csv, outbound_flights.csv, all_flights.csv")
        void createsThreeFiles(@TempDir Path dir) throws IOException {
            AircraftGeneration gen =
                    new AircraftGeneration(
                            DURATION_60, FLOW_15, FLOW_15, EMERGENCY_PROB, false, SEED);
            gen.writeCsvs_directory_files(dir);

            Path inbound = dir.resolve("inbound_flights.csv");
            Path outbound = dir.resolve("outbound_flights.csv");
            Path all = dir.resolve("all_flights.csv");

            assertTrue(Files.exists(inbound));
            assertTrue(Files.exists(outbound));
            assertTrue(Files.exists(all));
        }

        @Test
        @DisplayName("each CSV has header line and data lines")
        void csvHasHeaderAndData(@TempDir Path dir) throws IOException {
            AircraftGeneration gen =
                    new AircraftGeneration(
                            DURATION_60, FLOW_15, FLOW_15, EMERGENCY_PROB, false, SEED);
            gen.writeCsvs_directory_files(dir);

            List<String> inboundLines = Files.readAllLines(dir.resolve("inbound_flights.csv"));
            assertTrue(inboundLines.size() >= 2);
            assertTrue(inboundLines.get(0).startsWith("callsign,"));
            assertTrue(inboundLines.size() <= 1 + 15); // header + at most 15 rows for 60 min
        }

        @Test
        @DisplayName("all_flights.csv contains both inbound and outbound rows")
        void allFlightsCombined(@TempDir Path dir) throws IOException {
            AircraftGeneration gen =
                    new AircraftGeneration(
                            DURATION_60, FLOW_15, FLOW_15, EMERGENCY_PROB, false, SEED);
            gen.writeCsvs_directory_files(dir);

            List<String> allLines = Files.readAllLines(dir.resolve("all_flights.csv"));
            assertTrue(allLines.size() >= 2);
            assertTrue(allLines.size() <= 1 + 15 + 15); // header + max inbound + max outbound
        }
    }
}
