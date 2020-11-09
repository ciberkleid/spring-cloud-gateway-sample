package com.example.gatewaydemo;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import java.time.Duration;

@SpringBootApplication
public class GatewayDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayDemoApplication.class, args);
	}

	// Resilience4J default circuit breaker configuration
	@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
				.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
				.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());
	}

	// Simple key resolver config for Redis Rate Limiter
	// From the Spring Cloud Gateway docs:
	/*
	The default implementation of KeyResolver is the PrincipalNameKeyResolver which retrieves
	the Principal from the ServerWebExchange and calls Principal.getName(). By default, if the
	KeyResolver does not find a key, requests will be denied. This behavior can be adjusted with
	the spring.cloud.gateway.filter.request-rate-limiter.deny-empty-key (true or false) and
	spring.cloud.gateway.filter.request-rate-limiter.empty-key-status-code properties.
	 */
	@Bean
	KeyResolver userKeyResolver() {
		return exchange -> Mono.just("1");
	}

	// Java route config example
	// Uppercase request body
	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("modify_request_body", r -> r.path("/post/**")
						.filters(f -> f.modifyRequestBody(
								String.class, Msg.class, MediaType.APPLICATION_JSON_VALUE,
								(exchange, s) -> Mono.just(new Msg(s.toUpperCase()))))
						.uri("https://httpbin.org"))
				.build();
	}

	static class Msg {
		String msg = "";

		public Msg() { }

		public Msg(String s) {
			this.msg = s;
		}

		public String getMsg() {
			return this.msg;
		}

		public void setMsg(String s) {
			this.msg = s;
		}
	}

}
