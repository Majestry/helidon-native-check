package com.example.helidon;

public interface IMqttClient {
    void publishMessage(String payload);

    void publishMessage(String message, String topic);

    void destroy();
}
