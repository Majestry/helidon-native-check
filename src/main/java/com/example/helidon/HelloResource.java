package com.example.helidon;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.metrics.annotation.Timed;

import java.util.Map;

@Path("/hello-world")
public class HelloResource
{

    @Inject
    private IMqttClient mqttClient;

    @GET
    @Produces("application/json")
    @Timed(name = "helloTimings")
    public Map<String, Object> hello(@QueryParam(value = "name") String name)
    {
        String response = String.format("Hello, %s!", name);
        mqttClient.publishMessage(response);
        return Map.of("response", response);
    }
}