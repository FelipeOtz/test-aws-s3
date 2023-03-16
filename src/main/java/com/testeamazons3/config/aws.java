package com.testeamazons3.config;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@RestController
@RequestMapping("/teste")


@CrossOrigin(origins = "*")
public class aws {

    @RequestMapping(value = "/image", method = RequestMethod.GET,
            produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage() throws IOException {

//        var imgFile = new ClassPathResource("images/img.jpg");
//        byte[] bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());


        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
        Region region = Region.SA_EAST_1;

        byte[] bytes = new byte[0];
        try (S3Client s3 = S3Client.builder().region(region).credentialsProvider(credentialsProvider).build()) {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket("br-interns-interest")
                    .key("FelipeMid.png")
                    .build();

            s3.getObject(getObjectRequest);


            InputStream inputStream = s3.getObject(getObjectRequest);

            bytes = inputStream.readAllBytes();

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }




        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(bytes);
    }


//    @GetMapping("/find")
//    public software.amazon.awssdk.core.ResponseInputStream<GetObjectResponse> find() {
//        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
//        Region region = Region.SA_EAST_1;
//
//
//        try (S3Client s3 = S3Client.builder().region(region).credentialsProvider(credentialsProvider).build()) {
//            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
//                    .bucket("br-interns-interest")
//                    .key("FelipeMid.png")
//                    .build();
//
//            s3.getObject(getObjectRequest);
//        }catch (S3Exception e) {
//            System.out.println("erro!!!!!!!!!!!!!!!!!!!");
//        }
//return null;
//    }

   // public static void main(String[] args) {
     //   ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
       // Region region = Region.SA_EAST_1;
        //S3Client s3 = S3Client.builder().region(region).credentialsProvider(credentialsProvider).build();
        //listBucketObjects(s3, "br-interns-interest");
       // s3.close();
    //}


    public static void listBucketObjects(S3Client s3, String bucketName ) {

        try {
            ListObjectsRequest listObjects = ListObjectsRequest
                    .builder()
                    .bucket(bucketName)
                    .build();

            ListObjectsResponse res = s3.listObjects(listObjects);
            List<S3Object> objects = res.contents();
            for (S3Object myValue : objects) {
                System.out.print("\n The name of the key is " + myValue.key());
                System.out.print("\n The object is " + calKb(myValue.size()) + " KBs");
                System.out.print("\n The owner is " + myValue.owner());
            }

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    //convert bytes to kbs.
    private static long calKb(Long val) {
        return val/1024;
    }
}
