package src;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorPaddock implements IHorse_Paddock, ISpectator_Paddock {
	private final ReentrantLock mutex;
	private final Condition horse_condition;
	private final Condition spectator_condition;
	private final int total_horses;
	
	private boolean horsesCanNotGo;
	private int horsesInPaddock;
	
	public MonitorPaddock(int total_horses) {
		mutex = new ReentrantLock(true);
		horse_condition = mutex.newCondition();
		spectator_condition = mutex.newCondition();
		this.total_horses=total_horses;
		this.horsesCanNotGo=true;
		horsesInPaddock=0;
	}
	
	@Override
	public void proceedToStartLine() {
		// TODO Auto-generated method stub
		//este metodo não sera no monitor RacingTrack?
		mutex.lock();
		//horsesInPaddock--;
		try {
			 while(horsesInPaddock < total_horses) {
				 horse_condition.await();
			 }
		}catch(Exception e) {
			System.out.println("Horse ProceedToStarLine "+e);
		}
		
		
		
		mutex.unlock();
		
	}
	public void proceedToPaddock(int horse_id) {
		
		mutex.lock();
		try {
			
			horsesInPaddock++;
			
			if(horsesInPaddock==total_horses) {
				spectator_condition.signalAll();
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
	public void goCheckHorses(int spectator_id) {
		mutex.lock();
		try {
			System.out.println("Spectator_"+spectator_id+" is checking the horses!");
			while(horsesInPaddock < total_horses) {
				try {
					spectator_condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			horsesCanNotGo=false;
			
			
			
		} finally {
			mutex.unlock();
		}
		
	}
	
}
