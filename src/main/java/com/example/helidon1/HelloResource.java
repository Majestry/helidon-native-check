package com.example.helidon1;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Path("/hello-world")
public class HelloResource {

    @Inject
    @Channel("emit-it")
    private Emitter<String> emitter;

    @GET
    @Produces("text/plain")
    public String hello(@QueryParam(value = "name") String name) {
        String response = String.format("Hello, %s!", name);
        emitter.send(response);
        return response;
    }
}