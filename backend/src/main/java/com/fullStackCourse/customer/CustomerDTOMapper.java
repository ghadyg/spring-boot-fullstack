package com.fullStackCourse.customer;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CustomerDTOMapper implements Function<Customer,CustomerDTO> {
    @Override
    public CustomerDTO apply(Customer c) {
        return  new CustomerDTO(
                c.getId(),c.getName(),
                c.getEmail(),c.getGender(),
                c.getAge(),c.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())
                ,c.getUsername(),c.getProfileImageId()
        );
    }
}
