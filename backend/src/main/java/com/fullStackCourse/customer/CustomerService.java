package com.fullStackCourse.customer;

import com.fullStackCourse.Exception.DuplicateResourceException;
import com.fullStackCourse.Exception.RequestValidationException;
import com.fullStackCourse.Exception.ResourceNotFoundException;
import com.fullStackCourse.s3.S3Buckets;
import com.fullStackCourse.s3.S3Service;
import io.micrometer.common.util.StringUtils;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerDao customerDao;
    private final PasswordEncoder passwordEncoder;
    private final CustomerDTOMapper customerDTOMapper;
    private final S3Service s3Service;
    private final S3Buckets s3Buckets;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao, PasswordEncoder passwordEncoder, CustomerDTOMapper customerDTOMapper, S3Service s3Service, S3Buckets s3Buckets) {
        this.customerDao = customerDao;
        this.passwordEncoder = passwordEncoder;
        this.customerDTOMapper = customerDTOMapper;
        this.s3Service = s3Service;
        this.s3Buckets = s3Buckets;
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
    public List<CustomerDTO> selectAPageOfCustomer(Integer pageSize,Integer offset){
            return  customerDao.selectAPageOfCustomer(pageSize,offset)
                    .stream().map(customerDTOMapper).collect(Collectors.toList());
    }

    public void uploadCustomerImage(Integer id, MultipartFile file) {
        if(!customerDao.existsPersonWithId(id))
            throw new ResourceNotFoundException("the user does not exist");
        String profileImageId = UUID.randomUUID().toString();
        try {

            s3Service.putObject(
                    s3Buckets.getCustomer(),
                    "profile-images/%s/%s".formatted(id, profileImageId),
                    file.getBytes()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        customerDao.updateCustomerProfileImageId(profileImageId,id);

    }

    public byte[] getCustomerProfilePicture(Integer id) {
        var customer = customerDao.selectCustomer(id).map(customerDTOMapper)
                .orElseThrow(()->new ResourceNotFoundException("Customer not found"));

        //TODO check if profileImageID is empty or null
        if(StringUtils.isBlank(customer.profileImageId())){
            throw  new ResourceNotFoundException("customer does not have profile");
        }

        return s3Service.getObject(
            s3Buckets.getCustomer(),
                "profile-images/%s/%s".formatted(id, customer.profileImageId())
        );

    }
}
