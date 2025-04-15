package com.analyzer.datasimulator.messenger.processor;

import com.analyzer.datasimulator.camera.configuration.CameraConfigurator;
import com.analyzer.datasimulator.camera.model.Camera;
import com.analyzer.datasimulator.messenger.utilities.JsonParser;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class DeviceMessageProcessor implements MqttMessageProcessor{

    private final String DATABASE_URL = "http://localhost:8080/db/createContent";

    private final String DEVICE_RECEIVE_TOPIC = "/device/receive";

    @Autowired
    IMqttClient iMqttClient;

    @Autowired
    CameraConfigurator cameraConfigurator;

    @Override
    @Async
    public void publishMessage() throws MqttException {

        Camera camera = new Camera(cameraConfigurator.cameraSource,"camera","Camera Message Content", LocalDateTime.now());
        MqttMessage message = new MqttMessage();
        message.setPayload(JsonParser.getInstance().serializeJson(camera).getBytes());
        iMqttClient.publish(DEVICE_RECEIVE_TOPIC, message);

        System.out.println("Message "+ message +" is sent to '/device/receive'");

    }


    @Async
    public void publishObjectDetectedMessage() throws MqttException {

        Camera camera = new Camera(cameraConfigurator.cameraSource,"camera","object detected",LocalDateTime.now());
        MqttMessage message = new MqttMessage();
        message.setQos(0);
        message.setPayload(JsonParser.getInstance().serializeJson(camera).getBytes());
        iMqttClient.publish(DEVICE_RECEIVE_TOPIC, message);

        System.out.println("Object is detected !");
        System.out.println("Message "+ message +" is sent to '/device/receive'");

    }
}
