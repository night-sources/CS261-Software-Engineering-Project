package com.airportsim.view.controllers;

import com.airportsim.view.configuration.LoadResultsPage;
import com.airportsim.view.configuration.LoadScenarioPage;
import com.airportsim.view.listeners.ConfigurationListener;
import java.io.File;
import java.util.List;
import javafx.stage.FileChooser;

/**
 * Controller for configuration pages & Interactions (ConmfigurationPage, LoadScenarioPage,
 * LoadResultsPage).
 */
public class ConfigurationController implements ConfigurationListener {
    private final MainController mainController;
    private final NavigationController navigationController;

    public ConfigurationController(
            MainController mainController, NavigationController navigationController) {
        this.mainController = mainController;
        this.navigationController = navigationController;
    }

    @Override
    public void onBackToMainMenu() {
        navigationController.showMainMenu(mainController.getMainMenuController());
    }

    @Override
    public void onBrowseFile(LoadScenarioPage page) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Scenario File");
        fileChooser
                .getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(mainController.getStage());
        page.setSelectedFile(file);
    }

    @Override
    public void onBrowseFiles(LoadResultsPage page) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Result Files");
        fileChooser
                .getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        List<File> files = fileChooser.showOpenMultipleDialog(mainController.getStage());
        page.setSelectedFiles(files);
    }

    @Override
    public void onLoadScenario(File file) {
        // TODO: Parse the scenario file and start the simulation
    }

    @Override
    public void onLoadResults(List<File> files) {
        // TODO: Parse the results files and display them
    }
}
