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

/**
 *
 * @author baphs
 */
public class TemperatureSensorImpl {
    private static IOTGatewaySensorInterface gateway;
    
     public static void main(String args[]) throws Exception {
        Object obj = Naming.lookup("rmi://localhost/IOTGatewaySensorInterface");
        System.out.println("This client will send a few GPS updates via the Gateway and then destruct itself...");
        gateway = (IOTGatewaySensorInterface) obj;
        
        testTemperatureUpdate();
    }

    private static String getCurrentTimeStamp() {
        SimpleDateFormat SimpleDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        return SimpleDate.format(new Date());
    }

    private static void testTemperatureUpdate() throws Exception {
        float temp = -40f;
        
        int steps = 40;
        for (int i = 0; i < steps; i++) {
            
            temp += (i * 2.5f);
            
            if(gateway.sendTemperature(temp, getCurrentTimeStamp())) {
                System.out.println("Success");
            } else {
                System.out.println("Failed!");
            }
            Thread.sleep(2000);
        }
    }
    
    
}
