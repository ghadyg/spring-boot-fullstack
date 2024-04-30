package com.fullStackCourse.customer;

import com.fullStackCourse.Exception.DuplicateResourceException;
import com.fullStackCourse.Exception.RequestValidationException;
import com.fullStackCourse.Exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers()
    {
        return customerDao.selectAllCustomers();
    }
    public Customer getCustomer(Integer id)
    {
        return customerDao.selectCustomer(id)
                .orElseThrow(()->new ResourceNotFoundException("Customer not found"));
    }

    public void addCustomer(CustomerRegistrationRequest customer)
    {
        String email = customer.email();
        if(customerDao.existsPersonWithEmail(email))
            throw new DuplicateResourceException("Customer already exists");
        customerDao.insertCustomer(new Customer(
                customer.name(),
                customer.email(),
                customer.age()
        ));
    }

    public void deleteCustomer(Integer id)
    {
        if(!customerDao.existsPersonWithId(id))
            throw new ResourceNotFoundException("the user does not exist");

        customerDao.deleteCustomer(id);
    }

    public  void updateCustomer(Integer id,CustomerUpdateRequest customerUpdateRequest)
    {
        Customer customer = getCustomer(id);
        boolean changes = false;
        if(customerUpdateRequest.name() != null && !customerUpdateRequest.name().equals(customer.getName()))
        {
            customer.setName(customerUpdateRequest.name());
            changes = true;
        }

        if(customerUpdateRequest.email() != null && !customerUpdateRequest.email().equals(customer.getEmail()))
        {
            if(customerDao.existsPersonWithEmail(customerUpdateRequest.email()))
                throw  new DuplicateResourceException("Email already taken");
            customer.setEmail(customerUpdateRequest.email());
            changes = true;
        }

        if(customerUpdateRequest.age() != null && ! customerUpdateRequest.age().equals(customer.getAge()))
        {
            customer.setAge(customerUpdateRequest.age());
            changes = true;
        }

        if(!changes)
            throw new RequestValidationException("No changes made");

        customerDao.updateCustomer(customer);

    }

}
