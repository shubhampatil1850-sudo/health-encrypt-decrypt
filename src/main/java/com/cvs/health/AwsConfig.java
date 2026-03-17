package com.cvs.health;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.regions.Region;

@Configuration
public class AwsConfig {

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.EU_NORTH_1)
                .build();
    }

    @Bean
    public KmsClient kmsClient() {
        return KmsClient.builder()
                .region(Region.EU_NORTH_1)
                .build();
    }
}