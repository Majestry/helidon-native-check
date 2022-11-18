package com.example.helidon;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import jakarta.jms.ConnectionFactory;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;


@ApplicationScoped
public class JmsConnections {

    @Produces
    @Named("activemq")
    public ConnectionFactory connectionFactoryActiveMq(@ConfigProperty(name = "rmq.username") String username,
                                                       @ConfigProperty(name = "rmq.password") String password,
                                                       @ConfigProperty(name = "rmq.host") String host) {
        return new JmsConnectionFactory(username, password, host);
    }

    @Incoming("from-jms")
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    public void rabbit(String msg) {
        System.out.println("JMS from SomeQueue says: " + msg);
    }
}
