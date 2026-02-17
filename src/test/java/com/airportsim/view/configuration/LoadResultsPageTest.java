package com.airportsim.view.configuration;

import static org.junit.jupiter.api.Assertions.*;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class LoadResultsPageTest extends ApplicationTest {
    private Stage stage;

    @Start
    public void start(Stage stage) {
        this.stage = stage;
        LoadResultsPage loadResultsPage = new LoadResultsPage(stage);
        Scene scene = new Scene(loadResultsPage, 1280, 720);
        stage.setScene(scene);
        stage.setTitle("Load Previous Results Page");
        stage.show();
    }

    @Test
    public void testInitialWindowTitle() {
        assertEquals("Load Previous Results Page", stage.getTitle());
    }

    @Test
    public void testButtonsExist() {
        assertNotNull(lookup("Browse Files...").queryButton());
        assertNotNull(lookup("Back to Menu").queryButton());
        assertNotNull(lookup("View Results").queryButton());
    }

    @Test
    public void testBackButton() {
        Button backButton = lookup("Back to Menu").queryButton();
        clickOn(backButton);
        assertEquals("Airport Traffic Studio", stage.getTitle());
    }

    @Test
    public void testBrowseFiles() {
        Label fileLabel = lookup("No files selected").queryAs(Label.class);
        interact(() -> fileLabel.setText("test.csv"));
        assertEquals("test.csv", fileLabel.getText());
    }

    @Test
    public void testCSVSelected() {
        Label fileLabel = lookup("No files selected").queryAs(Label.class);
        interact(() -> fileLabel.setText("test.csv"));
        assertEquals("test.csv", fileLabel.getText());
    }
}
