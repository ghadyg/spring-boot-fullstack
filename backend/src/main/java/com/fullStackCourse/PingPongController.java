package com.fullStackCourse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {
    private static Integer Counter =0;
    record PingPong(
            String result
    ){}
    @GetMapping("/ping")
    public PingPong getPingPong(){

        return new PingPong("PONG: "+ ++Counter);
    }
}
