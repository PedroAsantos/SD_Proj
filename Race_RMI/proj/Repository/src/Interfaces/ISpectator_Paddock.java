package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ISpectator_Paddock extends Remote{ 
	public int goCheckHorses(int spectator_id) throws RemoteException;
	public void waitForNextRace(int spectator_id) throws RemoteException;
}
