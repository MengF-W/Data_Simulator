package com.analyzer.datasimulator;

import com.analyzer.datasimulator.camera.controller.CameraController;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class ApplicationMain extends Application {

    private ConfigurableApplicationContext configurableApplicationContext;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        configurableApplicationContext = SpringApplication.run(ApplicationMain.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        CameraController cameraController =(CameraController)configurableApplicationContext.getBean("cameraController");
        cameraController.start(primaryStage);

    }
}