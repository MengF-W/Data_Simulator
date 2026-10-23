package com.analyzer.datasimulator.messenger.configuration;


import com.analyzer.datasimulator.camera.configuration.CameraConfigurator;
import com.analyzer.datasimulator.messenger.processor.DeviceMessageProcessor;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@ComponentScan(basePackages = "com.analyzer.datasimulator")
public class MqttConfig {

    Logger logger = LoggerFactory.getLogger(MqttConfig.class);

    private final String DEVICE_RECEIVE_TOPIC = "/device/receive";

    @Value("${running.mode}")
    private String runningMode;

    @Autowired
    private Environment environment;

    public IMqttClient iMqttClient;

    @Bean
    public IMqttClient initMqttClient() {

        this.iMqttClient = initConnection();

        return iMqttClient;
    }

    private IMqttClient initConnection(){

        final String MQTT_SERVER_ADDRES= environment.getProperty("mqtt.server.address");

        final String MQTT_PUBLISHER_ID = UUID.randomUUID().toString();

        try {
            if(Boolean.parseBoolean(environment.getProperty("mqtt.filePersistence")))
            {
                iMqttClient = new MqttClient(MQTT_SERVER_ADDRES, MQTT_PUBLISHER_ID);
            }
            else
            {

                iMqttClient = new MqttClient(MQTT_SERVER_ADDRES, MQTT_PUBLISHER_ID,new MemoryPersistence());
            }

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);

            IMqttToken iMqttToken = iMqttClient.connectWithResult(options);

            if (!runningMode.equals("Testing"))
            {
                if(iMqttToken != null && iMqttToken.getClient() != null && iMqttToken.getClient().isConnected())
                {
                    logger.info("MQTT is connected");

                }
                else
                {
                    initConnection();
                }
            }

            iMqttClient.setCallback(new MqttCallback() {

                public void messageArrived(String topic, MqttMessage message) throws Exception {

                    logger.info("Message Arrived" );
                    logger.info("Topic: " + topic );
                    logger.info("Message: " + new String(message.getPayload()));
                    logger.info("QoS: " + message.getQos());

                }

                public void connectionLost(Throwable cause) {

                    logger.debug("MQTT connection is disconnected. " + cause.getMessage());
                    logger.debug("MQTT is reconnecting. ");
                    initConnection();

                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                }

            });

        } catch (MqttException e) {
            logger.debug("MQTT is not connected. " + e.getMessage());
            initMqttClient();
        }

        return iMqttClient;
    }


}
