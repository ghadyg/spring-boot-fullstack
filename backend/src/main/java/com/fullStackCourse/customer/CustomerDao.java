package com.fullStackCourse.customer;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomer(Integer id);
    void insertCustomer(Customer customer);
    boolean existsPersonWithEmail(String email);
    void deleteCustomer(Integer id);
    boolean existsPersonWithId(Integer id);
    void updateCustomer(Customer customer);
    Optional<Customer> selectUserByEmail(String email);
}
