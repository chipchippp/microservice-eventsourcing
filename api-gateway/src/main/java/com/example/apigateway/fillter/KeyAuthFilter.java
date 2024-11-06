package com.example.apigateway.fillter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class KeyAuthFilter extends AbstractGatewayFilterFactory<KeyAuthFilter.Config> {

    @Value("${apiKey}")
    private String apiKey;


    @Override
    public GatewayFilter apply(Config config) {
        // Implement your filter here
        return (exchange, chain) -> {
            if (!exchange.getRequest().getHeaders().containsKey("apiKey")){
                return handleException(exchange,"Missing authorization information",HttpStatus.UNAUTHORIZED);
            }
            String key = exchange.getRequest().getHeaders().get("apiKey").get(0);
            if (!key.equals(apiKey)){
                return handleException(exchange,"Invalid Api Key",HttpStatus.FORBIDDEN);
            }
            ServerHttpRequest request = exchange.getRequest();
            return chain.filter(exchange.mutate().request(request).build());
        };
    }

    private Mono<Void> handleException(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String errorMessage = String.format(
                "{\"timestamp\": \"%s\", \"status\": %d, \"error\": \"%s\", \"message\": \"%s\", \"path\": \"%s\"}",
                java.time.ZonedDateTime.now().toString(),
                status.value(),
                status.getReasonPhrase(),
                message,
                exchange.getRequest().getPath()
        );
        // Implement your error handling here
        return response.writeWith(Mono.just(response.bufferFactory().wrap(errorMessage.getBytes())));
    }

    public KeyAuthFilter() {
        super(Config.class);
    }

    public static class Config {
        // Put configuration properties here
    }
}