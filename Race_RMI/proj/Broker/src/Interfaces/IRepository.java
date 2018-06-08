/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Enum.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author rute
 */
public interface IRepository extends Remote {
    public void writeLog() throws RemoteException;
    public void toLog() throws RemoteException;
    public boolean addHorsesToRun(int horseId) throws RemoteException;
    public void clearhorsesRunning() throws RemoteException;
    public int getNumberOfSpectators() throws RemoteException;
    public int getNumberOfRaces() throws RemoteException;
    public int getNumberOfRacesMissing() throws RemoteException;
    public void raceStarted() throws RemoteException;
    public void raceDone() throws RemoteException;
    public void setSpecStat(int spectator_id, SpectatorState state) throws RemoteException;
    public void setHorseProbabilitie(int horse_id,double probabilitie) throws RemoteException;
    public void setHorseStat(int horse_id, HorseState state) throws RemoteException;
    public void sethorseruns(int horse_id, int runs) throws RemoteException;
    public void setHorsePerformance(int horse_id, int performance) throws RemoteException;
    public void sethorseposition(int horse_id, int position) throws RemoteException;
    public void setspecbetamount(int spectator_id, double amount) throws RemoteException;
    public void setspecMoney(int spectator_id, double money) throws RemoteException;
    public void setbrokerstate(BrokerState brokerstate) throws RemoteException;
    public void setspecbets(int spec_id,int horse_id) throws RemoteException;
    public void sethorserank(int horse_id, int rank) throws RemoteException;
    public void clearhorserank() throws RemoteException;
    public int getHorsesPerRace() throws RemoteException;
    public int getTotalHorses() throws RemoteException;
    public void finished() throws RemoteException;
}
