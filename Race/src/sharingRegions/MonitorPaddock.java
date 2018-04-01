package sharingRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import Interfaces.IHorse_Paddock;
import Interfaces.ISpectator_Paddock;
import stakeholders.Horse;

public class MonitorPaddock implements IHorse_Paddock, ISpectator_Paddock {
	private final ReentrantLock mutex;
	private final Condition horse_condition;
	private final Condition spectatorWaiting_condition;
	private final Condition spectatorCheckHorses_condition;
	private final int horsesPerRace;
	private final int totalSpectators;
	
	private boolean horsesCanNotGo;
	private boolean spectatorsCheckingHorses;
	private int horsesInPaddock;
	private int spectatorsInPaddock;
	Repository repo;
	
	public MonitorPaddock(Repository repo) {
		mutex = new ReentrantLock(true);
		horse_condition = mutex.newCondition();
		spectatorWaiting_condition = mutex.newCondition();
		spectatorCheckHorses_condition = mutex.newCondition();
		this.horsesPerRace=repo.getHorsesPerRace();
		this.horsesCanNotGo=true;
		this.totalSpectators=repo.getNumberOfSpectators();
		spectatorsCheckingHorses=true;
		spectatorsInPaddock=0;
		horsesInPaddock=0;
		this.repo = repo;
	}
	

	
	@Override
	public void proceedToPaddock(Horse horse) {
		
		mutex.lock();
		try {
			
			horsesInPaddock++;
			System.out.println("Horse_"+horse.getID()+" is going to paddock!");
			repo.sethorseruns(horse.getID(),horse.getRuns());					
			if(horsesInPaddock==horsesPerRace) {
				spectatorWaiting_condition.signalAll();
			}
			
			try {
				 while(horsesCanNotGo) {
					 horse_condition.await();
				 }
			}catch(Exception e) {
				System.out.println("Horse ProceedToPaddock (paddockMonitor) "+e);
			}
			
				 
				 horsesInPaddock--;
				 
				 if(horsesInPaddock==0) {
					 horsesCanNotGo=true;
				 }
				 
				 
		} finally {
			mutex.unlock();
		}
		
	
	}
	@Override
	public void waitForNextRace(int spectator_id) {
		mutex.lock();
		try {
			System.out.println("Spectator_"+spectator_id+" is waiting for checking the horses!");
			while(horsesInPaddock < horsesPerRace) {
				try {
					spectatorWaiting_condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		} finally {
			mutex.unlock();
		}
		
	}


	@Override
	public void goCheckHorses(int spectator_id) {
		mutex.lock();
		try {
			System.out.println("Spectator_"+spectator_id+" is checking the horses!");
			spectatorsInPaddock++;
			
			if(spectatorsInPaddock==totalSpectators) {
				spectatorsCheckingHorses=false;
				spectatorCheckHorses_condition.signalAll();
				//last spectator wakes up the horses
				horsesCanNotGo=false;
				horse_condition.signalAll();
			}
			
			while(spectatorsCheckingHorses) {
				try {
					spectatorCheckHorses_condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			spectatorsInPaddock--;
			if(spectatorsInPaddock==0) {
				spectatorsCheckingHorses=true;
			}
			
				
			
		} finally {
			mutex.unlock();
		}
		
	}
	
}
