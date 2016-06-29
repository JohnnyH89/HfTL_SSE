/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hftl.studies.sse.group1.aufgabe2_sensor_temp;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import purejavahidapi.DeviceRemovalListener;
import purejavahidapi.HidDevice;
import purejavahidapi.HidDeviceInfo;
import purejavahidapi.InputReportListener;
import purejavahidapi.PureJavaHidApi;

/**
 *
 * @author Malte Christjan Koch
 */
public class ReadTemperatureThread {

    private Boolean connected = false;
    //private HidServices hidServices;
    HidDevice sensor = null;
    private float value = 0f;
    
    public void init() {
        //hidServices = HidManager.getHidServices();
        //hidServices.addHidServicesListener(this);

        HidDeviceInfo sensorInfo = null;
        List<HidDeviceInfo> devices = PureJavaHidApi.enumerateDevices();
        for (HidDeviceInfo dev : devices) {
            if (dev.getVendorId() == 5824 && dev.getProductId() == 1152) {
                sensorInfo = dev;
                break;
            }
        }
        if (sensorInfo != null) {
            try {
                sensor = PureJavaHidApi.openDevice(sensorInfo.getPath());
                sensor.setInputReportListener(new DeviceListener());
                sensor.setDeviceRemovalListener(new DeviceDeattached());
                connected = true;
            } catch (IOException ex) {
                Logger.getLogger(ReadTemperatureThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Boolean isConnected() {
        return connected;
    }

    public float getValue() {
        return this.value;
    }
    
    private class DeviceListener implements InputReportListener {

        @Override
        public void onInputReport(HidDevice source, byte id, byte[] data, int len) {
            String out = Arrays.toString(data).replaceAll(" ", "");
            String[] outSplit = out.split(",");
            String sTemp = outSplit[4];
            String multi = outSplit[5];

            int temp = Integer.decode(sTemp);
            int multiplier = temp > 40 ? 0 : 1;

            float outInt = temp + (256 * multiplier);
            outInt = outInt / 10;
            value = outInt;
        }
    }
    
    private class DeviceDeattached implements DeviceRemovalListener {

        @Override
        public void onDeviceRemoval(HidDevice source) {
            connected = false;
        }
        
    }
}
