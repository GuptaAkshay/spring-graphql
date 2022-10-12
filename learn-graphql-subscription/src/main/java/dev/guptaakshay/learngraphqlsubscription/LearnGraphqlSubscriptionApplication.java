package dev.guptaakshay.learngraphqlsubscription;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class LearnGraphqlSubscriptionApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearnGraphqlSubscriptionApplication.class, args);
	}

}

@Controller
class GreetingController {

	@SubscriptionMapping
	Flux<Greeting> greetings(){
		return Flux.fromStream(Stream.generate(() -> new Greeting("Hello, World @ "+ Instant.now() + "!")))
			.delayElements(Duration.ofSeconds(1))
			.take(10);
	}

	@QueryMapping
	Greeting greeting (){
		return new Greeting("Hello, World!");
	}
}

record Greeting(String greeting) {}
