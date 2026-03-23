package com.encryption.envelope;

import org.springframework.stereotype.Service; 
import software.amazon.awssdk.services.s3.S3Client; 
import software.amazon.awssdk.services.s3.model.*; 

@Service 
public class S3Service 
{ 
	private final S3Client s3Client; 
	public S3Service(S3Client s3Client) 
	{ this.s3Client=s3Client; } 
	public String readJson(String bucket, String key) {
		GetObjectRequest request = GetObjectRequest.builder() .bucket(bucket) .key(key) .build(); 
		return s3Client.getObjectAsBytes(request).asUtf8String(); 
		} 
	
	public void uploadJson(String bucket, String key, String data) 
	{ PutObjectRequest request = PutObjectRequest.builder() .bucket(bucket) .key(key) .build(); 
	s3Client.putObject( request, software.amazon.awssdk.core.sync.RequestBody.fromString(data) ); 
	} }
