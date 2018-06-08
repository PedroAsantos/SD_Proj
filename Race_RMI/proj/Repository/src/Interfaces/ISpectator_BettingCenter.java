package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ISpectator_BettingCenter extends Remote{
	public void placeABet(int spectatorId,double money,int horsePicked) throws RemoteException;
	public double goCollectTheGains(int spectatorId) throws RemoteException;
}
