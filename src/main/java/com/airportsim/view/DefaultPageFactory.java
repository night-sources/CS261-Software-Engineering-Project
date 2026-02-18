package com.airportsim.view;

import com.airportsim.view.configuration.ConfigurationPage;
import com.airportsim.view.configuration.LoadResultsPage;
import com.airportsim.view.configuration.LoadScenarioPage;
import com.airportsim.view.controllers.ConfigurationController;
import com.airportsim.view.controllers.MainMenuController;

/** Default implementation of the PageFactory interface. */
public class DefaultPageFactory implements PageFactory {
    @Override
    public MainMenuPage createMainMenuPage(MainMenuController controller) {
        return new MainMenuPage(controller);
    }

    @Override
    public ConfigurationPage createConfigurationPage(ConfigurationController controller) {
        return new ConfigurationPage(controller);
    }

    @Override
    public LoadScenarioPage createLoadScenarioPage(ConfigurationController controller) {
        return new LoadScenarioPage(controller);
    }

    @Override
    public LoadResultsPage createLoadResultsPage(ConfigurationController controller) {
        return new LoadResultsPage(controller);
    }
}
