package com.fullStackCourse.journey;


import com.fullStackCourse.customer.Customer;
import com.fullStackCourse.customer.CustomerRegistrationRequest;
import com.fullStackCourse.customer.CustomerUpdateRequest;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private  static  final Random RANDOM = new Random();

    @Test
    void CanRegisterACustomer()
    {

        //create a registration req
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName()+"-"+UUID.randomUUID()+"@testgmail.com";
        int age = RANDOM.nextInt(1,100);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,email,age
        );

        //send a post req
        String uri = "/api/v1/customers";
        webTestClient.post()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();
        //get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        //make sure that the customer is present
        Customer expectedCustomer = new Customer(
                name,email,age
        );
        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);

        var id =allCustomers.stream().filter(c->c.getEmail().equals(email))
                .map(Customer::getId).findFirst().orElseThrow();
        expectedCustomer.setId(id);
        //getcustomer by id
        webTestClient.get()
                .uri(uri+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .isEqualTo(expectedCustomer);

    }
    @Test
    void canDeleteCustomer()
    {
        //create a registration req
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName()+"-"+UUID.randomUUID()+"@testgmail.com";
        int age = RANDOM.nextInt(1,100);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,email,age
        );

        //send a post req
        String uri = "/api/v1/customers";
        webTestClient.post()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();
        //get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        //make sure that the customer is present

        var id =allCustomers.stream().filter(c->c.getEmail().equals(email))
                .map(Customer::getId).findFirst().orElseThrow();

        //delete customer by id
        webTestClient.delete()
                .uri(uri+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        //getcustomer by id
        webTestClient.get()
                .uri(uri+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void canUpdateCustomer()
    {
        //create a registration req
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName()+"-"+UUID.randomUUID()+"@testgmail.com";
        int age = RANDOM.nextInt(1,100);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,email,age
        );

        //send a post req
        String uri = "/api/v1/customers";
        webTestClient.post()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();
        //get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        //make sure that the customer is present

        var id =allCustomers.stream().filter(c->c.getEmail().equals(email))
                .map(Customer::getId).findFirst().orElseThrow();

        //update customer
        String newName = "newNameToTest";
        CustomerUpdateRequest  customerUpdateRequest = new CustomerUpdateRequest(
                newName,null,null
        );

        webTestClient.put()
                .uri(uri+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerUpdateRequest),CustomerUpdateRequest.class)
                .exchange()
                .expectStatus().isOk();

        //getcustomer by id
        Customer updatedCustomer = webTestClient.get()
                .uri(uri + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();

        Customer customer = new Customer(
                id,newName,email,age
        );

        assertThat(updatedCustomer).isEqualTo(customer);
    }


}
