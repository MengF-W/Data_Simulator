package com.analyzer.datasimulator;

import com.analyzer.datasimulator.camera.controller.CameraController;
import com.analyzer.datasimulator.camera.controller.CameraFxController;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.Properties;

@SpringBootApplication
public class ApplicationMain{

    public static void main(String[] args) {

        try {
            Properties prop = new Properties();
            prop.load(ApplicationMain.class.getClassLoader().getResourceAsStream("application.properties"));

            if(Boolean.parseBoolean(prop.getProperty("javafx.window.display"))){
                FxApplication.launch(FxApplication.class,args);
            }
            else {
                int mb = 1024 * 1024;
                MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
                long xmx = memoryBean.getHeapMemoryUsage().getMax() / mb;
                long xms = memoryBean.getHeapMemoryUsage().getInit() / mb;
                System.out.println("Initial Memory (xms) : "+xms+ "mb");
                System.out.println("Max Memory (xmx) : "+xmx+"mb");
                ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(ApplicationMain.class);
                CameraController cameraController =(CameraController)configurableApplicationContext.getBean("cameraController");
                cameraController.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}