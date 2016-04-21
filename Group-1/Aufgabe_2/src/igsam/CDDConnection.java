package igsam;

import java.awt.PageAttributes.MediaType;

import okhttp3.OkHttpClient;

public class CDDConnection {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/vnd.com.nsn.cumulocity.measurement+json");
		RequestBody body = RequestBody.create(mediaType, "{\n\t\"c8y_TemperatureMeasurement\": {\n    \t\"T\": { \n        \t\"value\": 40,\n            \"unit\": \"C\" }\n        },\n    \"time\":\"2016-04-21T19:52:00.000+02:00\", \n    \"source\": {\n    \t\"id\":\"45235\" }, \n    \"type\": \"c8y_TemperatureMeasurement\"\n}");
		Request request = new Request.Builder()
		  .url("https://cdm.ram.m2m.telekom.com/measurement/measurements")
		  .post(body)
		  .addHeader("authorization", "Basic SGZUTC1Hcm91cC0xOk1va2VsZXQxMw==")
		  .addHeader("content-type", "application/vnd.com.nsn.cumulocity.measurement+json")
		  .addHeader("accept", "application/vnd.com.nsn.cumulocity.measurement+json")
		  .addHeader("cache-control", "no-cache")
		  .addHeader("postman-token", "2d73a87a-6d42-2c68-52e8-49e69c65b959")
		  .build();

		Response response = client.newCall(request).execute();
	}

}
