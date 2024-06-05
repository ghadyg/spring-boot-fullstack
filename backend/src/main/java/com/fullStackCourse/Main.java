package com.fullStackCourse;


import com.fullStackCourse.customer.Customer;
import com.fullStackCourse.customer.CustomerRepository;
import com.fullStackCourse.customer.Gender;
import com.fullStackCourse.s3.S3Buckets;
import com.fullStackCourse.s3.S3Service;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;

@SpringBootApplication
public class Main {


    public static void main(String[] args) {

        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(Main.class, args);
//        String[] beanDefinitionNames = configurableApplicationContext.getBeanDefinitionNames();
//
//        for(var n : beanDefinitionNames)
//        {
//            System.out.println(n);
//        }
    }


    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository,
                             PasswordEncoder passwordEncoder){
        return args -> {
                saveCustomer(customerRepository, passwordEncoder);
            //testingBucket(s3service, s3Bcukets);
        };
    }

    private static void testingBucket(S3Service s3service, S3Buckets s3Buckets) {
        s3service.putObject(s3Buckets.getCustomer(),
                "test",
                    "Hello World".getBytes());


        byte[] object = s3service.getObject(s3Buckets.getCustomer(),
                "test");

        System.out.println(new String(object));
    }

    private static void saveCustomer(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        Random random = new Random();
        var faker = new Faker();
        Name name = faker.name();
        String firstName = name.firstName();
        String lastName = name.lastName();
        Customer customer = new Customer(
                firstName+" "+lastName,
                firstName +"."+ lastName +"@example.com",
                passwordEncoder.encode("password"), random.nextInt(16,99),
                Gender.female
        );


        customerRepository.save(customer);
    }

}
