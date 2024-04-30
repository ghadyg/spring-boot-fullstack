package com.fullStackCourse.customer;

public record CustomerUpdateRequest (
        String name,
        String email,
        Integer age
) {
}

