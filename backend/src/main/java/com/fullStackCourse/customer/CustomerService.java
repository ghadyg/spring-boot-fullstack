package com.fullStackCourse.customer;

import com.fullStackCourse.Exception.DuplicateResourceException;
import com.fullStackCourse.Exception.RequestValidationException;
import com.fullStackCourse.Exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerDao customerDao;
    private final PasswordEncoder passwordEncoder;
    private final CustomerDTOMapper customerDTOMapper;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao, PasswordEncoder passwordEncoder, CustomerDTOMapper customerDTOMapper) {
        this.customerDao = customerDao;
        this.passwordEncoder = passwordEncoder;
        this.customerDTOMapper = customerDTOMapper;
    }

    public List<CustomerDTO> getAllCustomers()
    {
        return customerDao.selectAllCustomers()
                .stream().map(customerDTOMapper).collect(Collectors.toList());
    }
    public CustomerDTO getCustomer(Integer id)
    {
        return customerDao.selectCustomer(id).map(customerDTOMapper)
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
                passwordEncoder.encode(customer.password()), customer.age(),
                customer.gender()
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
        Customer customer = customerDao.selectCustomer(id).orElseThrow(()->new ResourceNotFoundException("Customer not found"));
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
