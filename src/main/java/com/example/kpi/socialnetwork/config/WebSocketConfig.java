package com.example.kpi.socialnetwork.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

/**
 * A configuration class for implementing server-side websockets
 * */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configuring a message broker for instant messaging of posts, likes, comments
     * */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Configuring endpoints of a message broker
     * */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/tweets")
                .withSockJS()
                .setClientLibraryUrl("/js/sockjs.js")
                .setWebSocketEnabled(true);
        registry.addEndpoint("/tweets/user")
                .withSockJS()
                .setClientLibraryUrl("/js/sockjs.js")
                .setWebSocketEnabled(true);
        registry.addEndpoint("/comments")
                .withSockJS()
                .setClientLibraryUrl("/js/sockjs.js")
                .setWebSocketEnabled(true);
        registry.addEndpoint("/comments/like")
                .withSockJS()
                .setClientLibraryUrl("/js/sockjs.js")
                .setWebSocketEnabled(true);
        registry.addEndpoint("/tweets/like")
                .withSockJS()
                .setClientLibraryUrl("/js/sockjs.js")
                .setWebSocketEnabled(true);
        registry.addEndpoint("/tweets/delete")
                .withSockJS()
                .setClientLibraryUrl("/js/sockjs.js")
                .setWebSocketEnabled(true);
        registry.addEndpoint("/users/edit")
                .withSockJS()
                .setClientLibraryUrl("/js/sockjs.js")
                .setWebSocketEnabled(true);
        registry.addEndpoint("/tweets/edit")
                .withSockJS()
                .setClientLibraryUrl("/js/sockjs.js")
                .setWebSocketEnabled(true);
        registry.addEndpoint("/tweets/save")
                .withSockJS()
                .setClientLibraryUrl("/js/sockjs.js")
                .setWebSocketEnabled(true);
    }
}