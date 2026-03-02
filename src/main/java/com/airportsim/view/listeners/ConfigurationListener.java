package com.airportsim.view.listeners;

import com.airportsim.view.configuration.LoadResultsPage;
import com.airportsim.view.configuration.LoadScenarioPage;
import java.io.File;
import java.util.List;

public interface ConfigurationListener {
    void onBackToMainMenu();

    void onBrowseFile(LoadScenarioPage page);

    void onBrowseFiles(LoadResultsPage page);

    void onLoadScenario(File file);

    void onLoadResults(List<File> files);
}
