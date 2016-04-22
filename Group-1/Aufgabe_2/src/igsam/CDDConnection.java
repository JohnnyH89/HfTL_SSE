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

import org.json.*;

public class CDDConnection {
	private static String deviceId = null;
	private static String serial = "1234";
	private static String deviceName = "newDevice";
	private static String ultraSecureEncodedDigest = "Basic SGZUTC1Hcm91cC0xOk1va2VsZXQxMw==";
	//private static String date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(new Date());
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		//		String authorization = username +":"+ new String(password);
		//		encodedAuth = Base64.encode(authorization.getBytes());
		
		deviceId = getDeviceId();
		System.out.println("DeviceId: " + deviceId);
		if (deviceId == null) {
			deviceId = createDevice ();
			if  (deviceId != null) {
				System.out.println (registerDevice());
			}
		} 
		
		testLocationUpdate();		
	}

	public static String getCurrentTimeStamp(){
		SimpleDateFormat SimpleDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
		return SimpleDate.format(new Date());
	}
	
	private static void testUpdateDataClassFromYourMom() {
		float temperature;
		String date;
		
		Random rand = new Random();
		while (true) {
			date = getCurrentTimeStamp();
			temperature = rand.nextFloat() * 100;
			System.out.println (temperature);
			sendTemperature(temperature, date);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	}
	
	private static void testLocationUpdate() throws Exception {
		float altitude = 67f;
		float start_longitude = 7.443106f;
		float start_latitude = 51.512747f;
		float end_longitude = 7.117240f;
		float end_latitude = 50.747883f;
		float cur_longitude = start_longitude;
		float cur_latitude = start_latitude;
		String date;
		int steps=20;
	
		float part_long = (start_longitude - end_longitude)/steps; 
		float part_lat =  (start_latitude - end_latitude)/steps;
		for (int i = 0; i<steps; i++)  {
			cur_longitude = cur_longitude - part_long;
			cur_latitude = cur_latitude - part_lat;
			date = getCurrentTimeStamp();		
			sendGPS(altitude, cur_longitude, cur_latitude, date);
			Thread.sleep(2000);
		}

		
	}
	
	
	
	private static String registerDevice() throws Exception {
		// TODO Auto-generated method stub
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/vnd.com.nsn.cumulocity.externalId+json;charset=UTF-8");
		RequestBody body = RequestBody.create(mediaType, "{\n\t\"externalId\": " + serial + ",\n    \"type\": \"c8y_Serial\"\n}");
		Request request = new Request.Builder()
		  .url("https://cdm.ram.m2m.telekom.com/identity/globalIds/" + deviceId + "/externalIds")
		  .post(body)
		  .addHeader("authorization", ultraSecureEncodedDigest)
		  .addHeader("content-type", "application/vnd.com.nsn.cumulocity.externalId+json;charset=UTF-8")
		  .addHeader("accept", "application/vnd.com.nsn.cumulocity.externalId+json")
		  .build();

		Response response = client.newCall(request).execute();
		
		if (response.code() == 201) {
			return "Device " + serial + " erfolgreich registriert.";
		}
		else {
			return "Konnte Device " + serial + " nicht registrieren, Error Code: " + response.code();
		}
		
	}

	private static String createDevice() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Creating device " + deviceName);
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/vnd.com.nsn.cumulocity.managedObject+json;charset=UTF-8");
		RequestBody body = RequestBody.create(mediaType, "{\n\t\"name\": \"" + deviceName +"\", \n    \"c8y_IsDevice\": {}\n}");
		Request request = new Request.Builder()
		  .url("https://cdm.ram.m2m.telekom.com/inventory/managedObjects")
		  .post(body)
		  .addHeader("authorization", ultraSecureEncodedDigest)
		  .addHeader("content-type", "application/vnd.com.nsn.cumulocity.managedObject+json;charset=UTF-8")
		  .addHeader("accept", "application/vnd.com.nsn.cumulocity.managedObject+json")
		  .build();
		Response response = client.newCall(request).execute();
		JSONObject jObj = null;
		System.out.println ("Create Device - Response Code: " + response.code());

		if (response.code() == 201) {
			String jsonData = response.body().string();
			jObj = new JSONObject(jsonData);
			//				cast "jObj" into JSONObject
			return (String) ((JSONObject)jObj).get("id");
		}
		else {
			return null;
		}
	}

	public static String getDeviceId() throws Exception{
		System.out.println("Getting DeviceId for Device: " + serial);
		
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
		  .url("https://cdm.ram.m2m.telekom.com/identity/externalIds/c8y_Serial/" + serial)
		  .get()
		  .addHeader("authorization", ultraSecureEncodedDigest)
		  .addHeader("c8y_serial", serial)
		  .build();
		
		Response response = client.newCall(request).execute();
		JSONObject jObj = null;

		if (response.code() == 200) {
			String jsonData = response.body().string();
			jObj = (JSONObject) new JSONObject (jsonData);
			return (String) ((JSONObject)jObj.get("managedObject")).get("id");
		} else {
			return null; 
		}
		

	}
	
	
	public static void sendGPS (float alt, float longitude, float latitude, String date) throws Exception{
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/vnd.com.nsn.cumulocity.event+json;charset=UTF-8");
		RequestBody body = RequestBody.create(mediaType, "{\n\t\"c8y_Position\": {\n    \t\"alt\": " + alt + ",\n      \t\"lng\": " + longitude + ",\n      \t\"lat\": " +  latitude + " },\n\t\"time\":\"" + date + "\",\n    \"source\": {\n    \t\"id\":\"" + deviceId + "\" }, \n    \"type\": \"c8y_LocationUpdate\",\n  \"text\": \"LocUpdate\"\n}");
		Request request = new Request.Builder()
		  .url("https://cdm.ram.m2m.telekom.com/event/events")
		  .post(body)
		  .addHeader("authorization", ultraSecureEncodedDigest)
		  .addHeader("content-type", "application/vnd.com.nsn.cumulocity.event+json;charset=UTF-8")
		  .addHeader("accept", "application/vnd.com.nsn.cumulocity.event+json")
		  .build();

		Response response = client.newCall(request).execute();
		if (response.code() == 201) {
			System.out.println("Send LocationUpdate: - alt: " + alt + " long: " + longitude + " lat: " + latitude);
		} else {
			System.out.println("Error: Could not send LocationUpdate, Error Code: " + response.code()); 
		}
		
	}
	
	
	public static void sendTemperature (float value, String date){
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
		  .addHeader("authorization", ultraSecureEncodedDigest )
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
