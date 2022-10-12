package dev.guptaakshay.learngraphqldata;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.graphql.data.GraphQlRepository;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.querydsl.core.annotations.QueryEntity;

import dev.guptaakshay.learngraphqldata.QCustomer;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class LearnGraphqlDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearnGraphqlDataApplication.class, args);
	}

/*	@Bean
	RuntimeWiringConfigurer runtimeWiringConfigurer(CustomerRepository customerRepository) {
		return new RuntimeWiringConfigurer() {
			@Override
			public void configure(RuntimeWiring.Builder builder) {
				var data = QuerydslDataFetcher.builder(customerRepository).many();
				var dataum = QuerydslDataFetcher.builder(customerRepository).single();
				builder.type("Query", wiring -> wiring
					.dataFetcher("customer", dataum)
					.dataFetcher("customers", data));
			}
		};
	}*/

	@Bean
	ApplicationRunner applicationRunner(CustomerRepository customerRepository) {
		return args -> {

			Flux<Customer> all = customerRepository.findAll(QCustomer.customer.name.startsWith("A").not());

			customerRepository.deleteAll()
				.thenMany(Flux.just("Akshay", "Ridhhi", "Harsh", "Shubham", "Anup")
					.map(name -> new Customer(null, name))
					.flatMap(customerRepository::save))
				.thenMany(all)
				.subscribe(System.out::println);
		};
	}
}

@Controller
class ProfileController {

	@SchemaMapping(typeName = "Customer")
	Profile profile (Customer customer) {
		return new Profile(customer.id());
	}
}

@GraphQlRepository
interface CustomerRepository extends ReactiveCrudRepository<Customer, String>, ReactiveQuerydslPredicateExecutor<Customer> { }

@QueryEntity
@Document
record Customer(@Id String id, String name) {}

record Profile(String id) {}