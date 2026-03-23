package com.encryption.envelope;

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

    public void encryptAndUpload(String fileName) {

        String json = s3Service.readJson(inputBucket, fileName);

        String encryptedPayload = kmsService.encrypt(json);

        s3Service.uploadJson(encryptedBucket, fileName, encryptedPayload);
    }

    public void decryptAndUpload(String fileName) {

        String payload = s3Service.readJson(encryptedBucket, fileName);

        String decrypted = kmsService.decrypt(payload);

        s3Service.uploadJson(decryptedBucket, fileName, decrypted);
    }
}
