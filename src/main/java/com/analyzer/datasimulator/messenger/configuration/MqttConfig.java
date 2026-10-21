package com.analyzer.datasimulator.messenger.configuration;


import com.analyzer.datasimulator.messenger.processor.DeviceMessageProcessor;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
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
                    System.out.println("MQTT is connected");

                }
                else
                {
                    initConnection();
                }
            }

            iMqttClient.setCallback(new MqttCallback() {

                public void messageArrived(String topic, MqttMessage message) throws Exception {

                    System.out.println("Message Arrived" );
                    System.out.println("Topic: " + topic );
                    System.out.println("Message: " + new String(message.getPayload()));
                    System.out.println("QoS: " + message.getQos());

                }

                public void connectionLost(Throwable cause) {

                    System.out.println("MQTT connection is disconnected. " + cause.getMessage());
                    System.out.println("MQTT is reconnecting. " + cause.getMessage());
                    initConnection();
                    try {

                        while(!iMqttClient.connectWithResult(options).getClient().isConnected())
                        {
                            initConnection();
                        }

                    }catch (MqttException e) {

                        connectionLost(e);
                    }
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                }

            });

        } catch (MqttException e) {
            e.printStackTrace();
            initMqttClient();
        }

        return iMqttClient;
    }


}
