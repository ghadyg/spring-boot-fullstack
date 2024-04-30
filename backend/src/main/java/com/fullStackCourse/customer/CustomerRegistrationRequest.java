package com.fullStackCourse.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age
) {
}
