package com.example.helidon;

import io.helidon.config.Config;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import java.util.logging.Logger;

@RequestScoped
public class MqttClientImpl implements IMqttClient {

    private static final Logger logger = Logger.getLogger(IMqttClient.class.getName());
    private final MqttClient client;
    @Inject
    private Config config;

    @Inject
    public MqttClientImpl(@ConfigProperty(name = "mqtt.url") String mqttServerUrl,
                          @ConfigProperty(name = "mqtt.username") String username,
                          @ConfigProperty(name = "mqtt.password") String password,
                          @ConfigProperty(name = "mqtt.topic") String topic) throws MqttException {
        client = new org.eclipse.paho.client.mqttv3.MqttClient(mqttServerUrl, MqttClient.generateClientId(), null);
        client.setCallback(new MessageReceivedCallback());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        try {
            client.connect(options);
            client.subscribe(topic);
        } catch (MqttSecurityException securityException) {
            logger.severe("Failed to establish security connections");
        } catch (MqttException mqttException) {
            logger.severe("Failed to establish mqtt connection");
        }
    }

    @Override
    public void publishMessage(final String message) {
        publishMessage(message, config.get("mqtt.topic").asString().get());
    }

    @Override
    public void publishMessage(final String message, final String topic) {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        try {
            client.publish(topic, mqttMessage);
        } catch (MqttException e) {
            System.out.println("Failed to send the message");
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            client.disconnect();
            client.close(true);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

}
