package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IHorse_Stable extends Remote{
	 public boolean proceedToStable(int horseId) throws RemoteException;

}
