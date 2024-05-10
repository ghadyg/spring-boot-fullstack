package com.fullStackCourse.customer;

import com.fullStackCourse.Exception.DuplicateResourceException;
import com.fullStackCourse.Exception.RequestValidationException;
import com.fullStackCourse.Exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }



    @Test
    void getAllCustomers() {
        underTest.getAllCustomers();

        verify(customerDao).selectAllCustomers();

    }

    @Test
    void canGetCustomer() {
        Integer id = 1;
        Customer customer = new Customer(
                id,"ghady","ghady@gmail.com",22,
                Gender.male);
        when(customerDao.selectCustomer(id)).thenReturn(Optional.of(customer));

        Customer actual = underTest.getCustomer(1);

        assertThat(actual).isEqualTo(customer);

    }

    @Test
    void canNotGetCustomer() {
        Integer id = 1;
        Customer customer = new Customer(
                id,"ghady","ghady@gmail.com",22,
                Gender.male);
        when(customerDao.selectCustomer(id)).thenReturn(Optional.empty());

        assertThatThrownBy(()->underTest.getCustomer(1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Customer not found");



    }

    @Test
    void canNotAddCustomer() {
        Integer id = 1;
        String email = "ghady@gmail.com";
        when(customerDao.existsPersonWithEmail(email)).thenReturn(true);


        CustomerRegistrationRequest customer = new CustomerRegistrationRequest(
                "ghady", email,22,Gender.male
        );

        assertThatThrownBy(()->underTest.addCustomer(customer))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Customer already exists");

        verify(customerDao,never()).insertCustomer(any());

    }

    @Test
    void addCustomer()
    {
        Integer id = 1;
        String email = "ghady@gmail.com";
        when(customerDao.existsPersonWithEmail(email)).thenReturn(false);


        CustomerRegistrationRequest customer = new CustomerRegistrationRequest(
                "ghady", email,22,Gender.male
        );

        underTest.addCustomer(customer);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );
        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(customer.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.age());
        assertThat(capturedCustomer.getGender()).isEqualTo(Gender.male);


    }


    @Test
    void deleteCustomer() {
        Integer id = 10;
        when(customerDao.existsPersonWithId(id)).thenReturn(true);

        underTest.deleteCustomer(id);

        verify(customerDao).deleteCustomer(id);
    }

    @Test
    void canNotDeleteCustomer() {
        Integer id = 10;
        when(customerDao.existsPersonWithId(id)).thenReturn(false);

        assertThatThrownBy(()->underTest.deleteCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("the user does not exist");

        verify(customerDao,never()).deleteCustomer(any());

    }

    @Test
    void updateCustomer() {
        Integer id = 1;
        Customer customer = new Customer(
                id,"ghady","ghady@gmail.com",22,
                Gender.male);
        when(customerDao.selectCustomer(id)).thenReturn(Optional.of(customer));

        String email = "alex@gmail.com";
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
                "alex", email, 25);

        when(customerDao.existsPersonWithEmail(email)).thenReturn(false);
        underTest.updateCustomer(id, customerUpdateRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer captured = customerArgumentCaptor.getValue();

        assertThat(captured.getAge()).isEqualTo(customerUpdateRequest.age());
        assertThat(captured.getName()).isEqualTo(customerUpdateRequest.name());
        assertThat(captured.getEmail()).isEqualTo(customerUpdateRequest.email());
        assertThat(captured.getGender()).isEqualTo(Gender.male);

    }

    @Test
    void canNotUpdateCustomerThrowDup() {
        Integer id = 1;
        Customer customer = new Customer(
                id,"ghady","ghady@gmail.com",22,
                Gender.male);
        when(customerDao.selectCustomer(id)).thenReturn(Optional.of(customer));

        String email = "alex@gmail.com";
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
                "alex", email, 25);

        when(customerDao.existsPersonWithEmail(email)).thenReturn(true);

        assertThatThrownBy(()->underTest.updateCustomer(id,customerUpdateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Email already taken");

        verify(customerDao,never()).updateCustomer(any());

    }

    @Test
    void canNotUpdateCustomerNoChanges() {
        Integer id = 1;
        Customer customer = new Customer(
                id,"ghady","ghady@gmail.com",22,
                Gender.male);
        when(customerDao.selectCustomer(id)).thenReturn(Optional.of(customer));

        String email = "alex@gmail.com";
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
                customer.getName(), customer.getEmail(), customer.getAge());

        assertThatThrownBy(()->underTest.updateCustomer(id,customerUpdateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("No changes made");

        verify(customerDao,never()).updateCustomer(any());

    }
}