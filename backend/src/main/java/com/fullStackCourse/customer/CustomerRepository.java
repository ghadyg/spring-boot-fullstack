package com.fullStackCourse.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {

    boolean existsCustomerByEmail(String email);
    boolean existsCustomerById(Integer id);
    Optional<Customer> findCustomerByEmail(String email);
    @Modifying(clearAutomatically = true)
    @Query("update Customer c set c.profileImageId = ?1 where c.id = ?2")
    int setProfileImageId(String profileId, Integer customerId);


}
