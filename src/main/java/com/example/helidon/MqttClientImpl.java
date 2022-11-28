package com.example.helidon;

import io.helidon.config.Config;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.paho.client.mqttv3.*;

import java.util.logging.Logger;

@ApplicationScoped
public class MqttClientImpl implements IMqttClient
{

    private static final Logger logger = Logger.getLogger(MqttClientImpl.class.getName());
    private final MqttClient client;
    @Inject
    private Config config;

    @Inject
    public MqttClientImpl(@ConfigProperty(name = "mqtt.url") String mqttServerUrl,
                          @ConfigProperty(name = "mqtt.username") String username,
                          @ConfigProperty(name = "mqtt.password") String password,
                          @ConfigProperty(name = "mqtt.topic") String topic) throws MqttException
    {
        client = new org.eclipse.paho.client.mqttv3.MqttClient(mqttServerUrl, MqttClient.generateClientId(), null);
        client.setCallback(new MessageReceivedCallback());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        try
        {
            client.connect(options);
            client.subscribe(topic);
        } catch (MqttSecurityException securityException)
        {
            logger.severe("Failed to establish secured connections");
        } catch (MqttException mqttException)
        {
            logger.severe("Failed to establish mqtt connection");
        }
    }

    @Override
    public void publishMessage(final String message)
    {
        publishMessage(message, config.get("mqtt.topic").asString().get());
    }

    @Override
    public void publishMessage(final String message, final String topic)
    {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        try
        {
            client.publish(topic, mqttMessage);
        } catch (MqttException e)
        {
            logger.severe("Failed to send the message");
        }
    }

    @PreDestroy
    public void destroy()
    {
        try
        {
            logger.info("Bye-bye, mqtt!");
            client.disconnect();
            client.close(true);
        } catch (MqttException e)
        {
            logger.severe(String.format("Failed to destroy the bean! Original exception: %s", e.getMessage()));
        }
    }

}
