package com.example.helidon;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

@ApplicationScoped
public class Service {

    private MqttClient client;

    public Service() throws MqttException {
        client = new MqttClient("tcp://localhost:1883", "Helid");
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("guest");
        options.setPassword("guest".toCharArray());
        client.connect(options);
        client.subscribe("/helidon", (topic, msg) -> System.out.printf("Received message %s from topic %s%n", topic, new String(msg.getPayload())));
    }

    public void publishMessage(final String payload) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage(payload.getBytes());
        client.publish("/helidon", mqttMessage);
    }


}
