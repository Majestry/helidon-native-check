package com.example.helidon;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageReceivedCallback implements MqttCallback
{

    private static final Logger LOGGER = Logger.getLogger(MessageReceivedCallback.class.getName());

    @Override
    public void connectionLost(Throwable cause)
    {
        LOGGER.log(Level.SEVERE, "Conenction was lost...");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message)
    {
        LOGGER.log(Level.INFO, () -> String.format("Topic: %s, message: %s%n", topic, message));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token)
    {
        LOGGER.log(Level.INFO, () -> String.format("Delivery is %s%n", token.isComplete()));
    }
}
