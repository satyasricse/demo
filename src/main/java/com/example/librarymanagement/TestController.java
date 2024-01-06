package com.example.librarymanagement;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class TestController {
    @GetMapping("/test")
    protected HashMap<String,String> test(){
        return new HashMap<>();
    }
}
