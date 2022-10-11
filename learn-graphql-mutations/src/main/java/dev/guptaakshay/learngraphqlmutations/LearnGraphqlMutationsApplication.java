package dev.guptaakshay.learngraphqlmutations;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@SpringBootApplication
public class LearnGraphqlMutationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearnGraphqlMutationsApplication.class, args);
	}

}

@Controller
class MutationController {

	private final Map<Integer, Customer> db = new ConcurrentHashMap<>();
	private final AtomicInteger id = new AtomicInteger();

	@MutationMapping
	Customer addCustomer(@Argument String name) {
		var id = this.id.incrementAndGet();
		var value = new Customer(id, name);
		this.db.put(id, value);
		return value;
	}

	@QueryMapping
	Customer customerById(@Argument Integer id) {
		return this.db.get(id);
	}
}

record Customer(Integer id, String name) {}
