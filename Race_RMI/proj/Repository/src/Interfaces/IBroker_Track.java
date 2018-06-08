package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IBroker_Track extends Remote{
	public void startTheRace() throws RemoteException;
	public int[] reportResults() throws RemoteException;
}
