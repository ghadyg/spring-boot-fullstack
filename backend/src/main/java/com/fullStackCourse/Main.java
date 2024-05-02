package com.fullStackCourse;


import com.fullStackCourse.customer.Customer;
import com.fullStackCourse.customer.CustomerRepository;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;


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
    CommandLineRunner runner(CustomerRepository customerRepository){
        return args -> {
            Random random = new Random();
            var faker = new Faker();
            Name name = faker.name();
            String firstName = name.firstName();
            String lastName = name.lastName();
            Customer customer = new Customer(
                    firstName+" "+lastName,
                    firstName +"."+ lastName +"@example.com",
                    random.nextInt(10,99)
            );


            customerRepository.save(customer);
        };
    }

}
