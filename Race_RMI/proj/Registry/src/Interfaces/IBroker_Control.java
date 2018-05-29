package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IBroker_Control extends Remote{
	public void reportResults(int[] horseAWinners) throws RemoteException;
	public void entertainTheGuests() throws RemoteException;
}
