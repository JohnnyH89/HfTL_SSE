package de.hftl.studies.sse.group1.aufgabe2;

import de.hftl.studies.sse.group1.aufgabe2_interface.IOTGatewaySensorInterface;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.text.SimpleDateFormat;
import java.util.Date;

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
            deviceId = createDevice();
            if (deviceId != null) {
                System.out.println(registerDevice());
            }
        }

       

        try { //special exception handler for registry creation
            System.out.println("Start RMI Thread...");
            LocateRegistry.createRegistry(1099);
            System.out.println("Java RMI registry created.");
            IOTGatewaySensorInterface iotGateway = new IOTGatewaySensorImpl(ultraSecureEncodedDigest, deviceId, deviceName, serial);
            Naming.rebind("IOTGatewaySensorInterface", (Remote) iotGateway);
            System.out.println("Gatewayserver bound in registry and Listening...");
        } catch (RemoteException e) {
            //do nothing, error means registry already exists
            System.out.println("java RMI registry already exists.");
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
        } else {
            return "Konnte Device " + serial + " nicht registrieren, Error Code: " + response.code();
        }

    }

    private static String createDevice() throws Exception {
        // TODO Auto-generated method stub
        System.out.println("Creating device " + deviceName);
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/vnd.com.nsn.cumulocity.managedObject+json;charset=UTF-8");
        RequestBody body = RequestBody.create(mediaType, "{\n\t\"name\": \"" + deviceName + "\", \n    \"c8y_IsDevice\": {}\n}");
        Request request = new Request.Builder()
                .url("https://cdm.ram.m2m.telekom.com/inventory/managedObjects")
                .post(body)
                .addHeader("authorization", ultraSecureEncodedDigest)
                .addHeader("content-type", "application/vnd.com.nsn.cumulocity.managedObject+json;charset=UTF-8")
                .addHeader("accept", "application/vnd.com.nsn.cumulocity.managedObject+json")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject jObj = null;
        System.out.println("Create Device - Response Code: " + response.code());

        if (response.code() == 201) {
            String jsonData = response.body().string();
            jObj = new JSONObject(jsonData);
            //				cast "jObj" into JSONObject
            return (String) ((JSONObject) jObj).get("id");
        } else {
            return null;
        }
    }

    public static String getDeviceId() throws Exception {
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
            jObj = (JSONObject) new JSONObject(jsonData);
            return (String) ((JSONObject) jObj.get("managedObject")).get("id");
        } else {
            return null;
        }

    }

}
