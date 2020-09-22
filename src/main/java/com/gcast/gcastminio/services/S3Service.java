package com.gcast.gcastminio.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class S3Service {

    // The following are set in application.properties
    @Value("${minio.endPoint}")
    private String endPoint;
    @Value("${minio.accessKey}")
    private String accessKey;
    @Value("${minio.secretKey}")
    private String secretKey;
    @Value("${minio.bucketName}")
    private String bucketName;
    @Value("${localFileFolder}")
    private String localFileFolder;

    public void UploadWithS3Client(String fileName) throws IOException {
        AmazonS3 s3Client = getAmazonS3Client(accessKey, secretKey, endPoint);
        String fileToUpload = localFileFolder + fileName;
        try {
            File file = new File(fileToUpload);

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file);
            s3Client.putObject(putObjectRequest);
        } catch (AmazonServiceException ase) {
            System.out.println("Error Message:    " + ase.getMessage());

        } catch (AmazonClientException ace) {
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

    public List<String> ListS3Objects() {
        List<String> blobList = new ArrayList<String>();
        System.out.format("Objects in S3 bucket %s:\n", bucketName);
        AmazonS3 s3Client = getAmazonS3Client(accessKey, secretKey, endPoint);
        ListObjectsV2Result result = s3Client.listObjectsV2(bucketName);
        List<S3ObjectSummary> blobs = result.getObjectSummaries();
        for (S3ObjectSummary blob : blobs) {
            blobList.add(blob.getKey());
            System.out.println("* " + blob.getKey());
        }
        return blobList;
    }

    public void PrintObjectContents(String objectName) throws IOException {
        AmazonS3 s3Client = getAmazonS3Client(accessKey, secretKey, endPoint);
        GetObjectRequest rangeObjectRequest = new GetObjectRequest(bucketName, objectName);
        S3Object objectPortion = s3Client.getObject(rangeObjectRequest);
        System.out.println("Printing bytes retrieved:");
        displayTextInputStream(objectPortion.getObjectContent());
    }

    private static void displayTextInputStream(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while (true) {
            String line = reader.readLine();
            if (line == null)
                break;

            System.out.println("    " + line);
        }
        System.out.println();
    }

	public void DownloadFromMinIOWithS3Client(String objectName) {
		System.out.format("Downloading %s from S3 bucket %s...\n", objectName, bucketName);
		AmazonS3 s3Client = getAmazonS3Client(accessKey, secretKey, endPoint);
		try {
			S3Object o = s3Client.getObject(bucketName, objectName);
			S3ObjectInputStream s3is = o.getObjectContent();
			String downloadedFile = localFileFolder + "D_" + objectName;
			FileOutputStream fos = new FileOutputStream(new File(downloadedFile));
			byte[] read_buf = new byte[1024];
			int read_len = 0;
			while ((read_len = s3is.read(read_buf)) > 0) {
				fos.write(read_buf, 0, read_len);
			}
			s3is.close();
			fos.close();
		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
			System.exit(1);
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
			}
		}


    public static AmazonS3 getAmazonS3Client(String accessKey, String secretKey, String endPoint) {
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTP);
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, Regions.US_EAST_1.name()))
                .withPathStyleAccessEnabled(true)
                .withClientConfiguration(clientConfig)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        return s3client;
	}
}
