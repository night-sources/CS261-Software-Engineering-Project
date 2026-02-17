package com.airportsim;

import static org.junit.jupiter.api.Assertions.*;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class AirportSimAppTest extends ApplicationTest {
    private Stage stage;

    @Start
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        new AirportSimApp().start(stage);
    }

    @Test
    public void testInitialWindowTitle() {
        assertEquals("Airport Traffic Studio", stage.getTitle());
    }
}
