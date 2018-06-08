package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ISpectator_Control extends Remote{
	public void goWatchTheRace(int spectator_id) throws RemoteException;
	public void relaxABit(int spectator_id) throws RemoteException;
	public boolean haveIwon(int spectator_id,int horsePicked) throws RemoteException;
	public boolean noMoreRaces() throws RemoteException;
}
