package com.analyzer.datasimulator.messenger.processor;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface MqttMessageProcessor{
    void publishMessage() throws Exception;
}
