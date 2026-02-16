package com.airportsim.model;

import java.io.File;
import java.util.List;

public class SimulationConfig {
    private File runwaysFile;
    private int inboundFlowRate;
    private int outboundFlowRate;
    private int duration;
    private int maxWaitTime;

    public SimulationConfig() {
        // Default values
        this.inboundFlowRate = 15;
        this.outboundFlowRate = 15;
        this.duration = 3600; // 1 hour
        this.maxWaitTime = 1800; // 30 minutes
    }

    public void loadSettingsConfig(File configFile) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<Runway> parseRunways(File runwaysFile) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public File getRunwaysFile() {
        return runwaysFile;
    }

    public void setRunwaysFile(File runwaysFile) {
        this.runwaysFile = runwaysFile;
    }

    public int getInboundFlowRate() {
        return inboundFlowRate;
    }

    public void setInboundFlowRate(int inboundFlowRate) {
        this.inboundFlowRate = inboundFlowRate;
    }

    public int getOutboundFlowRate() {
        return outboundFlowRate;
    }

    public void setOutboundFlowRate(int outboundFlowRate) {
        this.outboundFlowRate = outboundFlowRate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getMaxWaitTime() {
        return maxWaitTime;
    }

    public void setMaxWaitTime(int maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }
}
