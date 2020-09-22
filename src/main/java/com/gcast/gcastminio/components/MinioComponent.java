package com.gcast.gcastminio.components;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.gcast.gcastminio.services.MinIOService;
import com.gcast.gcastminio.services.S3Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MinioComponent {
    
    @Autowired
    private S3Service s3Service;

    @Autowired
    private MinIOService minioService;

    public void ReadWriteMinIo()
        throws InvalidKeyException, IllegalArgumentException, NoSuchAlgorithmException, IOException {

        // Interact with MinIO using AWS S3 SDK
        String s3fileName = "S3test.txt";
        s3Service.UploadWithS3Client(s3fileName);
        s3Service.ListS3Objects();
        s3Service.PrintObjectContents(s3fileName);
        s3Service.DownloadFromMinIOWithS3Client(s3fileName);

        // Interact with MinIO using MinIO SDK
        String miniofileName = "miniotest.txt";
        minioService.WriteToMinIO(miniofileName);
        minioService.ReadFromMinIO(miniofileName);
        }
}
