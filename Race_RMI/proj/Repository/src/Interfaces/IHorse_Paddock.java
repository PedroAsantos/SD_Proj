package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IHorse_Paddock extends Remote {
    public void proceedToPaddock(int horseId,int performance) throws RemoteException;
}
