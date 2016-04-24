/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hftl.studies.sse.group1.aufgabe2;

import de.hftl.studies.sse.group1.aufgabe2_interface.IOTGatewaySensorInterface;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * @author MalteChristjan
 */
public class IOTGatewaySensorImpl extends UnicastRemoteObject implements IOTGatewaySensorInterface{
    
    private static String deviceId = null;
    private static String serial = null;
    private static String deviceName = null;
    private static String ultraSecureEncodedDigest = null;
    
    /**
     * Constructor.
     * 
     * @param ultraSecureEncodedDigest
     * @param deviceId
     * @param deviceName
     * @param serial
     * @throws RemoteException 
     */
    public IOTGatewaySensorImpl(String ultraSecureEncodedDigest, String deviceId, String deviceName, String serial) throws RemoteException {
        super(0);
        
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.serial = serial;
        this.ultraSecureEncodedDigest = ultraSecureEncodedDigest;
    }
    
    @Override
    public Boolean sendMessage(String message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean sendGPS(float alt, float longitude, float latitude, String date) {
        try {
            OkHttpClient client = new OkHttpClient();
            
            MediaType mediaType = MediaType.parse("application/vnd.com.nsn.cumulocity.event+json;charset=UTF-8");
            RequestBody body = RequestBody.create(mediaType, "{\n\t\"c8y_Position\": {\n    \t\"alt\": " + alt + ",\n      \t\"lng\": " + longitude + ",\n      \t\"lat\": " + latitude + " },\n\t\"time\":\"" + date + "\",\n    \"source\": {\n    \t\"id\":\"" + deviceId + "\" }, \n    \"type\": \"c8y_LocationUpdate\",\n  \"text\": \"LocUpdate\"\n}");
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
                return true;
            } else {
                System.out.println("Error: Could not send LocationUpdate, Error Code: " + response.code());
            }
        } catch (IOException ex) {
            Logger.getLogger(IOTGatewaySensorImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public Boolean sendTemperature(float value, String date) throws RemoteException {
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
                .addHeader("authorization", ultraSecureEncodedDigest)
                .addHeader("content-type", "application/vnd.com.nsn.cumulocity.measurement+json;charset=UTF-8;ver=0.9")
                .build();

        try {
            System.out.println(request.toString());
            Response response = client.newCall(request).execute();
            System.out.println(response.message());
            System.out.println(response.toString());
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    
}
