package com.fullStackCourse.customer;

import com.fullStackCourse.AbstractTestcontainer;
import com.fullStackCourse.TestConfig;
import com.fullStackCourse.s3.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestConfig.class})
class CustomerRepositoryTest extends AbstractTestcontainer {

    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;


    @BeforeEach
    void setUp() {


    }

    @Test
    void existsCustomerByEmail() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                "password", 20,
                Gender.male
        );


        underTest.save(customer);

        boolean result = underTest.existsCustomerByEmail(email);

        assertThat(result).isTrue();
    }

    @Test
    void notExistsCustomerByEmail() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        boolean result = underTest.existsCustomerByEmail(email);

        assertThat(result).isFalse();
    }

    @Test
    void existsCustomerById() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                "password", 20,Gender.male
        );


        underTest.save(customer);

        Integer id = underTest.findAll()
                .stream().filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        boolean result = underTest.existsCustomerById(id);

        assertThat(result).isTrue();
    }

    @Test
    void notExistsCustomerById() {
        int id =-1;

        boolean result = underTest.existsCustomerById(id);

        assertThat(result).isFalse();
    }

    @Test
    void canUpdateProfile()
    {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                "password", 20,Gender.male
        );


        underTest.save(customer);

        Integer id = underTest.findAll()
                .stream().filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        underTest.setProfileImageId("2222",id);

        Optional<Customer> result = underTest.findById(id);
        assertThat(result).isPresent().hasValueSatisfying(
                c->{
                assertThat(c.getProfileImageId()).isEqualTo("2222");
                }
        );

    }
}