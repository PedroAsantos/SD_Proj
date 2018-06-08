package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IBroker_Stable extends Remote{
	public void summonHorsesToPaddock() throws RemoteException;
	public void summonHorsesToEnd() throws RemoteException;
        void turnOffServer() throws RemoteException;
}
