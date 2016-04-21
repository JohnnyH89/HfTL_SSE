package igsam;



import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CDDConnection {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int deviceID = 45235;
		float temperature;
		String basicDigest = "SGZUTC1Hcm91cC0xOk1va2VsZXQxMw==";
		
		//		String authorization = username +":"+ new String(password);
		//		encodedAuth = Base64.encode(authorization.getBytes());
		
		String date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(new Date());
		
		Random rand = new Random();
		
		
		while (true) {
			temperature = rand.nextFloat() * 100;
			System.out.println (temperature);
			sendTemperature(deviceID, temperature, date, basicDigest);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	////Michael is watching us!
	//Ok, jetzt kommt das wichtige!!!
	
	public static void sendGPS (){
		//mach ICH!!!
	}
	
	
	public static void sendTemperature (int deviceId, float value, String date, String basicDigest){
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/vnd.com.nsn.cumulocity.measurement+json;charset=UTF-8");
		RequestBody body = RequestBody.create(mediaType, ""
				+ "{\"c8y_TemperatureMeasurement\": {"
				+ "\"T\": {"
				+ "\"value\":" + value + ","
				+ "\"unit\": \"C\" }"
				+ "},"
				+ "\"time\":\"" + date + "\","
				+ "\"source\": {"
				+ "\"id\":\"" + deviceId + "\" },"
				+ "\"type\": \"c8y_TemperatureMeasurement\""
				+ "}");
		Request request = new Request.Builder()
		  .url("https://cdm.ram.m2m.telekom.com/measurement/measurements")
		  .post(body)
		  .addHeader("authorization", "Basic " +  basicDigest )
		  .addHeader("content-type", "application/vnd.com.nsn.cumulocity.measurement+json;charset=UTF-8;ver=0.9")
		  .build();

		try {
			System.out.println(request.toString());
			Response response = client.newCall(request).execute();
			System.out.println(response.message());
			System.out.println(response.toString());
			System.out.print("KRASSER SHIT!!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
