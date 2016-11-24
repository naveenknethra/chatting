package com.niit.collaboration.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
@ComponentScan(basePackages="com.niit")
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer{
	
	private static final Logger logger = LoggerFactory.getLogger(AppInitializer.class);

    /*@Override*/
	public void configMessageBroker(MessageBrokerRegistry config) {
		logger.debug("Starting of the method config Message Broker");
		config.enableSimpleBroker("/topic","/queue");
		config.setApplicationDestinationPrefixes("/app");
		logger.debug("Starting of the method ending");
	}
	
	

	public void registerStompEndpoints(StompEndpointRegistry registry) {
		
		logger.debug("Starting of the method register stompEnd points");
		registry.addEndpoint("/chat").withSockJS();
		logger.debug("Ending of the method register stompEnd points");
	/*	registry.addEndpoint("/chat_forum").withSockJS();*/
		
	}
	
}
