package com.encryption.envelope;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/json")
public class JsonProcessingController {

    private final JsonProcessingService service;
    public JsonProcessingController(JsonProcessingService service) {
        this.service = service;
    }
    
    @GetMapping("/encrypt")
    public String encrypt(@RequestParam String file) {

        service.encryptAndUpload(file);

        return "File encrypted and uploaded to encrypted bucket";
    }

    @GetMapping("/decrypt")
    public String decrypt(@RequestParam String file) {

        service.decryptAndUpload(file);

        return "File decrypted and uploaded to decrypted bucket";
    }
}
