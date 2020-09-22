package com.gcast.gcastminio;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.gcast.gcastminio.components.MinioComponent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class GcastminioApplication {

	public static void main(String[] args)
			throws InvalidKeyException, IllegalArgumentException, NoSuchAlgorithmException, IOException {
		ApplicationContext applicationContext = SpringApplication.run(GcastminioApplication.class, args);

		MinioComponent minIOComponent = applicationContext.getBean(MinioComponent.class);
		minIOComponent.ReadWriteMinIo();

		System.out.println ("Done");
	}
}
