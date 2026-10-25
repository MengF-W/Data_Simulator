package com.analyzer.datasimulator.messenger.processor;

import com.analyzer.datasimulator.camera.configuration.CameraConfigurator;
import com.analyzer.datasimulator.camera.model.Camera;
import com.analyzer.datasimulator.messenger.configuration.MqttConfig;
import com.analyzer.datasimulator.messenger.utilities.JsonParser;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.Deflater;

@Component
public class DeviceMessageProcessor implements MqttMessageProcessor{

    Logger logger = LoggerFactory.getLogger(DeviceMessageProcessor.class);

    private final String DEVICE_RECEIVE_TOPIC = "/device/receive";

    private ReentrantLock reentrantLock;

    @Autowired
    IMqttClient iMqttClient;

    @Autowired
    CameraConfigurator cameraConfigurator;

    @PostConstruct
    public void postConstruct() {
        reentrantLock = new ReentrantLock();
    }

    @Override
    @Async
    public void publishMessage() throws MqttException {

        Camera camera = new Camera(cameraConfigurator.cameraSource,"camera","Camera Message Content", null , LocalDateTime.now());
        MqttMessage message = new MqttMessage();
        message.setPayload(JsonParser.getInstance().serializeJson(camera).getBytes());
        iMqttClient.publish(DEVICE_RECEIVE_TOPIC, message);

        logger.info("Message "+ message +" is sent to '/device/receive'");

    }


    @Async
    public void publishObjectDetectedMessage(byte[] cameraImage) throws MqttException {

        if(!reentrantLock.isLocked()){
            reentrantLock.lock();
            try {

                Camera camera = new Camera(cameraConfigurator.cameraSource,"camera","object detected", cameraImage, LocalDateTime.now());
                MqttMessage message = new MqttMessage();
                message.setQos(0);
                message.setPayload(compressPayload(JsonParser.getInstance().serializeJson(camera).getBytes()));
                iMqttClient.publish(DEVICE_RECEIVE_TOPIC, message);

                logger.info("Message is sent to '/device/receive'");
                Thread.sleep(180000 );

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (MqttException e){
                logger.debug("Message failed to be sent: ", e.getMessage());
            }finally {
                reentrantLock.unlock();
            }
        }
    }

    private byte[] compressPayload(byte[] payload){

        Deflater deflater = new Deflater();
        deflater.setInput(payload);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        while (!deflater.finished()) {
            int compressedSize = deflater.deflate(buffer);
            outputStream.write(buffer, 0, compressedSize);
        }

        return outputStream.toByteArray();
    }
}
