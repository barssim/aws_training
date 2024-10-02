package ma.solide;

import java.util.List;
import java.util.stream.Collectors;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

public class S3Listing {
	public static void main(String[] args) {
		Region region = Region.EU_CENTRAL_1;
		S3Client s3 = S3Client.builder().region(region)
				.credentialsProvider(ProfileCredentialsProvider.create()).build();

		listS3Buckets(s3);
	}

	public static List<Bucket> listS3Buckets(S3Client s3) {
		List<Bucket> bucketsList = null;
		try {
			ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().build();
			ListBucketsResponse listBucketsResponse = s3.listBuckets(listBucketsRequest);

			System.out.println("Buckets:");
			bucketsList = listBucketsResponse.buckets().stream().collect(Collectors.toList());
			bucketsList.forEach(bucket -> System.out.println(bucket.name()));
			bucketsList.forEach(bucket -> listObjectsInBucket(bucket, s3));
		} catch (S3Exception e) {
			System.err.println(e.awsErrorDetails().errorMessage());
			System.exit(1);
		}
		return bucketsList;
	}
	
	static void listObjectsInBucket(Bucket bucket, S3Client s3) {
	   
	    ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
	      .bucket(bucket.name())
	      .build();
	    ListObjectsV2Response listObjectsV2Response = s3.listObjectsV2(listObjectsV2Request);

	    List<S3Object> contents = listObjectsV2Response.contents();

	    System.out.println("Number of objects in the bucket: " + contents.stream().count());
	    contents.stream().forEach(System.out::println);
	    
	    s3.close();
	}
	
}
