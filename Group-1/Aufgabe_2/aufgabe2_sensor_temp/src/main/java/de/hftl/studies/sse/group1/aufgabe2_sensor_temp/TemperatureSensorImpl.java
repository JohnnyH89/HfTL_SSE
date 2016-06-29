/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hftl.studies.sse.group1.aufgabe2_sensor_temp;

import de.hftl.studies.sse.group1.aufgabe2_interface.IOTGatewaySensorInterface;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author Malte Christjan Koch
 */
public class TemperatureSensorImpl  {
    private static IOTGatewaySensorInterface gateway;
    
     public static void main(String args[]) throws Exception {
       // Object obj = Naming.lookup("rmi://localhost/IOTGatewaySensorInterface");
        System.out.println("This client will send a few Temperature updates via the Gateway and then destruct itself. Press Return to start");
        Scanner a = new Scanner(System.in);
        a.nextLine();
        //gateway = (IOTGatewaySensorInterface) obj;

        ReadTemperatureThread rtt = new ReadTemperatureThread();
        rtt.init();
        while(rtt.isConnected()) {
            Thread.sleep(3000);
            //gateway.sendTemperature(rtt.getTemp(), getCurrentTimeStamp());
            System.out.println("Temp: " + rtt.getValue());
        }
        System.out.println("Device has been removed. Shutting down.");
    }

    private static String getCurrentTimeStamp() {
        SimpleDateFormat SimpleDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        return SimpleDate.format(new Date());
    }
}
