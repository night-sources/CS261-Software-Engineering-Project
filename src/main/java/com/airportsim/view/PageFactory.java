package com.airportsim.view;

import com.airportsim.view.configuration.ConfigurationPage;
import com.airportsim.view.configuration.LoadResultsPage;
import com.airportsim.view.configuration.LoadScenarioPage;
import com.airportsim.view.controllers.ConfigurationController;
import com.airportsim.view.controllers.MainMenuController;

/** Interface for creating pages. */
public interface PageFactory {
    MainMenuPage createMainMenuPage(MainMenuController controller);

    ConfigurationPage createConfigurationPage(ConfigurationController controller);

    LoadScenarioPage createLoadScenarioPage(ConfigurationController controller);

    LoadResultsPage createLoadResultsPage(ConfigurationController controller);
}
