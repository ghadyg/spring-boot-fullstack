package com.fullStackCourse.auth;

import com.fullStackCourse.customer.CustomerDTO;

public record AuthenticationResponse(String token,CustomerDTO customerDTO) {

}
