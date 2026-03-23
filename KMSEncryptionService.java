package com.encryption.envelope;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.*;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class KMSEncryptionService {

    private final KmsClient kmsClient;

    public KMSEncryptionService(KmsClient kmsClient) {
        this.kmsClient = kmsClient;
    }

    @Value("${aws.kms.key}")
    private String kmsKey;

    // ================= ENCRYPT =================
    public String encrypt(String data) {

        try {
            System.out.println("===== 🔐 ENVELOPE ENCRYPT START =====");

            // 1. Generate Data Key from KMS
            GenerateDataKeyRequest request = GenerateDataKeyRequest.builder()
                    .keyId(kmsKey)
                    .keySpec(DataKeySpec.AES_256)
                    .build();

            GenerateDataKeyResponse response = kmsClient.generateDataKey(request);

            byte[] plainDataKey = response.plaintext().asByteArray();
            byte[] encryptedDataKey = response.ciphertextBlob().asByteArray();

            // 2. Encrypt JSON using AES (local)
            SecretKey secretKey = new SecretKeySpec(plainDataKey, "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedData = cipher.doFinal(data.getBytes());

            // 3. Encode both
            String encryptedJson = Base64.getEncoder().encodeToString(encryptedData);
            String encryptedKey = Base64.getEncoder().encodeToString(encryptedDataKey);

            // 4. Combine (store both)
            String finalPayload = encryptedKey + "::" + encryptedJson;

            System.out.println("Encrypted Key: " + encryptedKey);
            System.out.println("Encrypted Data: " + encryptedJson);

            System.out.println("===== 🔐 ENVELOPE ENCRYPT END =====");

            return finalPayload;

        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    // ================= DECRYPT =================
    public String decrypt(String payload) {

        try {
            System.out.println("===== 🔓 ENVELOPE DECRYPT START =====");

            // 1. Split payload
            String[] parts = payload.split("::");
            String encryptedKey = parts[0];
            String encryptedData = parts[1];

            // 2. Decode
            byte[] encryptedKeyBytes = Base64.getDecoder().decode(encryptedKey);
            byte[] encryptedDataBytes = Base64.getDecoder().decode(encryptedData);

            // 3. Decrypt Data Key using KMS
            DecryptRequest decryptRequest = DecryptRequest.builder()
                    .ciphertextBlob(
                            software.amazon.awssdk.core.SdkBytes.fromByteArray(encryptedKeyBytes)
                    )
                    .build();

            DecryptResponse decryptResponse = kmsClient.decrypt(decryptRequest);

            byte[] plainDataKey = decryptResponse.plaintext().asByteArray();

            // 4. Decrypt JSON using AES
            SecretKey secretKey = new SecretKeySpec(plainDataKey, "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decryptedData = cipher.doFinal(encryptedDataBytes);

            String result = new String(decryptedData);

            System.out.println("Decrypted JSON: " + result);

            System.out.println("===== 🔓 ENVELOPE DECRYPT END =====");

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}
