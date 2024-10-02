package ma.solide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

import ma.solide.models.Person;


public class StorePersonFunction implements RequestStreamHandler {

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		JSONParser parser = new JSONParser();
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		JSONObject responseJson = new JSONObject();
		try {
			JSONObject event = (JSONObject) parser.parse(reader);
			Object body = event.get("body");
			if ( body != null)
			{
				Person person = new Person(body.toString());
				DynamoDB dynamoDb = new DynamoDB(client);
				dynamoDb.getTable("Person")
	              .putItem(new PutItemSpec().withItem(new Item().withNumber("id", person.getId())
	                .withString("name", person.getName())));
				JSONObject jsonHeader = new JSONObject();
				JSONObject jsonBody = new JSONObject();
				
				jsonHeader.put("x-custom-header", "my custom header value");
				jsonBody.put("message", "New item created");
				
				responseJson.put("statusCode", 200);
		        responseJson.put("headers", jsonHeader);
		        responseJson.put("body", jsonBody.toString());
				
			}
		} catch (ParseException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
	    writer.write(responseJson.toString());
	    writer.close();
	}
}
