/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author rute
 */
public interface IMonitor_BettingCenter extends IBroker_BettingCenter, ISpectator_BettingCenter, Remote {
    
    /**
     * This function is used for the log to signal the BettingCenter to shutdown.
     *
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public void signalShutdown() throws RemoteException, IOException;
}
