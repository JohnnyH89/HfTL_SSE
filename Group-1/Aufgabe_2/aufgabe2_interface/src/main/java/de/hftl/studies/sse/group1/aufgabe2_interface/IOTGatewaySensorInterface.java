/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hftl.studies.sse.group1.aufgabe2_interface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author MalteChristjan
 */
public interface IOTGatewaySensorInterface extends Remote {
    /**
     * Send a Message for the current Device.
     * @param message
     * @return
     * @throws RemoteException 
     */
     public Boolean sendMessage(String message) throws RemoteException;
     /**
      * Send GPS for the currentDevice.
      * @param alt
      * @param longitude
      * @param latitude
      * @param date
      * @return
      * @throws RemoteException 
      */
     public Boolean sendGPS(float alt, float longitude, float latitude, String date) throws RemoteException;
     /**
      * sendTemperature for the current Device.
      * @param value
      * @param date
      * @return
      * @throws RemoteException 
      */
     public Boolean sendTemperature(float value, String date) throws RemoteException;
}
