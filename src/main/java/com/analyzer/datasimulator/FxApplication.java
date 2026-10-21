package com.analyzer.datasimulator;

import com.analyzer.datasimulator.camera.controller.CameraFxController;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class FxApplication extends Application {

    private ConfigurableApplicationContext configurableApplicationContext;

    @Override
    public void init() throws Exception {
        configurableApplicationContext = SpringApplication.run(ApplicationMain.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        CameraFxController cameraController =(CameraFxController)configurableApplicationContext.getBean("cameraFxController");
        cameraController.start(primaryStage);

    }
}
