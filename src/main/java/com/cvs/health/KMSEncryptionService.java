package com.cvs.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.*;

import java.util.Base64;

@Service
public class KMSEncryptionService {

    private final KmsClient kmsClient;
    public KMSEncryptionService(KmsClient kmsClient) {
    		this.kmsClient = kmsClient;
    	}

    @Value("${aws.kms.key}")
    private String kmsKey;

    public String encrypt(String data) {

        EncryptRequest request = EncryptRequest.builder()
                .keyId(kmsKey)
                .plaintext(
                        software.amazon.awssdk.core.SdkBytes.fromUtf8String(data)
                )
                .build();

        EncryptResponse response = kmsClient.encrypt(request);

        return Base64.getEncoder()
                .encodeToString(response.ciphertextBlob().asByteArray());
    }

    public String decrypt(String encrypted) {

        byte[] decoded = Base64.getDecoder().decode(encrypted);

        DecryptRequest request = DecryptRequest.builder()
                .ciphertextBlob(
                        software.amazon.awssdk.core.SdkBytes.fromByteArray(decoded)
                )
                .build();

        DecryptResponse response = kmsClient.decrypt(request);

        return response.plaintext().asUtf8String();
    }
}