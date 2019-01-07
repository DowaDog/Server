package com.sopt.dowadog.service.common;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.sopt.dowadog.util.S3Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Service
public class S3FileServiceImpl implements FileService {

    @Autowired
    private AmazonS3 s3client;

    @Value("${cloud.aws.bucket}")
    private String bucketName;


    //todo 파일 삭제 관련 생각
    @Async
    @Override
    public void fileUpload(MultipartFile multipartFile, String filePath) {
        try {

            File convFile = new File(multipartFile.getOriginalFilename());
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(multipartFile.getBytes());
            fos.close();
            s3client.putObject(new PutObjectRequest(bucketName, filePath, convFile));
            convFile.delete();

            log.info("===================== Upload File - Done! =====================");
        } catch (AmazonServiceException ase) {
            printS3Error(ase);
        } catch (AmazonClientException ace) {
            log.info("Caught an AmazonClientException: ");
            log.info("Error Message: " + ace.getMessage());
        } catch (IOException ioe) {
            log.info("IOE Error Message: " + ioe.getMessage());
        }
    }


    @Override
    public void fileDownload(String filePath) {
        try {
            System.out.println("Downloading an object");
            S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, filePath));
            System.out.println("Content-Type: " + s3object.getObjectMetadata().getContentType());
            S3Util.displayText(s3object.getObjectContent());
            log.info("===================== Import File - Done! =====================");

        } catch (AmazonServiceException ase) {
            printS3Error(ase);
        } catch (AmazonClientException ace) {
            log.info("Caught an AmazonClientException: ");
            log.info("Error Message: " + ace.getMessage());
        } catch (IOException ioe) {
            log.info("IOE Error Message: " + ioe.getMessage());
        }

    }

    private void printS3Error(AmazonServiceException ase) {
        log.info("Caught an AmazonServiceException from PUT requests, rejected reasons:");
        log.info("Error Message:    " + ase.getMessage());
        log.info("HTTP Status Code: " + ase.getStatusCode());
        log.info("AWS Error Code:   " + ase.getErrorCode());
        log.info("Error Type:       " + ase.getErrorType());
        log.info("Request ID:       " + ase.getRequestId());
    }
}
