package com.fullStackCourse.customer;

import com.fullStackCourse.AbstractTestcontainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CustomerJPADataAccessServiceTest extends AbstractTestcontainer {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {

        underTest.selectAllCustomers();

        verify(customerRepository).findAll();


    }

    @Test
    void selectAPageOfCustomer() {
        int pageSize = 100;
        int offset = 0;

        Customer customer = new Customer(
                faker.name().fullName(),
                faker.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                "password", 20,
                Gender.male
        );


        List<Customer> customers = List.of(customer);
        Page pageMock = mock(Page.class);

        when(pageMock.getContent()).thenReturn(customers);
        when(customerRepository.findAll(any(Pageable.class))).thenReturn(pageMock);

        List<Customer> expected = underTest.selectAPageOfCustomer(pageSize,offset);

        assertThat(expected).isEqualTo(customers);
        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(customerRepository).findAll(pageableArgumentCaptor.capture());
        assertThat(pageableArgumentCaptor.getValue()).isEqualTo(Pageable.ofSize(pageSize));
    }

    @Test
    void selectCustomer() {

        int id = 1;

        underTest.selectCustomer(id);

        verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                "password", 20,
                Gender.male
        );

        underTest.insertCustomer(customer);

        verify(customerRepository).save(customer);
    }

    @Test
    void existsPersonWithEmail() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        underTest.existsPersonWithEmail(email);

        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void existsPersonWithId() {

        Integer id = 1;
        underTest.existsPersonWithId(id);

        verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void updateCustomer() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                "password", 20,
                Gender.male
        );

        underTest.updateCustomer(customer);

        verify(customerRepository).save(customer);
    }

    @Test
    void deleteCustomer() {
        Integer id = 1;

        underTest.deleteCustomer(id);

        verify(customerRepository).deleteById(id);
    }

    @Test
    void canUpdateProfile()
    {
        String profileId = "222";

        Integer customerid = 1;

        underTest.updateCustomerProfileImageId(profileId,customerid);

        verify(customerRepository).setProfileImageId(profileId,customerid);
    }
}