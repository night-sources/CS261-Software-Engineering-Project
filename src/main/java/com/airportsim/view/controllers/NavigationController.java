package com.airportsim.view.controllers;

import com.airportsim.view.MainMenuPage;
import com.airportsim.view.PageFactory;
import com.airportsim.view.configuration.ConfigurationPage;
import com.airportsim.view.configuration.LoadResultsPage;
import com.airportsim.view.configuration.LoadScenarioPage;
import javafx.stage.Stage;

/** Controller responsible for all page creation and navigation logic. */
public class NavigationController {
    private final Stage stage;
    private final PageFactory pageFactory;

    public NavigationController(Stage stage, PageFactory pageFactory) {
        this.stage = stage;
        this.pageFactory = pageFactory;
    }

    public void showMainMenu(MainMenuController mainMenuController) {
        MainMenuPage page = pageFactory.createMainMenuPage(mainMenuController);
        stage.getScene().setRoot(page);
    }

    public void showConfigurationPage(ConfigurationController configController) {
        ConfigurationPage page = pageFactory.createConfigurationPage(configController);
        stage.getScene().setRoot(page);
    }

    public void showLoadScenarioPage(ConfigurationController configController) {
        LoadScenarioPage page = pageFactory.createLoadScenarioPage(configController);
        stage.getScene().setRoot(page);
    }

    public void showLoadResultsPage(ConfigurationController configController) {
        LoadResultsPage page = pageFactory.createLoadResultsPage(configController);
        stage.getScene().setRoot(page);
    }

    /**
     * Creates the main menu page without setting it as the current scene root. (used for
     * initialisation in AirportSimApp)
     */
    public MainMenuPage createMainMenuPage(MainMenuController mainMenuController) {
        return pageFactory.createMainMenuPage(mainMenuController);
    }

    // TODO: Add more navigation methods as needed (showSimulationPage, showResultsPage, etc.)
}
