package com.analyzer.datasimulator.camera.configuration;

import nu.pattern.OpenCV;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CameraConfigurator {

    private VideoCapture videoCapture;

    @Value("")
    public String cameraSource;

    @Bean
    public VideoCapture initVideoCapture()
    {
        OpenCV.loadLocally();
        if(cameraSource == null || cameraSource.equals(""))
        {
            videoCapture = new VideoCapture(0);
            System.out.println("Web Camera is used");
            cameraSource = "Web Camera";

        }
        else
        {
            videoCapture = new VideoCapture(cameraSource);
            System.out.println("Camera " + cameraSource + " is used");
        }

        return videoCapture;
    }

}
