package dev.guptaakshay.learngraphqlbatch;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@SpringBootApplication
public class LearnGraphqlBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearnGraphqlBatchApplication.class, args);
	}

}

@Controller
class BatchController {
	@QueryMapping
	Collection<Customer> customers() {
		return List.of(new Customer(1, "A"), new Customer(2, "B"));
	}

	@BatchMapping
	Map<Customer, Account> account (List<Customer> customers) {
		System.out.println("calling account for customers");
		return customers
			.stream()
			.collect(Collectors.toMap(customer -> customer,
				customer ->  new Account(customer.id())));
	}
	// has n+1 problem network calls in real life
	// @SchemaMapping(typeName = "Customer")
	// Account account(Customer customer) {
	// 	System.out.println("getting account for customer id: "+ customer.id());
	// 	return new Account(customer.id());
	// }
}

record Customer (Integer id, String name) {}
record Account (Integer id) {}