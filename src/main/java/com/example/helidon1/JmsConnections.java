package com.example.helidon1;

import com.rabbitmq.jms.admin.RMQConnectionFactory;
import com.rabbitmq.jms.client.message.RMQTextMessage;
import io.helidon.config.Config;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.ConnectionFactory;


@ApplicationScoped
public class JmsConnections {

    @Inject
    Config config;

    @Produces
    @Named("rabbitmq")
    public ConnectionFactory connectionFactory() {
        RMQConnectionFactory factory = new RMQConnectionFactory();
        factory.setHost(config.get("rmq.host").asString().get());
        factory.setPassword(config.get("rmq.password").asString().get());
        factory.setUsername(config.get("rmq.user").asString().get());
        factory.setVirtualHost("/");
        factory.setPort(5672);
        factory.setConfirmListener(context ->
        {
            context.getMessage();
            context.isAck();
        });
        return factory;
    }

    @Incoming("from-jms")
    public void consumeJms(String msg) {
        System.out.println("JMS from TESTQUEUE says: " + msg);
    }

    @Outgoing("to-jms")
    public PublisherBuilder<String> produceToJms() {
        return ReactiveStreams.of("test1", "test2", "test3");
    }

    @Incoming("rabbit")
    public void rabbit(String msg) {
        System.out.println("JMS from SomeQueue says: " + msg);
    }
}
