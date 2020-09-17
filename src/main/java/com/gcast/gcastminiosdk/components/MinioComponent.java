package com.gcast.gcastminiosdk.components;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.gcast.gcastminiosdk.services.MinIOService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MinioComponent {
    
    @Autowired
    private MinIOService minioService;

    public void ReadWriteMinIo(String fileName)
    throws InvalidKeyException, IllegalArgumentException, NoSuchAlgorithmException, IOException {
        minioService.WriteToMinIO(fileName);
        minioService.ReadFromMinIO(fileName);
        System.out.println ("MinIODemo");
        }
}
