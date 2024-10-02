package ma.solide;


import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;

public class SolideSqs {
	public static void main(String[] args) {
		Region region = Region.EU_CENTRAL_1;
		SqsClient sqsClient = SqsClient.builder()
			    .region(region)
			    .credentialsProvider(ProfileCredentialsProvider.create())
			    .build();
		CreateQueueRequest createStandardQueueRequest = CreateQueueRequest.builder()
			    .queueName("solideSQS")
			    .build();

			sqsClient.createQueue(createStandardQueueRequest);
	}
}
