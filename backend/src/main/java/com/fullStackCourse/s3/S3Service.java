package com.fullStackCourse.s3;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
public class S3Service {

    private final S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void putObject(String bucketName, String key, byte[] file){
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

       s3Client.putObject(objectRequest, RequestBody.fromBytes(file));
    }

    public byte[] getObject(String bucketName,String keyName){
        GetObjectRequest objectRequest = GetObjectRequest
                .builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        ResponseInputStream<GetObjectResponse> objectBytes = s3Client.getObject(objectRequest);

        try {
            return objectBytes.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
