package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IBroker_BettingCenter extends Remote {
	public void acceptTheBets() throws RemoteException;
	public void honourTheBets() throws RemoteException;
	public boolean areThereAnyWinners(int[] horseAWinners) throws RemoteException;

}
