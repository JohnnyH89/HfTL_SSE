/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hftl.studies.sse.group1.aufgabe2_sensor_temp;

import de.hftl.studies.sse.group1.aufgabe2_interface.IOTGatewaySensorInterface;
import java.rmi.Naming;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesListener;
import org.hid4java.event.HidServicesEvent;

/**
 *
 * @author baphs
 */
public class TemperatureSensorImpl  {
    private static IOTGatewaySensorInterface gateway;
    
     public static void main(String args[]) throws Exception {
        Object obj = Naming.lookup("rmi://localhost/IOTGatewaySensorInterface");
        System.out.println("This client will send a few GPS updates via the Gateway and then destruct itself...");
        gateway = (IOTGatewaySensorInterface) obj;

        ReadTemperatureThread rtt = new ReadTemperatureThread();
        rtt.init();
        while(rtt.isConnected()) {
            gateway.sendTemperature(rtt.getTemp(), getCurrentTimeStamp());
        }
    }

    private static String getCurrentTimeStamp() {
        SimpleDateFormat SimpleDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        return SimpleDate.format(new Date());
    }
}
