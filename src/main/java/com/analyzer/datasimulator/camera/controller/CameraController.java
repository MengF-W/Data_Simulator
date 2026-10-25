package com.analyzer.datasimulator.camera.controller;

import com.analyzer.datasimulator.camera.configuration.CameraConfigurator;
import com.analyzer.datasimulator.camera.utility.ObjectDetection;
import com.analyzer.datasimulator.messenger.processor.DeviceMessageProcessor;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
public class CameraController {

    Logger logger = LoggerFactory.getLogger(CameraController.class);

    @Autowired
    VideoCapture videoCapture;

    @Autowired
    DeviceMessageProcessor deviceMessageProcessor;

    @Autowired
    ObjectDetection objectDetection;

    @Autowired
    private Environment environment;

    public void start(){

        streamCamera();

    }

    private void streamCamera(){

        if (videoCapture.isOpened()){

            logger.info("Camera is connected");
            BackgroundSubtractorMOG2 mog2 = Video.createBackgroundSubtractorMOG2(500,16,true);
            Mat mat = new Mat();
            new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    if(videoCapture.read(mat)) {

                        try {
                            Mat foreGroundMask = objectDetection.processFrame(mat,mog2);
                            objectDetection.detectObject(foreGroundMask,mat);
                            mat.release();
                        } catch (MqttException e) {
                            logger.debug("Message cannot be sent. The exception is: " + e.getMessage());
                        }
                    }
                    else
                    {
                        logger.info("Camera is stopping to capture image");

                    }
                }
            }).start();

        }

    }

}
