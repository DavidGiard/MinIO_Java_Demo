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

    public void ReadWriteMinIo(String fileName)
        throws InvalidKeyException, IllegalArgumentException, NoSuchAlgorithmException, IOException {

        s3Service.UploadWithS3Client(fileName);
        s3Service.ListS3Objects();
        s3Service.PrintObjectContents(fileName);
        s3Service.DownloadFromMinIOWithS3Client(fileName);



        // minioService.WriteToMinIO(fileName);
        // minioService.ReadFromMinIO(fileName);
        }
}
