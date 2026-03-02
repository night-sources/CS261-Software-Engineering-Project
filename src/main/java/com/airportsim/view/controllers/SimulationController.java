package com.airportsim.view.controllers;

import com.airportsim.view.listeners.SimulationListener;

/** Controller for handling interactions with the simulation page. (Incomplete) */
public class SimulationController implements SimulationListener {
    private final MainController mainController;
    private final NavigationController navigationController;

    public SimulationController(
            MainController mainController, NavigationController navigationController) {
        this.mainController = mainController;
        this.navigationController = navigationController;
    }
}
