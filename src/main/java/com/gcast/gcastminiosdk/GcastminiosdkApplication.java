package com.gcast.gcastminiosdk;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.gcast.gcastminiosdk.components.MinioComponent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class GcastminiosdkApplication {

	public static void main(String[] args)
			throws InvalidKeyException, IllegalArgumentException, NoSuchAlgorithmException, IOException {
		ApplicationContext applicationContext = SpringApplication.run(GcastminiosdkApplication.class, args);

		String fileName = "test.txt";
		MinioComponent minIOComponent = applicationContext.getBean(MinioComponent.class);
		minIOComponent.ReadWriteMinIo(fileName);

		System.out.println ("Done");
	}
}
