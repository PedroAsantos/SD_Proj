package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IHorse_Track extends Remote{
	public void proceedToStartLine(int horseId,int performance) throws RemoteException;
	public void makeAMove(int horseId) throws RemoteException;
	public boolean hasFinishLineBeenCrossed(int horseId) throws RemoteException;

}
