package com.airportsim.view.controllers;

import com.airportsim.view.listeners.MainMenuListener;

/** Controller for handling interactions with the main menu page. */
public class MainMenuController implements MainMenuListener {
    private final MainController mainController;
    private final NavigationController navigationController;

    public MainMenuController(
            MainController mainController, NavigationController navigationController) {
        this.mainController = mainController;
        this.navigationController = navigationController;
    }

    @Override
    public void onStartNewScenario() {
        navigationController.showConfigurationPage(mainController.getConfigurationController());
    }

    @Override
    public void onLoadPreviousScenario() {
        navigationController.showLoadScenarioPage(mainController.getConfigurationController());
    }

    @Override
    public void onLoadPreviousResults() {
        navigationController.showLoadResultsPage(mainController.getConfigurationController());
    }

    @Override
    public void onQuit() {
        mainController.onQuitClicked();
    }
}
