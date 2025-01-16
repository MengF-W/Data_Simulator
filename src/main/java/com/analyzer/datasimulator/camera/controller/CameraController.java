package com.analyzer.datasimulator.camera.controller;

import com.analyzer.datasimulator.messenger.processor.DeviceMessageProcessor;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
public class CameraController extends Application {

    @Autowired
    VideoCapture videoCapture;
    private ImageView imageView;
    private Button sendMessageButton;
    private VBox vBox;
    private Scene scene;

    @Autowired
    DeviceMessageProcessor deviceMessageProcessor;

    @Override
    public void start(Stage primaryStage) throws Exception {

        initProperties();
        streamCamera();
        initPrimaryStage(primaryStage);

    }

    private void initProperties()
    {
        imageView = new ImageView();
        sendMessageButton = new Button("Send Message");
        initButtonAction(sendMessageButton);
        vBox = new VBox();

        vBox.getChildren().addAll(imageView, sendMessageButton);
        vBox.setMargin(imageView, new Insets(20, 20, 20, 20));
        vBox.setMargin(sendMessageButton, new Insets(20, 20, 20, 20));

        scene = new Scene(vBox, videoCapture.get(Videoio.CAP_PROP_FRAME_WIDTH)+30, videoCapture.get(Videoio.CAP_PROP_FRAME_HEIGHT)+85);
    }

    private void streamCamera(){

        if (videoCapture.isOpened()){

            System.out.println("Camera is connected");

            new AnimationTimer() {
                @Override public void handle(long l) {

                    if(videoCapture.read(new Mat())) {

                        Mat mat = new Mat();
                        videoCapture.read(mat);
                        imageView.setImage(convertImgDimensionArrayToViewableImg(mat));
                    }
                    else
                    {
                        System.out.println("Camera is stopping to capture image");
                    }
                }
            }.start();
        }

    }

    private Image convertImgDimensionArrayToViewableImg(Mat mat) {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", mat, matOfByte);
        InputStream inputStream = new ByteArrayInputStream(matOfByte.toArray());
        return new Image(inputStream);
    }

    private void initPrimaryStage(Stage primaryStage)
    {
        primaryStage.setTitle("Camera Player");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    System.exit(0);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void initButtonAction(Button sendMessageButton)
    {
        sendMessageButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                try {
                    deviceMessageProcessor.publishMessage();
                } catch (MqttException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }


}
