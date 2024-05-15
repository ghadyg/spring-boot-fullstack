package com.fullStackCourse.journey;

import com.fullStackCourse.auth.AuthenticationRequest;
import com.fullStackCourse.auth.AuthenticationResponse;
import com.fullStackCourse.customer.CustomerDTO;
import com.fullStackCourse.customer.CustomerRegistrationRequest;
import com.fullStackCourse.customer.Gender;
import com.fullStackCourse.jwt.JWTUtil;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthenticationIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JWTUtil jwtUtil;



    private  static  final Random RANDOM = new Random();
    private static final String Auth_path = "api/v1/auth";
    private static final String uri = "/api/v1/customers";

    @Test
    void CanLogin()
    {

        //create a registration req
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName()+"-"+ UUID.randomUUID()+"@testgmail.com";
        int age = RANDOM.nextInt(1,100);
        String password = "password";
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,email, password, age, Gender.male
        );

        webTestClient.post()
                .uri(Auth_path+"/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new AuthenticationRequest(email,password)), AuthenticationRequest.class)
                .exchange()
                .expectStatus().isUnauthorized();
        //send a post req
        webTestClient.post()
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

        EntityExchangeResult<AuthenticationResponse> result = webTestClient.post()
                .uri(Auth_path + "/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new AuthenticationRequest(email, password)), AuthenticationRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<AuthenticationResponse>() {
                })
                .returnResult();

        String tokens = result.getResponseHeaders().get(AUTHORIZATION).get(0);

        AuthenticationResponse authenticationResponse = result.getResponseBody();
        CustomerDTO dto = result.getResponseBody().customerDTO();

        assertThat(jwtUtil.isTokenValid(tokens,dto.username()));

        assertThat(dto.email()).isEqualTo(email);
        assertThat(dto.age()).isEqualTo(age);
        assertThat(dto.name()).isEqualTo(name);
        assertThat(dto.username()).isEqualTo(email);
        assertThat(dto.gender()).isEqualTo(Gender.male);
        assertThat(dto.roles()).isEqualTo(List.of("ROLE_USER"));

    }
}
