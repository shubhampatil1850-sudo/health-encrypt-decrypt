package com.cvs.health;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JsonProcessingService {

    private final S3Service s3Service;
    
    private final KMSEncryptionService kmsService;
    
    public JsonProcessingService(S3Service s3Service,
            KMSEncryptionService kmsService) {
    			this.s3Service = s3Service;
    			this.kmsService = kmsService;
    		}

    @Value("${aws.bucket.input}")
    private String inputBucket;

    @Value("${aws.bucket.encrypted}")
    private String encryptedBucket;

    @Value("${aws.bucket.decrypted}")
    private String decryptedBucket;

    public void processJson(String fileName) {

        // Read JSON
        String json = s3Service.readJson(inputBucket, fileName);

        // Encrypt
        String encrypted = kmsService.encrypt(json);

        // Upload encrypted
        s3Service.uploadJson(encryptedBucket, fileName, encrypted);

        // Decrypt
        String decrypted = kmsService.decrypt(encrypted);

        // Upload decrypted
        s3Service.uploadJson(decryptedBucket, fileName, decrypted);
    }
}
