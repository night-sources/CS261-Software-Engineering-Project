package com.airportsim.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class AircraftGeneration {
    public static final String OUR_AIRPORT = "COV";
    private static final String[] AIRPORTS = {
        "LHR", "JFK", "DXB", "SIN", "USA", "FRA", "CDG", "AMS", "MAD", "BCN", "DUB", "EDI"
    };

    private static final Operator[] OPERATORS = {
        new Operator("Cathay Pacific", "CX"),
        new Operator("Lufthansa", "LH"),
        new Operator("British Airways", "BA"),
        new Operator("Emirates", "EK"),
        new Operator("Air France", "AF"),
        new Operator("Turkish Airlines", "TK"),
        new Operator("Singapore Airlines", "SQ"),
        new Operator("Ryanair", "FR"),
        new Operator("EasyJet", "U2"),
        new Operator("Jet2", "LS"),
    };

    public static final float default_altitude = Aircraft.default_altitude;
    public static final double default_ground_speed = Aircraft.default_ground_speed;
    public static final int flow_per_hour = 15;
    public static final double sigma_minutes = 5.0;
    public static final int fuel_min = 20;
    public static final int fuel_max = 60;
    public static final double emergency_probability = 0.15;

    private static final String csv_header =
            "callsign,operator,origin,destination,scheduled_time_minutes,actual_time_minutes,"
                    + "fuel_remaining_minutes,emergency_status,altitude,ground_speed,inbound,flight_type";

    private static final String flight_type_arrival = "arrival";
    private static final String flight_type_departure = "departure";

    private final Random random;
    private final int durationMinutes;
    private final int inboundPerHour;
    private final int outboundPerHour;
    private final double emergencyProbability;
    private final boolean stressTest;

    public AircraftGeneration(
            int durationMinutes,
            int inboundPerHour,
            int outboundPerHour,
            double emergency_probability,
            boolean stressTest,
            long seed) {
        this.durationMinutes = durationMinutes;
        this.inboundPerHour = inboundPerHour;
        this.outboundPerHour = outboundPerHour;
        this.emergencyProbability = emergency_probability;
        this.stressTest = stressTest;
        this.random = seed != 0 ? new Random(seed) : new Random();
    }

    public List<FlightRow> generateInbound() {
        return generateFlights(true, inboundPerHour);
    }

    public List<FlightRow> generateOutbound() {
        return generateFlights(false, outboundPerHour);
    }

    private List<FlightRow> generateFlights(boolean inbound, int flowPerHour) {
        List<FlightRow> rows = new ArrayList<>();
        int numHours = Math.max(1, durationMinutes / 60);
        int flightsPerHour;

        if (stressTest && inbound) {
            flightsPerHour = 15;
        } else {
            flightsPerHour = flowPerHour;
        }

        int slotIntervalMinutes = 60 / Math.max(1, flightsPerHour);

        if (stressTest && inbound) {
            // 15 aircraft in a single 5-minute window (e.g. at minute 0–5)
            int windowStart = random.nextInt(Math.max(1, durationMinutes - 5));
            for (int i = 0; i < 15; i++) {
                double target = windowStart + random.nextDouble() * 5;
                rows.add(createFlightRow(inbound, target));
            }
        } else {
            // regular flow-rate scheduling
            int totalFlights = numHours * flightsPerHour;
            for (int i = 0; i < totalFlights; i++) {
                int h = i / flightsPerHour;
                int slot = i % flightsPerHour;
                int targetMinutes = h * 60 + (slot + 1) * slotIntervalMinutes;
                if (targetMinutes >= durationMinutes) break;
                rows.add(createFlightRow(inbound, targetMinutes));
            }
        }
        rows.sort(Comparator.comparingDouble(FlightRow::actualTimeMinutes)); // sort by actual time
        return rows;
    }

    private FlightRow createFlightRow(boolean inbound, double targetScheduledMinutes) {
        double actualMinutes = targetScheduledMinutes + nextGaussian() * sigma_minutes;
        actualMinutes = Math.max(0, Math.min(durationMinutes, actualMinutes));

        int fuelMinutes = fuel_min + random.nextInt(fuel_max - fuel_min + 1);
        EmergencyStatus emergency = nextEmergency();

        Operator operator = pickRandomOperator();
        String callsign = buildCallsign(operator.code());
        String origin = inbound ? pickRandomAirport() : OUR_AIRPORT;
        String destination = inbound ? OUR_AIRPORT : pickRandomAirport();
        String flightType = inbound ? flight_type_arrival : flight_type_departure;

        return new FlightRow(
                callsign,
                operator.name(),
                origin,
                destination,
                targetScheduledMinutes,
                actualMinutes,
                fuelMinutes,
                emergency,
                default_altitude,
                default_ground_speed,
                inbound,
                flightType);
    }

    private Operator pickRandomOperator() {
        return OPERATORS[random.nextInt(OPERATORS.length)];
    }

    private String pickRandomAirport() {
        return AIRPORTS[random.nextInt(AIRPORTS.length)];
    }

    private String buildCallsign(String operatorCode) {
        int number = random.nextInt(10_000);
        return String.format("%s%04d", operatorCode, number);
    }

    private double nextGaussian() {
        return random.nextGaussian();
    }

    private EmergencyStatus nextEmergency() {
        if (random.nextDouble() >= emergencyProbability) return EmergencyStatus.NONE;
        EmergencyStatus[] emergencies =
                Arrays.stream(EmergencyStatus.values())
                        .filter(s -> s != EmergencyStatus.NONE)
                        .toArray(EmergencyStatus[]::new);
        if (emergencies.length == 0) return EmergencyStatus.NONE;
        return emergencies[random.nextInt(emergencies.length)];
    }

    public void writeCsvs_directory_files(Path outputDir) throws IOException {
        Files.createDirectories(outputDir);

        List<FlightRow> inbound = generateInbound();
        List<FlightRow> outbound = generateOutbound();

        writeCsv_line(outputDir.resolve("inbound_flights.csv"), inbound);
        writeCsv_line(outputDir.resolve("outbound_flights.csv"), outbound);

        List<FlightRow> all = new ArrayList<>(inbound);
        all.addAll(outbound);
        all.sort(Comparator.comparingDouble(FlightRow::actualTimeMinutes));
        writeCsv_line(outputDir.resolve("all_flights.csv"), all);
    }

    private void writeCsv_line(Path path, List<FlightRow> rows) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add(csv_header);
        for (FlightRow row : rows) {
            lines.add(row.toCsvLine());
        }
        Files.write(path, lines);
    }

    private record Operator(String name, String code) {}

    public record FlightRow(
            String callsign,
            String operator,
            String origin,
            String destination,
            double scheduledTimeMinutes,
            double actualTimeMinutes,
            int fuelRemainingMinutes,
            EmergencyStatus emergencyStatus,
            float altitudeM,
            double groundSpeedKnots,
            boolean inbound,
            String flightType) {

        public String toCsvLine() {
            return String.join(
                    ",",
                    escape(callsign),
                    escape(operator),
                    escape(origin),
                    escape(destination),
                    formatDouble(scheduledTimeMinutes),
                    formatDouble(actualTimeMinutes),
                    String.valueOf(fuelRemainingMinutes),
                    emergencyStatus.name(),
                    String.valueOf((int) altitudeM),
                    formatDouble(groundSpeedKnots),
                    Boolean.toString(inbound),
                    escape(flightType));
        }

        private static String escape(String s) {
            if (s == null) return "\"\"";
            if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
                return "\"" + s.replace("\"", "\"\"") + "\"";
            }
            return s;
        }

        private static String formatDouble(double v) {
            return String.format("%.2f", v);
        }
    }

    public static void main(String[] args) throws IOException {
        int durationMinutes = args.length > 0 ? Integer.parseInt(args[0]) : 60;
        long seed = args.length > 1 ? Long.parseLong(args[1]) : 0;
        boolean stressTest = args.length > 2 && Boolean.parseBoolean(args[2]);

        Path out = Path.of("simulation");
        AircraftGeneration gen =
                new AircraftGeneration(
                        durationMinutes,
                        flow_per_hour,
                        flow_per_hour,
                        emergency_probability,
                        stressTest,
                        seed);
        gen.writeCsvs_directory_files(out);
        System.out.println(
                "Wrote "
                        + out.toAbsolutePath()
                        + "/inbound_flights.csv, outbound_flights.csv, all_flights.csv");
    }
}
