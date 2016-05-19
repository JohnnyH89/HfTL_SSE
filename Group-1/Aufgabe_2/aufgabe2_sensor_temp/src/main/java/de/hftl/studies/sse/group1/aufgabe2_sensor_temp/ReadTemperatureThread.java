/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hftl.studies.sse.group1.aufgabe2_sensor_temp;

import java.util.Arrays;
import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesListener;
import org.hid4java.event.HidServicesEvent;

/**
 *
 * @author baphs
 */
public class ReadTemperatureThread implements HidServicesListener {

    private Boolean connected = false;
        private HidServices hidServices;
    HidDevice sensor;
    public void init() {
        hidServices = HidManager.getHidServices();
        hidServices.addHidServicesListener(this);
        
        sensor = hidServices.getHidDevice(0x16C0, 0x0480, null);
        if(sensor != null) {
            connected = true;
        }
    }
    
    public float getTemp() {
        byte[] data = new byte[64];
        sensor.read(data);

        String out = Arrays.toString(data).replaceAll(" ", "");
        String[] outSplit = out.split(",");
        String sTemp = outSplit[4];
        String multi = outSplit[5];
        
        
        int temp = Integer.decode(sTemp);
        int multiplier = temp > 40 ? 0 : 1;
        
        float outInt = temp + (256 * multiplier);
        outInt = outInt / 10;
       
        return outInt;
    }
    
    public Boolean isConnected() {
        return connected;
    }
    
    @Override
    public void hidDeviceAttached(HidServicesEvent hse) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void hidDeviceDetached(HidServicesEvent hse) {
        connected = false;
    }

    @Override
    public void hidFailure(HidServicesEvent hse) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }    
}
