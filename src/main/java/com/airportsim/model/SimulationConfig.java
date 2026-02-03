package com.airportsim.model;

import java.io.File;
import java.util.List;

public class SimulationConfig {
    private File runwaysFile;
    private int inboundFlowRate;
    private int outboundFlowRate;
    private int duration;
    private int maxWaitTime;

    public SimulationConfig() {}

    public void loadSettingsConfig(File configFile) {}

    public List<Runway> parseRunways(File runwaysFile) {}

    public int getMaxWaitTime() {}
}
