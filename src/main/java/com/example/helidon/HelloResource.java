package com.example.helidon;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.paho.client.mqttv3.MqttException;

@Path("/hello-world")
public class HelloResource {

//    @Inject
//    @Channel("emit-it")
//    private Emitter<String> emitter;

    @Inject
    private Service service;

    @GET
    @Produces("text/plain")
    public String hello(@QueryParam(value = "name") String name) throws MqttException {
        String response = String.format("Hello, %s!", name);
        service.publishMessage(response);
//        emitter.send(response);
        return response;
    }
}