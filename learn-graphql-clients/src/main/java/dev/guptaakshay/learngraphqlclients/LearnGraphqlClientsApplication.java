package dev.guptaakshay.learngraphqlclients;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.graphql.client.RSocketGraphQlClient;

@SpringBootApplication
public class LearnGraphqlClientsApplication {

  public static void main(String[] args) {
    SpringApplication.run(LearnGraphqlClientsApplication.class, args);
  }

  @Bean
  HttpGraphQlClient httpGraphQlClient() {
    return HttpGraphQlClient.builder().url("http://localhost:8080/graphql").build();
  }

  @Bean
  RSocketGraphQlClient rSocketGraphQlClient(RSocketGraphQlClient.Builder<?> builder) {
    return builder.tcp("localhost", 9191).route("graphql").build();
  }

  @Bean
  ApplicationRunner applicationRunner(RSocketGraphQlClient rSocketGraphQlClient, HttpGraphQlClient httpGraphQlClient) {
    return new ApplicationRunner() {
      @Override
      public void run(ApplicationArguments args) throws Exception {
        var httpRequestDocument = """
          	query {
          		customerById (id:1) {
          			id 
          			name
          	  }
          	}
          """;

        httpGraphQlClient.document(httpRequestDocument)
          .retrieve("customerById")
          .toEntity(Customer.class)
          .subscribe(System.out::println);

        /*var rSocketRequestDocument = """
          	subscription {
          		greetings {
          			greeting
          	}
          """;
        rSocketGraphQlClient.document(rSocketRequestDocument)
          .retrieveSubscription("greetings")
          .toEntity(Greeting.class)
          .subscribe(System.out::println);*/
      }
    };
  }
}

record Greeting(String greeting) {
}

record Customer(Integer id, String name) {
}
