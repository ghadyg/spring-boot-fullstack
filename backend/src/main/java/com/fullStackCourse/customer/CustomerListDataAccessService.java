package com.fullStackCourse.customer;


import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("fake")
public class CustomerListDataAccessService implements CustomerDao {

    private static List<Customer> customers;

    static {
        customers = new ArrayList<>();

        Customer alex = new Customer(1,"Alex","alex@gmail.com", "password", 22, Gender.male);
        customers.add(alex);

        Customer lily = new Customer(2,"lily","lily@gmail.com", "password", 20, Gender.female);
        customers.add(lily);
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomer(Integer id) {
        return customers.stream()
                .filter(cust->cust.getId().equals(id))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customers.stream().anyMatch(c->c.getEmail().equals(email));
    }

    @Override
    public void deleteCustomer(Integer id) {
        customers.stream()
                .filter(c->c.getId().equals(id))
                .findFirst()
                .ifPresent(customers::remove);
    }

    @Override
    public boolean existsPersonWithId(Integer id) {
        return customers.stream().anyMatch(c->c.getId().equals(id));
    }

    @Override
    public void updateCustomer(Customer customer) {
        customers.stream().filter(c->c.getId().equals(customer.getId()))
                .findFirst().ifPresent(c->new Customer(customer.getId(),
                        customer.getName(), customer.getEmail(),
                        "password", customer.getAge(), customer.getGender()));
    }

    @Override
    public Optional<Customer> selectUserByEmail(String email) {
        return customers.stream()
                .filter(cust->cust.getUsername().equals(email))
                .findFirst();
    }

    @Override
    public void updateCustomerProfileImageId(String profileId, Integer customerId) {

    }

    @Override
    public List<Customer> selectAPageOfCustomer(Integer pageSize,Integer Offset) {
        return null;
    }
}
