package com.analyzer.datasimulator.messenger.configuration;


import org.eclipse.paho.client.mqttv3.*;
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

    private final String DEVICE_RECEIVE_TOPIC = "/device/receive";

    @Value("${running.mode}")
    private String runningMode;

    @Autowired
    private Environment environment;


    @Bean
    public IMqttClient initMqttClient() {

        IMqttClient iMqttClient = initConnection();

        return iMqttClient;
    }

    private IMqttClient initConnection(){

        final String MQTT_IP = environment.getProperty("mqtt.ip");

        final String MQTT_PORT = environment.getProperty("mqtt.port");

        final String MQTT_SERVER_ADDRES= "tcp://"+MQTT_IP+":"+MQTT_PORT;

        final String MQTT_PUBLISHER_ID = UUID.randomUUID().toString();

        IMqttClient iMqttClient = null;

        try {
            iMqttClient = new MqttClient(MQTT_SERVER_ADDRES, MQTT_PUBLISHER_ID);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);

            if (!runningMode.equals("Testing"))
            {
                if(iMqttClient.connectWithResult(options).getClient().isConnected())
                {
                    System.out.println("MQTT is connected");

                }
            }

        } catch (MqttException e) {
            e.printStackTrace();
        }

        return iMqttClient;
    }


}
