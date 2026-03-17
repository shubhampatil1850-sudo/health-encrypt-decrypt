package com.cvs.health;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/json")
public class JsonProcessingController {

    private final JsonProcessingService service;
    public JsonProcessingController(JsonProcessingService service) {
        this.service = service;
    }
    @GetMapping("/process")
    public String process(@RequestParam String file) {
        service.processJson(file);
        return "JSON processed successfully";
    }
}