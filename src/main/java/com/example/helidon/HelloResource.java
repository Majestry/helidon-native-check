package com.example.helidon;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

@Path("/hello-world")
public class HelloResource {

    @Inject
    private IMqttClient mqttClient;

    @GET
    @Produces("text/plain")
    public String hello(@QueryParam(value = "name") String name) {
        String response = String.format("Hello, %s!", name);
        mqttClient.publishMessage(response);
        return response;
    }
}