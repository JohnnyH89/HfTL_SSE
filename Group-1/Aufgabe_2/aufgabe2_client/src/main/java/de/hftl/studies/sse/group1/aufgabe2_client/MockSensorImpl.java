/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hftl.studies.sse.group1.aufgabe2_client;

import de.hftl.studies.sse.group1.aufgabe2_interface.IOTGatewaySensorInterface;
import java.rmi.Naming;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author MalteChristjan
 */
public class MockSensorImpl {

    private static IOTGatewaySensorInterface gateway;

    public static void main(String args[]) throws Exception {
        Object obj = Naming.lookup("rmi://localhost/IOTGatewaySensorInterface");
        System.out.println("This client will send a few GPS updates via the Gateway and then destruct itself...");
        gateway = (IOTGatewaySensorInterface) obj;

        testLocationUpdate();
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat SimpleDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        return SimpleDate.format(new Date());
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
        int steps = 20;

        float part_long = (start_longitude - end_longitude) / steps;
        float part_lat = (start_latitude - end_latitude) / steps;
        for (int i = 0; i < steps; i++) {
            cur_longitude = cur_longitude - part_long;
            cur_latitude = cur_latitude - part_lat;
            date = getCurrentTimeStamp();
            if(gateway.sendGPS(altitude, cur_longitude, cur_latitude, date)) {
                System.out.println("Success");
            } else {
                System.out.println("Failed!");
            }
            Thread.sleep(2000);
        }

    }
}
