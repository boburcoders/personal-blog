package com.company.PersonalBlog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;

    private final String bucketName = "my-first-s3-service";


    public String uploadFile(String originalName, byte[] data, String mimeType, String generatedName) {
        // Generate a unique name for the file in S3

        // Create the S3 PutObjectRequest
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(generatedName)
                .contentType(mimeType)
                .build();

        // Upload the file to S3
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(data));

        // Generate a URL to access the file (optional)
        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, generatedName);
    }

    public void deleteFileByGeneratedName(String generatedName) {
        // Create the DeleteObjectRequest with the bucket name and key (generated name)
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(generatedName)
                .build();

        // Call the S3 client to delete the file
        s3Client.deleteObject(deleteObjectRequest);
    }


}
