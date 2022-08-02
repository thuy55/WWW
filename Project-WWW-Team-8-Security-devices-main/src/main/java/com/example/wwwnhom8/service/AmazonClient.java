package com.example.wwwnhom8.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.wwwnhom8.util.FileHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;

@Service
public class AmazonClient {

    private AmazonS3 s3client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;
    @Value("${amazonProperties.region}")
    private String region;

    private FileHelper fileHelper=new FileHelper();

    @PostConstruct
    private void initializeAmazon() {
//        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
        this.s3client = AmazonS3ClientBuilder
                .standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .build();
    }

    public String uploadFile(MultipartFile multipartFile) {

        String fileUrl = "";
        try {
            File file = fileHelper.convertMultiPartToFile(multipartFile);
            String fileName = fileHelper.generateFileName(multipartFile);
//            fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
            fileUrl = endpointUrl + "/" + fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public void deleteBucketObjects(String objectName) {
//        LOGGER.info("Deleting file with name= " + keyName);
        final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, objectName);
        s3client.deleteObject(deleteObjectRequest);
//        LOGGER.info("File deleted successfully.");
//        s3client.deleteObject(bucketName,objectName);
        /* Delete single object 's3.png' */
//        s3client.deleteObject(bucketName, objectName);

//        s3client.deleteObject(new DeleteObjectRequest(bucketName, objectName));

        /* Create an Object of DeleteObjectsRequest - Arguments: BucketName */
//        DeleteObjectsRequest request = new DeleteObjectsRequest(bucketName);

        /* Set Object Keys to delete */
//        request.setKeys(Arrays.asList(new DeleteObjectsRequest.KeyVersion[] { new DeleteObjectsRequest.KeyVersion(objectName)}));

        /* Send Delete Objects Request */
//        DeleteObjectsResult result = s3client.deleteObjects(request);

        /* Printing Deleted Object Keys */
//        if (result.getDeletedObjects() != null) {
//            result.getDeletedObjects().stream().forEach(e -> System.out.println(e.getKey()));
//        }
    }
}
