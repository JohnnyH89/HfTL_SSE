package igsam;



import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CDDConnection {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/vnd.com.nsn.cumulocity.measurement+json");
		RequestBody body = RequestBody.create(mediaType, "{\n\t\"c8y_TemperatureMeasurement\": {\n    \t\"T\": { \n        \t\"value\": 40,\n            \"unit\": \"C\" }\n        },\n    \"time\":\"2016-04-21T19:51:00.000+02:00\", \n    \"source\": {\n    \t\"id\":\"45235\" }, \n    \"type\": \"c8y_TemperatureMeasurement\"\n}");
		Request request = new Request.Builder()
		  .url("https://cdm.ram.m2m.telekom.com/measurement/measurements")
		  .post(body)
		  .addHeader("authorization", "Basic SGZUTC1Hcm91cC0xOk1va2VsZXQxMw==")
		  .addHeader("content-type", "application/vnd.com.nsn.cumulocity.measurement+json")
		  .addHeader("accept", "application/vnd.com.nsn.cumulocity.measurement+json")
		  .addHeader("cache-control", "no-cache")
		  .addHeader("postman-token", "bdc325db-9bbd-5162-dab4-775e4159004b")
		  .build();


		try {
			System.out.println(request.toString());
			Response response = client.newCall(request).execute();
			System.out.println(response.message());
			System.out.println(response.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
