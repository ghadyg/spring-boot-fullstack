package com.fullStackCourse.journey;


import com.fullStackCourse.customer.*;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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
                name,email, "password", age, Gender.male
        );

        //send a post req
        String uri = "/api/v1/customers";
        String token = webTestClient.post()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);
        //get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION,String.format("Bearer %s",token))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        //make sure that the customer is present



        var id =allCustomers.stream().filter(c->c.email().equals(email))
                .map(CustomerDTO::id).findFirst().orElseThrow();

        CustomerDTO expectedCustomer = new CustomerDTO(
                id,
                name
                ,email,Gender.male,age,List.of("ROLE_USER"),email
        );

        assertThat(allCustomers)
                //.usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);

        //getcustomer by id
        webTestClient.get()
                .uri(uri+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION,String.format("Bearer %s",token))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<CustomerDTO>() {})
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

        CustomerRegistrationRequest execCustomer = new CustomerRegistrationRequest(
                fakerName.fullName(),fakerName.lastName()+"-"+UUID.randomUUID()+"@testgmail.com", "password", age,Gender.male
        );

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,email, "password", age,Gender.male
        );

        //send a post req
        String uri = "/api/v1/customers";
       String token =  webTestClient.post()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(execCustomer),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Void.class)
            .getResponseHeaders()
            .get(AUTHORIZATION)
            .get(0);

        webTestClient.post()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();


        //get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION,String.format("Bearer %s",token))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        //make sure that the customer is present

        var id =allCustomers.stream().filter(c->c.email().equals(email))
                .map(CustomerDTO::id).findFirst().orElseThrow();

        //delete customer by id
        webTestClient.delete()
                .uri(uri+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION,String.format("Bearer %s",token))
                .exchange()
                .expectStatus().isOk();

        //getcustomer by id
        webTestClient.get()
                .uri(uri+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION,String.format("Bearer %s",token))
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
                name,email, "password", age,Gender.male
        );

        //send a post req
        String uri = "/api/v1/customers";
        String token = webTestClient.post()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);
        //get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION,String.format("Bearer %s",token))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        //make sure that the customer is present

        var id =allCustomers.stream().filter(c->c.email().equals(email))
                .map(CustomerDTO::id).findFirst().orElseThrow();

        //update customer
        String newName = "newNameToTest";
        CustomerUpdateRequest  customerUpdateRequest = new CustomerUpdateRequest(
                newName,null,null
        );

        webTestClient.put()
                .uri(uri+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION,String.format("Bearer %s",token))
                .body(Mono.just(customerUpdateRequest),CustomerUpdateRequest.class)
                .exchange()
                .expectStatus().isOk();

        //getcustomer by id
        CustomerDTO updatedCustomer = webTestClient.get()
                .uri(uri + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION,String.format("Bearer %s",token))
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerDTO.class)
                .returnResult()
                .getResponseBody();

        CustomerDTO customer = new CustomerDTO(
                id,newName,email,
                Gender.male,age,List.of("ROLE_USER"),email);

        assertThat(updatedCustomer).isEqualTo(customer);
    }


}
