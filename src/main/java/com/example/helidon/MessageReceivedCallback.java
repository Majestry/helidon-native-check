package com.example.helidon;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MessageReceivedCallback implements MqttCallback {

    @Override
    public void connectionLost(Throwable cause) {
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        System.out.printf("Topic: %s, message: %s%n", topic, message);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.printf("Delivery for %s completed%n", token.toString());
    }
}
