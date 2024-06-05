package com.fullStackCourse.s3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

    private S3Service underTest;

    @Mock
    private S3Client s3Client;

    @BeforeEach
    void setUp() {
        underTest =  new S3Service(s3Client);
    }

    @Test
    void putObject() {
        String bucketName = "customer";
        String testing = "testing";
        byte[] file = "helloworld".getBytes();
        underTest.putObject(bucketName, testing, file);

        ArgumentCaptor<PutObjectRequest> putObjectRequestArgumentCaptor =
                ArgumentCaptor.forClass(PutObjectRequest.class);

        ArgumentCaptor<RequestBody> requestBodyArgumentCaptor =
                ArgumentCaptor.forClass(RequestBody.class);
        verify(s3Client).putObject(putObjectRequestArgumentCaptor.capture(),requestBodyArgumentCaptor.capture());

        PutObjectRequest putObjectRequestArgumentCaptorValue = putObjectRequestArgumentCaptor.getValue();

        assertThat(putObjectRequestArgumentCaptorValue.bucket()).isEqualTo(bucketName);
        assertThat(putObjectRequestArgumentCaptorValue.key()).isEqualTo(testing);

        RequestBody requestBodyArgumentCaptorValue = requestBodyArgumentCaptor.getValue();

        try {
            assertThat(requestBodyArgumentCaptorValue.contentStreamProvider().newStream().readAllBytes())
                    .isEqualTo(RequestBody.fromBytes(file).contentStreamProvider().newStream().readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Test
    void getObject() throws IOException {
        String bucketName = "customer";
        String key = "testing";
        byte[] file = "helloworld".getBytes();
        GetObjectRequest objectRequest = GetObjectRequest
                .builder()
                .bucket(bucketName)
                .key(key)
                .build();

        ResponseInputStream<GetObjectResponse> res = mock(ResponseInputStream.class);
        when(res.readAllBytes()).thenReturn(file);

        when(s3Client.getObject(eq(objectRequest))).thenReturn(res);

        byte[] underTestObject = underTest.getObject(bucketName, key);

        assertThat(underTestObject).isEqualTo(file);
    }

    @Test
    void cannotGetObject() throws IOException {
        String bucketName = "customer";
        String key = "testing";
        byte[] file = "helloworld".getBytes();
        GetObjectRequest objectRequest = GetObjectRequest
                .builder()
                .bucket(bucketName)
                .key(key)
                .build();

        ResponseInputStream<GetObjectResponse> res = mock(ResponseInputStream.class);
        when(res.readAllBytes()).thenThrow(new IOException("cannot read bytes"));

        when(s3Client.getObject(eq(objectRequest))).thenReturn(res);

       assertThatThrownBy(()-> underTest.getObject(bucketName,key))
               .isInstanceOf(RuntimeException.class)
               .hasMessageContaining("cannot read bytes")
               .hasRootCauseInstanceOf(IOException.class);

    }
}
