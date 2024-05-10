package com.fullStackCourse.customer;

import com.fullStackCourse.AbstractTestcontainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainer {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {

        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {

        Customer customer = new Customer(
                faker.name().fullName(),
                faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID(),
                20,
                Gender.male
        );
        underTest.insertCustomer(customer);
        List<Customer> customers = underTest.selectAllCustomers();
        assertThat(customers).isNotEmpty();

    }

    @Test
    void selectCustomer() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20,
                Gender.male
        );
        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCustomers()
                .stream().filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Optional<Customer> result = underTest.selectCustomer(id);

        assertThat(result).isPresent().hasValueSatisfying(c->{
           assertThat(c.getId()).isEqualTo(id);
           assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getGender()).isEqualTo(Gender.male);

        });


    }

    @Test
    void willReturnEmptyWhenSelectCustomerById()
    {
        int id= -1;

        var customer = underTest.selectCustomer(id);

        assertThat(customer).isEmpty();
    }

    @Test
    void insertCustomer() {

        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20,
                Gender.male
        );

        underTest.insertCustomer(customer);
        Optional<Customer> result = underTest.selectAllCustomers()
                .stream().filter(c -> c.getEmail().equals(email))
                .findFirst();

        assertThat(result).isPresent().hasValueSatisfying(c->{
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getGender()).isEqualTo(Gender.male);

        });

    }

    @Test
    void existsPersonWithEmail() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20,
                Gender.male
        );
        underTest.insertCustomer(customer);

        boolean existsPersonWithEmail = underTest.existsPersonWithEmail(email);

        assertThat(existsPersonWithEmail).isTrue();
    }

    @Test
    void deleteCustomer() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20,
                Gender.male
        );
        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCustomers()
                .stream().filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        underTest.deleteCustomer(id);
        Optional<Customer> optionalCustomer = underTest.selectCustomer(id);

        assertThat(optionalCustomer).isNotPresent();
    }

    @Test
    void existsPersonWithId() {
        int id =1;
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20,
                Gender.male
        );
        underTest.insertCustomer(customer);

        boolean existsPersonWithId = underTest.existsPersonWithId(id);

        assertThat(existsPersonWithId).isTrue();
    }

    @Test
    void notExistsPersonWithId() {
        int id =-1;
        boolean existsPersonWithId = underTest.existsPersonWithId(id);

        assertThat(existsPersonWithId).isFalse();
    }

    @Test
    void updateCustomerName() {

        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20,
                Gender.male
        );

        underTest.insertCustomer(customer);

        Customer customer1 = underTest.selectAllCustomers()
                .stream().filter(c -> c.getEmail().equals(email))
                .findFirst()
                .orElseThrow();

        String newName = "random";
        customer1.setName(newName);


        underTest.updateCustomer(customer1);
        Optional<Customer> result = underTest.selectCustomer(customer1.getId());

        assertThat(result).isPresent().hasValueSatisfying(c->{
            assertThat(c.getName()).isEqualTo(newName);
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());

        });

    }

    @Test
    void updateAllFieldCustomer() {

        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20,
                Gender.male
        );

        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCustomers()
                .stream().filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        String newName = "random";
        String newEmail=faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        int newAge = 30;
        Customer customer1 = new Customer();
        customer1.setName(newName);
        customer1.setEmail(newEmail);
        customer1.setAge(newAge);
        customer1.setId(id);

        underTest.updateCustomer(customer1);
        Optional<Customer> result = underTest.selectCustomer(id);

        assertThat(result).isPresent().hasValueSatisfying(c->{
            assertThat(c.getName()).isEqualTo(newName);
            assertThat(c.getEmail()).isEqualTo(newEmail);
            assertThat(c.getAge()).isEqualTo(newAge);

        });

    }
}